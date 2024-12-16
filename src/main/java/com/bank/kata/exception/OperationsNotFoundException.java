package com.bank.kata.exception;

public class OperationsNotFoundException extends RuntimeException{
    public OperationsNotFoundException(){
        super("No Operations found for requested account");
    }
}
