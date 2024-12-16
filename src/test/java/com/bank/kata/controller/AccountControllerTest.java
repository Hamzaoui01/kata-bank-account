package com.bank.kata.controller;

import com.bank.kata.dto.OperationDto;
import com.bank.kata.dto.OperationRequestDto;
import com.bank.kata.enums.OperationType;
import com.bank.kata.exception.AccountNotFoundException;
import com.bank.kata.exception.InsufficientBalanceException;
import com.bank.kata.exception.OperationsNotFoundException;
import com.bank.kata.service.AccountService;
import com.bank.kata.service.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static com.bank.kata.enums.OperationType.CREDIT;
import static com.bank.kata.enums.OperationType.DEBIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
    
    private final static String API_URL_OPERATIONS = "/api/v1/accounts/{id}/operations";
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSucceedDebit_WhenAmountIsValid() throws Exception {
        OperationRequestDto requestDto = buildRequestDto(DEBIT,BigDecimal.valueOf(200));
        mvc.perform(post(API_URL_OPERATIONS,1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldSucceedCredit_WhenAmountIsValid() throws Exception {
        OperationRequestDto requestDto = buildRequestDto(CREDIT,BigDecimal.valueOf(200));
        mvc.perform(post(API_URL_OPERATIONS,1L)
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldFailDebit_whenAmountIsNegative() throws Exception {
        String errorMsg = "amount=must be greater than or equal to 0.0";
        OperationRequestDto requestDto = buildRequestDto(DEBIT,BigDecimal.valueOf(-1));
        MvcResult result = mvc.perform(post(API_URL_OPERATIONS, 1L)
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(errorMsg);
    }

    @Test
    void shouldFailCredit_whenAmountIsNegative() throws Exception {
        String errorMsg = "amount=must be greater than or equal to 0.0";
        OperationRequestDto requestDto = buildRequestDto(CREDIT,BigDecimal.valueOf(-1));
        MvcResult result = mvc.perform(post(API_URL_OPERATIONS, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                ).andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(errorMsg);

    }

    @Test
    void shouldFailDebit_whenAmountIsNull() throws Exception {
        String errorMsg = "amount=must not be null";
        OperationRequestDto requestDto = buildRequestDto(DEBIT,null);
        MvcResult result = mvc.perform(post(API_URL_OPERATIONS, 1L)
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(errorMsg);

    }

    @Test
    void shouldFailCredit_whenAmountIsNull() throws Exception {
        String errorMsg = "amount=must not be null";
        OperationRequestDto requestDto = buildRequestDto(CREDIT,null);
        MvcResult result = mvc.perform(post(API_URL_OPERATIONS, 1L)
                .contentType(APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(status().isBadRequest()).andReturn();

        assertThat(result.getResponse().getContentAsString()).contains(errorMsg);

    }

    @Test
    void shouldFailCredit_whenTypeIsNull() throws Exception {
        String errorMsg = "type=must not be null";
        String content = """
                        {
                            "type":null,
                            "amount": 20
                        }
                        """;
        MvcResult result = mvc.perform(post(API_URL_OPERATIONS, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                ).andExpect(status().isBadRequest())
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(errorMsg);
    }


    @Test
    void shouldFailCredit_whenTypeIsNotCorrectEnumValue() throws Exception {
        String errorMsg = "not one of the values accepted for Enum class: [CREDIT, DEBIT]";
        String content = """
                {
                    "type":"OK",
                    "amount": 20
                }
                """;
        MvcResult result = mvc.perform(
                        post(API_URL_OPERATIONS, 1L)
                                .contentType(APPLICATION_JSON)
                                .content(content))
                .andExpect(status().isBadRequest()).andReturn();
        assertThat(result.getResponse().getContentAsString()).contains(errorMsg);

    }

    @Test
    void shouldFailCreditWithNotFound_whenAccountNotFound() throws Exception  {
        doThrow(AccountNotFoundException.class).when(accountService).handleOperation(anyLong(), any(),any());
        OperationRequestDto requestDto = buildRequestDto(CREDIT,BigDecimal.ONE);
        mvc.perform(post(API_URL_OPERATIONS,1L)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldFailCredit_whenInsufficientBalance() throws Exception  {
        doThrow(InsufficientBalanceException.class).when(accountService).handleOperation(anyLong(), any(),any());
        OperationRequestDto requestDto = buildRequestDto(CREDIT,BigDecimal.ONE);
        MvcResult result = mvc.perform(post(API_URL_OPERATIONS, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }

    @Test
    void shouldSuccess_whenRetrieveOperations() throws Exception{
        PageImpl<OperationDto> page = new PageImpl<>(List.of(new OperationDto(1L, DEBIT, BigDecimal.TEN, BigDecimal.TEN, null)));
        when(operationService.getAccountOperations(any(),any())).thenReturn(page);
        mvc.perform(get(API_URL_OPERATIONS,1L))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString())
        ;
    }

    @Test
    void shouldFail_withNotFound_whenNoOperationFound() throws Exception{
        doThrow(OperationsNotFoundException.class).when(operationService).getAccountOperations(anyLong(), any());
        mvc.perform(get(API_URL_OPERATIONS,1L))
                .andExpect(status().isNotFound());
    }

    private static OperationRequestDto buildRequestDto(OperationType type, BigDecimal amount) {
        return OperationRequestDto.builder()
                .type(type)
                .amount(amount)
                .build();
    }

}
