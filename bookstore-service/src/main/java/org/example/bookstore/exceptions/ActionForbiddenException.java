package org.example.bookstore.exceptions;

public class ActionForbiddenException extends RuntimeException {

    public ActionForbiddenException(String message) {
        super(message);
    }

}
