package com.distrupify.exceptions;

import jakarta.ws.rs.core.Response;

public class WebExceptionResponse {
    public int status;
    public String message;

    public WebExceptionResponse(Response.Status status, String message) {
        this.status = status.getStatusCode();
        this.message = message;
    }
}
