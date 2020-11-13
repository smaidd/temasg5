package com.example.lab5Demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskModel> getTasks(String title, String description, String assignTo, TaskModel.TaskSeverity severity, TaskModel.TaskStatus status){
        return taskRepository.findAll().stream()
                .filter( task -> isMatch(task, title, description, assignTo, severity, status ))
                .collect(Collectors.toList());
    }

    private boolean isMatch (TaskModel task, String title, String description, String assignTo, TaskModel.TaskSeverity severity, TaskModel.TaskStatus status){
        return ( title == null
                || task.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                &&
                (description == null
                || task.getDescription().toLowerCase().startsWith(description.toLowerCase()) )
                &&
                 (assignTo == null
                || task.getAssignedTo().toLowerCase().startsWith(assignTo.toLowerCase()) )
                &&
                (status == null || task.getStatus().equals(status) )
                &&
                (severity == null || task.getSeverity().equals(severity) );
    }
    public Optional<TaskModel> getTask(String id){
        return taskRepository.findById(id);
    }
    public TaskModel addTask (TaskModel task) throws IOException {
        taskRepository.save(task);
        return task;
    }
    public void updateTask (TaskModel task) throws IOException {
        taskRepository.save(task);
    }
    public void removeTask (String id) throws IOException {
        taskRepository.deleteById(id);
    }
}
