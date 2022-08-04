package com.ntokozodev.usertasksapi.model.task;

import com.ntokozodev.usertasksapi.common.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TaskDTO {
    @NotNull
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String date_time;
    private Status status;
}
