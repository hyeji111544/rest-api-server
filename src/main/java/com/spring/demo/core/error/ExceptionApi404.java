package com.spring.demo.core.error;

public class ExceptionApi404 extends RuntimeException{

    public ExceptionApi404(String message){
        super(message);
    }
}
