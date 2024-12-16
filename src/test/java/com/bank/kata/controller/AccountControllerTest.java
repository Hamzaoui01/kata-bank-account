package com.bank.kata.controller;

import com.bank.kata.dto.OperationRequestDto;
import com.bank.kata.exception.AccountNotFoundException;
import com.bank.kata.exception.InsufficientBalanceException;
import com.bank.kata.exception.OperationsNotFoundException;
import com.bank.kata.service.AccountService;
import com.bank.kata.service.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private OperationService operationService;

    private final static String API_URL_DEBIT = "/api/v1/accounts/{id}/debit";
    private final static String API_URL_CREDIT = "/api/v1/accounts/{id}/credit";
    private final static String API_URL_OPERATIONS = "/api/v1/accounts/{id}/operations";
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSucceedDebit_WhenAmountIsValid() throws Exception {
        OperationRequestDto requestDto = buildRequestDtoWithAmount(BigDecimal.valueOf(200));
        mvc.perform(post(API_URL_DEBIT,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(status().isNoContent());
    }

    @Test
    void shouldSucceedCredit_WhenAmountIsValid() throws Exception {
        OperationRequestDto requestDto = buildRequestDtoWithAmount(BigDecimal.valueOf(200));
        mvc.perform(post(API_URL_CREDIT,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isNoContent());
    }

    @Test
    void shouldFailDebit_whenAmountIsNegative() throws Exception {
        OperationRequestDto requestDto = buildRequestDtoWithAmount(BigDecimal.valueOf(-1));
        mvc.perform(post(API_URL_DEBIT,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCredit_whenAmountIsNegative() throws Exception {
        OperationRequestDto requestDto = buildRequestDtoWithAmount(BigDecimal.valueOf(-1));
        mvc.perform(post(API_URL_CREDIT,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailDebit_whenAmountIsNull() throws Exception {
        OperationRequestDto requestDto = buildRequestDtoWithAmount(null);
        mvc.perform(post(API_URL_DEBIT,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCredit_whenAmountIsNull() throws Exception {
        OperationRequestDto requestDto = buildRequestDtoWithAmount(null);
        mvc.perform(post(API_URL_CREDIT,1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCreditWithNotFound_whenAccountNotFound() throws Exception  {
        doThrow(AccountNotFoundException.class).when(accountService).credit(anyLong(), any());
        OperationRequestDto requestDto = buildRequestDtoWithAmount(BigDecimal.ONE);
        mvc.perform(post(API_URL_CREDIT,1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailDebitWithNotFound_whenAccountNotFound() throws Exception  {
        doThrow(AccountNotFoundException.class).when(accountService).debit(anyLong(), any());
        OperationRequestDto requestDto = buildRequestDtoWithAmount(BigDecimal.ONE);
        mvc.perform(post(API_URL_DEBIT,1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailCredit_whenInsufficientBalance() throws Exception  {
        doThrow(InsufficientBalanceException.class).when(accountService).credit(anyLong(), any());
        OperationRequestDto requestDto = buildRequestDtoWithAmount(BigDecimal.ONE);
        mvc.perform(post(API_URL_CREDIT,1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldSuccess_whenRetrieveOperations() throws Exception{
        mvc.perform(get(API_URL_OPERATIONS,1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFail_withNotFound_whenNoOperationFound() throws Exception{
        doThrow(OperationsNotFoundException.class).when(operationService).getAccountOperations(anyLong(), any());
        mvc.perform(get(API_URL_OPERATIONS,1L))
                .andExpect(status().isNotFound());
    }

    private static OperationRequestDto buildRequestDtoWithAmount(BigDecimal amount) {
        return OperationRequestDto.builder()
                .amount(amount)
                .build();
    }

}
