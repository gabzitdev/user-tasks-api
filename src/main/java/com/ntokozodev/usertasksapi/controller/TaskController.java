package com.ntokozodev.usertasksapi.controller;

import static com.ntokozodev.usertasksapi.common.Util.logException;
import static com.ntokozodev.usertasksapi.common.Util.parseId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntokozodev.usertasksapi.common.Constants;
import com.ntokozodev.usertasksapi.exception.EntityNotFoundException;
import com.ntokozodev.usertasksapi.exception.ServiceException;
import com.ntokozodev.usertasksapi.model.Paging;
import com.ntokozodev.usertasksapi.model.db.Task;
import com.ntokozodev.usertasksapi.model.task.TaskDTO;
import com.ntokozodev.usertasksapi.model.task.TaskResponse;
import com.ntokozodev.usertasksapi.service.TaskService;

@RestController
@RequestMapping("/api")
public class TaskController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final TaskService service;
    private final ObjectMapper mapper;
    @Autowired
    private final ModelMapper modelMapper;

    public TaskController(TaskService taskService, ObjectMapper mapper, ModelMapper modelMapper) {
        this.service = taskService;
        this.mapper = mapper;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/users/{id}/tasks")
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskDTO request, @PathVariable("id") String id) {
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

    @PutMapping("/users/{user_id}/tasks/{task_id}")
    public ResponseEntity<String> updateTask(
            @PathVariable("user_id") String userId,
            @PathVariable("task_id") String taskId,
            @RequestBody TaskDTO request) {
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

    @DeleteMapping("/users/{user_id}/tasks/{task_id}")
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

    @GetMapping("/users/{user_id}/tasks/{task_id}")
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

    @GetMapping("/users/{user_id}/tasks")
    public ResponseEntity<TaskResponse> getTasks(
            @PathVariable("user_id") String userId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) int size) {
        LOG.info("[getTasks] request for userId: [{}]", userId);

        try {
            if (page != null) {
                return getResponseWithPaging(userId, page, size);
            }

            List<TaskDTO> taskDTOs = new ArrayList<>();
            List<Task> tasks = service.getUserTasks(parseId(userId));
            tasks.forEach(task -> taskDTOs.add(mapTaskToResponse(task)));
            TaskResponse response = new TaskResponse();
            response.setTasks(taskDTOs);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logException("getTasks", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logException("getTasks", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<TaskResponse> getResponseWithPaging(String userId, Integer page, int size)
            throws ServiceException, EntityNotFoundException {
        LOG.info("[getTasks] request for userId: [{}] | page: [{}]", userId, page);
        Page<Task> pages = service.getUserTasksPaginated(parseId(userId), page, size);
        List<Task> tasks = pages.getContent();
        List<TaskDTO> taskDTOs = tasks.stream().map(this::mapTaskToResponse).collect(Collectors.toList());

        TaskResponse response = new TaskResponse();
        Paging paging = new Paging();

        paging.setLast(pages.isLast());
        paging.setPageSize(pages.getSize());
        paging.setPageNumber(pages.getNumber());
        paging.setTotalPages(pages.getTotalPages());
        paging.setTotalElements(pages.getTotalElements());
        response.setPaging(paging);
        response.setTasks(taskDTOs);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private TaskDTO mapTaskToResponse(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

}
