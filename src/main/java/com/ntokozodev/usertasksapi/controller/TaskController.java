package com.ntokozodev.usertasksapi.controller;

import static com.ntokozodev.usertasksapi.common.Util.logException;
import static com.ntokozodev.usertasksapi.common.Util.parseId;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntokozodev.usertasksapi.exception.EntityNotFoundException;
import com.ntokozodev.usertasksapi.model.db.Task;
import com.ntokozodev.usertasksapi.model.task.TaskRequest;
import com.ntokozodev.usertasksapi.model.task.TaskResponse;
import com.ntokozodev.usertasksapi.model.task.UpdateTaskRequest;
import com.ntokozodev.usertasksapi.service.TaskService;

@RestController
public class TaskController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final TaskService service;
    private final ObjectMapper mapper;
    private final ModelMapper modelMapper;

    public TaskController(TaskService taskService, ObjectMapper mapper, ModelMapper modelMapper) {
        this.service = taskService;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/api/users/{id}/tasks")
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskRequest request, @PathVariable("id") String id) {
        LOG.info("[createTask] request for Id: [{}] - task: [{}]", id, request.getName());

        try {
            Task task = service.createTask(request, parseId(id));
            String body = mapper.writeValueAsString(mapTaskToResponse(task));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logException("createTask", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("createTask", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<String> updateTask(
            @PathVariable("user_id") String userId,
            @PathVariable("task_id") String taskId,
            @RequestBody UpdateTaskRequest request) {
        LOG.info("[updateTask] request for userId: [{}] - taskId: [{}]", userId, taskId);

        try {
            Task task = service.updateUserTask(request, parseId(userId), parseId(taskId));
            String body = mapper.writeValueAsString(mapTaskToResponse(task));
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            logException("updateTask", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityNotFoundException ex) {
            logException("updateTask", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("updateTask", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<String> deleteTask(@PathVariable("user_id") String userId,
            @PathVariable("task_id") String taskId) {
        LOG.info("[deleteTask] request for userId: [{}] - taskId: [{}]", userId, taskId);

        try {
            service.deleteUserTask(parseId(userId), parseId(taskId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            logException("deleteTask", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityNotFoundException ex) {
            logException("deleteTask", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("deleteTask", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<String> getTask(@PathVariable("user_id") String userId,
            @PathVariable("task_id") String taskId) {
        LOG.info("[getTask] request for userId: [{}] - taskId: [{}]", userId, taskId);

        try {
            Task task = service.getUserTask(parseId(userId), parseId(taskId));
            String body = mapper.writeValueAsString(mapTaskToResponse(task));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logException("getTask", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            logException("getTask", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception ex) {
            logException("getTask", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users/{user_id}/tasks/{page}")
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable("user_id") String userId) {
        LOG.info("[getTasks] request for userId: [{}]", userId);

        try {
            List<TaskResponse> responses = new ArrayList<>();
            List<Task> tasks = service.getUserTasks(parseId(userId));
            tasks.forEach(task -> responses.add(mapTaskToResponse(task)));

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex) {
            logException("getTasks", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private TaskResponse mapTaskToResponse(Task task) {
        return modelMapper.map(task, TaskResponse.class);
    }
}
