package com.bank.kata.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OperationRequestDto {
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal amount;
}
