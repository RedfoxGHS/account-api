package br.com.coderbank.redfoxghs.account_api.controllers;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.request.AccountBalanceRequestDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountBalanceResponseDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.exception.generalExceptions.InsufficientBalanceException;
import br.com.coderbank.redfoxghs.account_api.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    private static final String PATH = "/api/v1/accounts";

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    private UUID idClient;

    @BeforeEach
    public void setUp() {
        idClient = UUID.randomUUID();
    }

    private String createAccountJson(UUID idClient) {
        return "{\"idClient\":\"" + idClient + "\"}";
    }

    @Test
    public void testCreateNewAccount_Success() throws Exception {
        AccountResponseDTO accountResponseExpectedDTO = new AccountResponseDTO(
                UUID.randomUUID(),
                idClient,
                1234,
                123456,
                BigDecimal.ZERO
        );

        when(accountService.create(idClient)).thenReturn(accountResponseExpectedDTO);

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson(idClient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idClient").value(idClient.toString()));

        verify(accountService).create(idClient);
    }

    @Test
    public void testCreateNewAccount_BadRequest_InvalidUuid() throws Exception {
        String invalidJson = "{ \"idClient\": \"invalid-uuid\" }";

        mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBalanceById_Success() throws Exception {
        AccountBalanceResponseDTO accountBalanceResponseDTOExpected = new AccountBalanceResponseDTO(
                BigDecimal.TEN
        );

        when(accountService.getBalanceAccount(idClient)).thenReturn(accountBalanceResponseDTOExpected);

        mockMvc.perform(get(PATH + "/" + idClient))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(accountBalanceResponseDTOExpected.balance()));
    }

    @Test
    public void testIncreaseAccountBalance_Success() throws Exception {
        AccountBalanceRequestDTO requestDTO = new AccountBalanceRequestDTO(
                idClient.toString(),
                BigDecimal.valueOf(100)
        );

        doNothing().when(accountService).increaseAccountBalance(requestDTO);

        mockMvc.perform(patch(PATH + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).increaseAccountBalance(requestDTO);
    }

    @Test
    public void testDecreaseAccountBalance_Success() throws Exception {
        AccountBalanceRequestDTO requestDTO = new AccountBalanceRequestDTO(
                idClient.toString(),
                BigDecimal.valueOf(50)
        );

        doNothing().when(accountService).decreaseAccountBalance(requestDTO);

        mockMvc.perform(patch(PATH + "/withdrawn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).decreaseAccountBalance(requestDTO);
    }

    @Test
    public void testIncreaseAccountBalance_BadRequest_InvalidUuid() throws Exception {
        String invalidUuidJson = "{ \"idAccount\": \"invalid-uuid\", \"balance\": 100 }";

        mockMvc.perform(patch(PATH + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUuidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIncreaseAccountBalance_BadRequest_NegativeBalance() throws Exception {
        AccountBalanceRequestDTO requestDTO = new AccountBalanceRequestDTO(
                idClient.toString(),
                BigDecimal.valueOf(-100)
        );

        mockMvc.perform(patch(PATH + "/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDecreaseAccountBalance_InsufficientBalance() throws Exception {
        UUID accountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("1000.00");

        AccountBalanceRequestDTO accountBalanceRequestDTO = new AccountBalanceRequestDTO(
                accountId.toString(),
                amount
        );

        doThrow(new InsufficientBalanceException("Saldo insuficiente"))
                .when(accountService).decreaseAccountBalance(accountBalanceRequestDTO);

        mockMvc.perform(patch("/api/v1/accounts/withdrawn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountBalanceRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Saldo Insuficiente"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.detail").value("Saldo insuficiente"));
    }
}

