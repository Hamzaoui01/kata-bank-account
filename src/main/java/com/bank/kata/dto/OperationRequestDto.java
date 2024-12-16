package com.bank.kata.dto;

import com.bank.kata.enums.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Schema
@Builder
public record OperationRequestDto (
    @Schema
    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal amount,
    @Schema
    @NotNull
    OperationType type){
}
