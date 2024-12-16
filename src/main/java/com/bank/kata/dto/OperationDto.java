package com.bank.kata.dto;

import com.bank.kata.enums.OperationType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class OperationDto {
    private OperationType type;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDateTime dateTime;
}
