package com.course2.apisecurity.exception.handler;

public class ApiErrorMessage {

    private String errorMessage;

    public ApiErrorMessage (String errorMessage){
        super();
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
