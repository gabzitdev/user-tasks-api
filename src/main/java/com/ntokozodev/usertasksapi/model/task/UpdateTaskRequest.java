package com.ntokozodev.usertasksapi.model.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateTaskRequest {
    private String name;
    private String description;
    private String date_time;
}