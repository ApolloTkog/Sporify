package com.myapp.sporify_backend.payload.response;

/**
 * Μια custom κλάση για να επιστρέφει ένα μήνυμα ως response
 */
public class MessageResponse {
    private String message;
    private Object object;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }
}

