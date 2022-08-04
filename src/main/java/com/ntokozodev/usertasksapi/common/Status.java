package com.ntokozodev.usertasksapi.common;

public enum Status {
    DONE("done"),
    PENDING("pending");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
