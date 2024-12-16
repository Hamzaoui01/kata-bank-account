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
     * Perform an operation (debit/credit) on account
     *
     * @param id The id for account to debit
     * @param operationRequestDto  The operation's data to perform (Type,amount ...)
     * @return the created operation
     */
    @PostMapping(value = "/{id}/operations",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Perform operation",description = "Make a deposit or withdraw operation")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationDto performOperation(@PathVariable("id") Long id , @Valid @RequestBody OperationRequestDto operationRequestDto) {
        log.info("Operation {} requested for account {}", operationRequestDto.type(),id);
        return accountService.handleOperation(id,operationRequestDto.type(),operationRequestDto.amount());
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


