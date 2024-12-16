package com.bank.kata.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(Long id){
        super("Account not found by id "+id);
    }
}
