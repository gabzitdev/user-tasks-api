package com.ntokozodev.usertasksapi.exception;

public class EntityDuplicateException extends Exception {
    public EntityDuplicateException() {
    }

    public EntityDuplicateException(String message) {
        super(message);
    }

    public EntityDuplicateException(Throwable cause) {
        super(cause);
    }

    public EntityDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
