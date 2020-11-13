package com.example.lab5Demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService service;

    @GetMapping
    public ResponseEntity<List<TaskModel>> getTasks(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String assignedTo,
        @RequestParam(required = false) TaskModel.TaskStatus status,
        @RequestParam(required = false) TaskModel.TaskSeverity severity){
            List<TaskModel> tasks = service.getTasks(title, description, assignedTo, severity, status);
            return tasks.isEmpty() ?
                    ResponseEntity.noContent().build()
                    : ResponseEntity.ok(tasks);

    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable String id){
        Optional<TaskModel> task = service.getTask(id);
        return task.isPresent() ? ResponseEntity.ok(task.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> addTask(@RequestBody TaskModel task){
        try {
            service.addTask(task);
            URI uri = WebMvcLinkBuilder.linkTo(getClass()).slash(task.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask (@PathVariable String id, @RequestBody TaskModel task){
        Optional<TaskModel> existingTask = service.getTask(id);
        if(existingTask.isPresent()){
            existingTask.get().update(task);
            try {
                service.updateTask(existingTask.get());
                return ResponseEntity.noContent().build();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchTask (@PathVariable String id, @RequestBody TaskModel task){
        Optional<TaskModel> existingTask = service.getTask(id);
        if(existingTask.isPresent()){
            existingTask.get().patch(task);
            try {
                service.updateTask(existingTask.get());
                return ResponseEntity.noContent().build();
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id){
        if(service.getTask(id).isPresent()){
            try {
                service.removeTask(id);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
