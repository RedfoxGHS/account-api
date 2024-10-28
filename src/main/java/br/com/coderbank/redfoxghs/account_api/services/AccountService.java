package br.com.coderbank.redfoxghs.account_api.services;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.request.AccountBalanceRequestDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountBalanceResponseDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import br.com.coderbank.redfoxghs.account_api.exception.generalExceptions.InsufficientBalanceException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.ConflictDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.CustomDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.NotFoundDatabaseException;
import br.com.coderbank.redfoxghs.account_api.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountResponseDTO create(UUID idClient) {
        if (accountRepository.existsByIdClient(idClient)) {
            throw new ConflictDatabaseException("Já existe uma conta para o cliente com id " + idClient);
        }

        AccountEntity accountEntity = new AccountEntity(idClient);

        try {
            AccountEntity savedAccount = accountRepository.save(accountEntity);
            return new AccountResponseDTO(savedAccount);
        } catch (DataIntegrityViolationException e) {
            throw new CustomDatabaseException("Erro de integridade de dados ao salvar a conta", e);
        }
    }

    public AccountBalanceResponseDTO getBalanceAccount(UUID idAccount) {
        AccountEntity accountEntity = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NotFoundDatabaseException("Não foi encontrada uma conta com esse id: " + idAccount));

        return new AccountBalanceResponseDTO(accountEntity.getBalance());
    }

    public void increaseAccountBalance(AccountBalanceRequestDTO increaseAccountBalance) {
        UUID accountId = UUID.fromString(increaseAccountBalance.idAccount());
        BigDecimal amountToAdd = validateAndGetAmount(increaseAccountBalance);

        AccountEntity accountEntity = this.findAccount(accountId);

        accountEntity.addBalance(amountToAdd);

        accountRepository.save(accountEntity);
    }

    public void decreaseAccountBalance(AccountBalanceRequestDTO decreaseAccountBalance) {
        UUID accountId = UUID.fromString(decreaseAccountBalance.idAccount());
        BigDecimal amountToSubtract = validateAndGetAmount(decreaseAccountBalance);

        AccountEntity accountEntity = this.findAccount(accountId);

        validateSufficientBalance(accountEntity, amountToSubtract);

        accountEntity.subtractBalance(amountToSubtract);

        accountRepository.save(accountEntity);
    }

    private AccountEntity findAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundDatabaseException("Não foi encontrada uma conta com esse id: " + accountId));
    }

    private BigDecimal validateAndGetAmount(AccountBalanceRequestDTO request) {
        BigDecimal amount = request.balance();
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("O valor a ser adicionado deve ser maior que zero.");
        }
        return amount;
    }
    private void validateSufficientBalance(AccountEntity accountEntity, BigDecimal amountToSubtract) {
        if (accountEntity.getBalance().compareTo(amountToSubtract) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente. Saldo atual: " + accountEntity.getBalance());
        }
    }
}
