package com.tu.elk.exception;
/**
 * Exception class
 * @author  Manjunath Melavanki
 * date    17-11-2023
 */
public class ElkClientException extends Exception{

    public ElkClientException(String message) {
        super(message);
    }

    public ElkClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
