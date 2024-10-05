package br.com.coderbank.redfoxghs.account_api.services;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountBalanceResponseDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.CustomDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.GeneralDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.NotFoundDatabaseException;
import br.com.coderbank.redfoxghs.account_api.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private UUID idClient;
    private UUID idAccount;


    @BeforeEach
    public void setUp() {
        idClient = UUID.randomUUID();
        idAccount = UUID.randomUUID();
    }

    @Test
    public void testCreate_AccountDoesNotExist() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setIdClient(idClient);

        when(accountRepository.existsByIdClient(idClient)).thenReturn(false);
        when(accountRepository.save(any())).thenReturn(accountEntity);

        AccountResponseDTO account = accountService.create(idClient);

        verify(accountRepository).existsByIdClient(idClient);
        verify(accountRepository).save(any());

        assertEquals(idClient, account.idClient());
        assertEquals(1, account.agencyNumber());
        assertEquals(0, account.balance().compareTo(BigDecimal.ZERO));
        assertEquals(6, String.valueOf(account.accountNumber()).length());
    }

    @Test
    public void testCreate_AccountExists() {
        String expectedMessage = "Já existe uma conta para o cliente com id " + idClient;

        when(accountRepository.existsByIdClient(idClient)).thenReturn(true);

        CustomDatabaseException exception = assertThrows(CustomDatabaseException.class, () -> {
            accountService.create(idClient);
        });

        verify(accountRepository).existsByIdClient(idClient);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testCreate_DataIntegrityViolation() {
        String expectedMessage = "Erro de integridade de dados ao salvar a conta";

        when(accountRepository.existsByIdClient(idClient)).thenReturn(false);
        when(accountRepository.save(any(AccountEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Erro de integridade"));

        CustomDatabaseException exception = assertThrows(CustomDatabaseException.class, () -> {
            accountService.create(idClient);
        });

        verify(accountRepository).existsByIdClient(idClient);
        verify(accountRepository).save(any(AccountEntity.class));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testCreate_UnknownError() {
        String expectedMessage = "Erro desconhecido ao salvar a conta";

        when(accountRepository.existsByIdClient(idClient)).thenReturn(false);
        when(accountRepository.save(any(AccountEntity.class)))
                .thenThrow(new RuntimeException("Erro desconhecido"));

        GeneralDatabaseException exception = assertThrows(GeneralDatabaseException.class, () -> {
            accountService.create(idClient);
        });

        verify(accountRepository).existsByIdClient(idClient);
        verify(accountRepository).save(any(AccountEntity.class));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testGetBalanceAccount_AccountNotFound() {
        String expectedMessage = "Não foi encontrada uma conta com esse id " + idAccount;

        when(accountRepository.findById(idAccount)).thenReturn(Optional.empty());

        NotFoundDatabaseException exception = assertThrows(NotFoundDatabaseException.class, () -> {
            accountService.getBalanceAccount(idAccount);
        });

        verify(accountRepository).findById(idAccount);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testGetBalanceAccount_AccountFound() {
        AccountBalanceResponseDTO accountBalanceResponseDTO = new AccountBalanceResponseDTO(BigDecimal.TEN);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.addBalance(BigDecimal.TEN);

        when(accountRepository.findById(idAccount)).thenReturn(Optional.of(accountEntity));

        AccountBalanceResponseDTO account = accountService.getBalanceAccount(idAccount);

        verify(accountRepository).findById(idAccount);

        assertEquals(0, accountBalanceResponseDTO.balance().compareTo(account.balance()));
    }
}
