package com.ntokozodev.usertasksapi.model.task;

import java.util.List;

import com.ntokozodev.usertasksapi.model.Paging;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {
    private List<TaskDTO> tasks;
    private Paging paging;
}
