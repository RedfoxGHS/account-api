package br.com.coderbank.redfoxghs.account_api.services;

import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.CustomDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.GeneralDatabaseException;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private UUID idClient;


    @BeforeEach
    public void setUp() {
        idClient = UUID.randomUUID();
    }

    @Test
    public void testCreate_AccountDoesNotExist() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setIdClient(idClient);

        when(accountRepository.existsByIdClient(idClient)).thenReturn(false);
        when(accountRepository.save(any())).thenReturn(accountEntity);

        AccountEntity account = accountService.create(idClient);

        verify(accountRepository).existsByIdClient(idClient);
        verify(accountRepository).save(any());

        assertEquals(idClient, account.getIdClient());
        assertEquals(1, account.getAgencyNumber());
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
        assertEquals(6, String.valueOf(account.getAccountNumber()).length());
    }

    @Test
    public void testCreate_AccountExists(){
        String expectedMessage = "Já existe uma conta com esse id de cliente";

        when(accountRepository.existsByIdClient(idClient)).thenReturn(true);

        CustomDatabaseException exception = assertThrows(CustomDatabaseException.class, () -> {
            accountService.create(idClient);
        });

        verify(accountRepository).existsByIdClient(idClient);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testCreate_DataIntegrityViolation(){
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
}
