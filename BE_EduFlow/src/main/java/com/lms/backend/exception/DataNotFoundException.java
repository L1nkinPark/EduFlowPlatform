package com.lms.backend.exception;

public class DataNotFoundException extends ResourceNotFoundException {
    public DataNotFoundException(){
        super("Data not found");
    }

    public DataNotFoundException(String message){
        super(message);
    }
}
