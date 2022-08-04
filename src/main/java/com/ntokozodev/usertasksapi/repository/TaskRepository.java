package com.ntokozodev.usertasksapi.repository;

import java.util.List;

import com.ntokozodev.usertasksapi.common.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ntokozodev.usertasksapi.model.db.Task;
import com.ntokozodev.usertasksapi.model.db.User;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    List<Task> findByUser(User user);
    Page<Task> findByUser(User user, Pageable pageable);
    List<Task> findByStatus(Status status);
}
