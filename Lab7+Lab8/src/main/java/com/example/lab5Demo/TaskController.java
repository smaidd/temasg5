package com.example.lab5Demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService service;
    @Autowired
    private Validator validator;

    @Operation(summary = "Search tasks", operationId = "getTasks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found tasks",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskModel[].class))
                    }
            ),
            @ApiResponse(responseCode = "204", description = "No tasks found")
    })
    @GetMapping
    public ResponseEntity<List<Object>> getTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) TaskModel.TaskStatus status,
            @RequestParam(required = false) TaskModel.TaskSeverity severity,
            @RequestHeader(name="X-Fields", required = false) String fields,
            @RequestHeader(name="X-Sort", required = false) String sort){

        List<TaskModel> tasks = service.getTasks(title, description, assignedTo, severity, status);
        if(tasks.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else{
            List<Object> items = null;

            if(fields != null && !fields.isBlank()){
                items = tasks.stream()
                        .map(task-> task.sparseFields(fields.split(","))).collect(Collectors.toList());
            }
            else{
                items = new ArrayList<Object>(tasks);
            }

            if(sort != null && !sort.isBlank()){
                items = items.stream().sorted((first, second) -> BaseModel.sorter(sort).compare(first, second))
                        .collect(Collectors.toList());
            }
            return ResponseEntity.ok(items);
        }

    }

    @Operation(summary = "Get a task", operationId = "getTask")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found task",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskModel.class))
                    }
            ),
            @ApiResponse(responseCode = "204", description = "No task found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable String id,
                                              @RequestHeader(name="X-Fields", required = false) String fields){
        Optional<TaskModel> task = service.getTask(id);
        if(task.isPresent()){
            if(fields!= null && !fields.isBlank()){
                return ResponseEntity.ok(task.get().sparseFields(fields.split(",")));
            }
            else{
                return ResponseEntity.ok(task.get());
            }
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a task", operationId = "addTask")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task was created",
                    headers = {@Header(name="location", schema = @Schema(type="string"))}
            ),
            @ApiResponse(responseCode = "204", description = "Bulk tasks created"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping
    public ResponseEntity<Void> addTask(@RequestBody String payload,
                                        @RequestHeader(name = "X-Action", required = false) String action) throws Exception {

            if ("bulk".equals(action)) {
                TaskModel[] tasks = new ObjectMapper().readValue(payload, TaskModel[].class);
                validate(tasks);
                for(TaskModel task : tasks){
                    service.addTask(task);
                }
                return ResponseEntity.noContent().build();
            }
            else{
                TaskModel task = new ObjectMapper().readValue(payload, TaskModel.class);
                validate(new Object[]{task});
                task = service.addTask(task);
                URI uri = WebMvcLinkBuilder.linkTo(getClass()).slash(task.getId()).toUri();
                return ResponseEntity.created(uri).build();
            }
    }

    @Operation(summary = "Update a task", operationId = "updateTask")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task was updated"
            ),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask (@PathVariable String id, @Valid @RequestBody TaskModel task) throws Exception {
        Optional<TaskModel> existingTask = service.getTask(id);
        if(existingTask.isPresent()){
            existingTask.get().update(task);
            service.updateTask(existingTask.get());
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Patch a task", operationId = "patchTask")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task was patched"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchTask (@PathVariable String id, @RequestBody TaskModel task) throws Exception {
        Optional<TaskModel> existingTask = service.getTask(id);
        if(existingTask.isPresent()){
            existingTask.get().patch(task);
            service.updateTask(existingTask.get());
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a task", operationId = "deleteTask")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task was deleted"),
            @ApiResponse(responseCode = "500", description = "Something went wrong"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) throws Exception {
        if(service.getTask(id).isPresent()){
            service.removeTask(id);
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    private void validate(Object[] objects){
        String message = Arrays.stream(objects)
                .map(object -> validator.validate(object).stream()
                        .map(contraintViolation -> contraintViolation.getMessage())
                        .filter(error -> !error.isBlank())
                        .collect(Collectors.joining("|"))).filter(error -> !error.isBlank())
                .collect(Collectors.joining("|"));
        if(!message.isBlank()){
            throw new ValidationException((message));
        }
    }
}
