package com.ntokozodev.usertasksapi.model.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {
    private int id;
    private String name;
    private String description;
    private String date_time;
}
