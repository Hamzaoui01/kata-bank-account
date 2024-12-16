package com.bank.kata.exception;

public class InvalidAmountException extends IllegalArgumentException{
    public InvalidAmountException(){
        super("Amount Invalid: amount must not be null or less than 0");
    }
}
