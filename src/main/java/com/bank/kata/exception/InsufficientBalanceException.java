package com.bank.kata.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(){
        super("Balance less than requested amount");
    }
}
