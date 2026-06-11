package com.lms.backend.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(){
    }

    public DataNotFoundException(String message){
        super(message = "Data Not Found");
    }

}
