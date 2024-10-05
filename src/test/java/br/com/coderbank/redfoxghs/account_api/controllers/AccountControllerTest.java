package br.com.coderbank.redfoxghs.account_api.controllers;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private UUID idClient;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        idClient = UUID.randomUUID();
    }

    private String createAccountJson(UUID idClient) {
        return "{\"idClient\":\"" + idClient + "\"}";
    }

    @Test
    public void testCreateNewAccount_Success() throws Exception {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(
                UUID.randomUUID(),
                idClient,
                1234,
                123456,
                BigDecimal.ZERO
        );

        when(accountService.create(idClient)).thenReturn(accountResponseDTO);

        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createAccountJson(idClient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idClient").value(idClient.toString()));

        verify(accountService).create(idClient);
    }

    @Test
    public void testCreateNewAccount_BadRequest_InvalidUuid() throws Exception {
        String invalidJson = "{ \"idClient\": \"invalid-uuid\" }";

        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}

