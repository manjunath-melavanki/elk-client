package com.elk.dto;

/**
 * ELK Response
 * @author  Manjunath Melavanki
 * date    17-11-2023
 */
public class ElkResponse<T> {
    private final int statusCode;
    private final T entity;
    private final String message;
    private final Exception exception;

    public ElkResponse(int statusCode, T entity, String message, Exception exception) {
        this.statusCode = statusCode;
        this.entity = entity;
        this.message = message;
        this.exception = exception;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getEntity() {
        return entity;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }


    @Override
    public String toString() {
        return "ElkResponse{" +
                "statusCode=" + statusCode +
                ", entity=" + entity +
                ", message='" + message + '\'' +
                ", exception=" + exception +
                '}';
    }

}
