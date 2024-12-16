package com.bank.kata.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    Long id;
    BigDecimal balance;
}
