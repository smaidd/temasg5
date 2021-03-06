package com.example.lab5Demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {
    private File file;
    private Map<String, TaskModel> tasks;

    public TaskRepository (@Value("${repository}") String repository) throws IOException {
        file = Paths.get(repository).toFile();
        tasks = file.exists() ?
                Arrays.stream(new ObjectMapper().readValue(file, TaskModel[].class))
                        .collect(Collectors.toMap(task -> task.getId(), task -> task))
                : new HashMap<String, TaskModel>();
    }

    public Collection<TaskModel> findAll(){
        return tasks.values();
    }
    public Optional<TaskModel> findById(String id){
        return tasks.containsKey(id)
                ? Optional.of(tasks.get(id))
                : Optional.empty();
    }
    public void save(TaskModel task) throws IOException {
        if(tasks.containsKey(task.getId())){
            tasks.get(task.getId()).update(task);
        }
        else
        {
            tasks.put(task.getId(), task);
        }
        new ObjectMapper().writeValue(file, findAll());
    }
    public void deleteById(String id) throws IOException {
        if(tasks.containsKey(id)){
            tasks.remove(id);
            new ObjectMapper().writeValue(file, findAll());
        }
        else{
            throw new IllegalArgumentException("Invalid task id: " + id);
        }
    }
}
