package com.distrupify.exceptions;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class WebException {
    public static class BadRequest extends RuntimeException {
        public BadRequest(String message) {
            throw new BadRequestException(buildResponse(Status.BAD_REQUEST, message));
        }
    }

    public static class InternalServerError extends RuntimeException {
        public InternalServerError(String message) {
            throw new InternalServerErrorException(buildResponse(Status.INTERNAL_SERVER_ERROR, message));
        }
    }

    private static Response buildResponse(Status status, String message) {
        return Response.status(status).entity(new WebExceptionResponse(status, message)).build();
    }
}
