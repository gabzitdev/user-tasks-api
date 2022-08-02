package com.ntokozodev.usertasksapi.model.task;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TaskRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String date_time;
}
