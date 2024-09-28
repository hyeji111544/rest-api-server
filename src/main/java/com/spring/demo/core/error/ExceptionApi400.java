package com.spring.demo.core.error;

public class ExceptionApi400 extends RuntimeException{

    public ExceptionApi400(String message){
        super(message);
    }
}
