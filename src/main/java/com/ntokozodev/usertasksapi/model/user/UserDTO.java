package com.ntokozodev.usertasksapi.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTO {
    @NotNull
    private long id;
    @NotNull
    private String username;
    @NotNull
    private String first_name;
    @NotNull
    private String last_name;
}
