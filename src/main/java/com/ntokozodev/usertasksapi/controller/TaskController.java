package com.ntokozodev.usertasksapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntokozodev.usertasksapi.exception.EntityNotFoundException;
import com.ntokozodev.usertasksapi.model.db.Task;
import com.ntokozodev.usertasksapi.model.task.TaskRequest;
import com.ntokozodev.usertasksapi.model.task.TaskResponse;
import com.ntokozodev.usertasksapi.model.task.UpdateTaskRequest;
import com.ntokozodev.usertasksapi.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.ntokozodev.usertasksapi.util.Util.logException;
import static com.ntokozodev.usertasksapi.util.Util.parseId;

@RestController
public class TaskController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final TaskService service;
    private final ObjectMapper mapper;

    public TaskController(TaskService taskService, ObjectMapper mapper) {
        this.service = taskService;
        this.mapper = mapper;
    }

    @PostMapping("/api/users/{id}/tasks")
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskRequest request, @PathVariable("id") String id) {
        LOG.info("[createTask] request for Id: [{}] - task: [{}]", id, request.getName());

        try {
            var task = service.createTask(request, parseId(id));
            var response = createResponse(task);
            var body = mapper.writeValueAsString(response);
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logException("getUserById", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("createTask", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<String> updateTask(@PathVariable("user_id") String userId, @PathVariable("task_id") String taskId, @RequestBody UpdateTaskRequest request) {
        LOG.info("[updateUserTask] request for userId: [{}] - taskId: [{}]", userId, taskId);

        try {
            var task = service.updateUserTask(request, parseId(userId), parseId(taskId));
            var response = createResponse(task);
            var body = mapper.writeValueAsString(response);
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            logException("updateUserTask", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (EntityNotFoundException ex) {
            logException("updateUserTask", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("updateUserTask", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<String> deleteTask(@PathVariable("user_id") String userId, @PathVariable("task_id") String taskId) {
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
    public ResponseEntity<String> getTask(@PathVariable("user_id") String userId, @PathVariable("task_id") String taskId) {
        LOG.info("[getTask] request for userId: [{}] - taskId: [{}]", userId, taskId);

        try {
            var task = service.getUserTask(parseId(userId), parseId(taskId));
            var response = createResponse(task);
            var body = mapper.writeValueAsString(response);
            var headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logException("getTask", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            logException("getTask", ex);
            return new ResponseEntity<>(ex.getMessage() ,HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception ex) {
            logException("getTask", ex);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/users/{user_id}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable("user_id") String userId) {
        LOG.info("[getTasks] request for userId: [{}]", userId);

        try {
            var responses = new ArrayList<TaskResponse>();
            var tasks = service.getAllUserTasks(parseId(userId));
            tasks.forEach(task -> responses.add(createResponse(task)));

            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex) {
            logException("getTasks", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private TaskResponse createResponse(Task task) {
        var response = new TaskResponse();
        response.setId(task.getId());
        response.setName(task.getName());
        response.setDescription(task.getDescription());
        response.setDate_time(task.getDate_time());

        return response;
    }
}
