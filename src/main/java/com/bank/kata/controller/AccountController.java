package com.bank.kata.controller;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.dto.OperationRequestDto;
import com.bank.kata.service.AccountService;
import com.bank.kata.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account",description = "Rest Service to manage account")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final OperationService operationService;


    /**
     *
     * Deposit money on an account
     *
     * @param id The id for account to debit
     * @param operationRequestDto : The amount to debit in the account
     */
    @PostMapping(value = "/{id}/debit",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Debit account",description = "Deposit money on the account")
    public void debitAccount(@PathVariable("id") Long id , @Valid @RequestBody OperationRequestDto operationRequestDto) {
        log.info("Received debit request for account {}: amount={}", id, operationRequestDto.getAmount());
        accountService.debit(id,operationRequestDto.getAmount());
    }

    /**
     *
     * Withdraw money from account
     *
     * @param id The id for account to credit
     * @param operationRequestDto The amount to withdraw from the account
     */
    @PostMapping(value = "/{id}/credit",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Credit account",description = "Withdraw money from the account")
    public void creditAccount(@PathVariable("id") Long id , @Valid @RequestBody OperationRequestDto operationRequestDto) {
        log.info("Received credit request for account {}: amount={}", id, operationRequestDto.getAmount());
        accountService.credit(id,operationRequestDto.getAmount());
    }


    /**
     *
     * Fetch a specific page of the history of operations for an account
     *
     * @param id   the id of the account
     * @param page the page number (optional, default to 0)
     * @param size the max number of operation to fetch in the page (optional, default to 10)
     * @return Page<OperationDto> and status OK (200)
     */
    @GetMapping("/{id}/operations")
    @ResponseStatus(HttpStatus.OK)
    public Page<OperationDto> getAccount(@PathVariable Long id,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Received get operations request for account {}", id);
        return operationService.getAccountOperations(id, PageRequest.of(page, size));
    }

}


