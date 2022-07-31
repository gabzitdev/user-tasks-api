package com.ntokozodev.usertasksapi.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private long id;
    private String username;
    private String first_name;
    private String last_name;
}
