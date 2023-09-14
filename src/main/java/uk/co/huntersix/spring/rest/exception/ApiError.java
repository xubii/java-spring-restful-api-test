package uk.co.huntersix.spring.rest.exception;

import java.io.Serializable;

public class ApiError {

    private String message;

    private Integer status;

    public ApiError(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
