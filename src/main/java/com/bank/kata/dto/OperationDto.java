package com.bank.kata.dto;

import com.bank.kata.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationDto(
     Long id,
     OperationType type,
     BigDecimal amount,
     BigDecimal balance,
     LocalDateTime dateTime
){}
