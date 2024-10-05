package br.com.coderbank.redfoxghs.account_api.services;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountBalanceResponseDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.ConflictDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.CustomDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.GeneralDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.NotFoundDatabaseException;
import br.com.coderbank.redfoxghs.account_api.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountResponseDTO create(UUID idClient) {
        if (accountRepository.existsByIdClient(idClient)) {
            throw new ConflictDatabaseException("Já existe uma conta para o cliente com id " + idClient);
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setIdClient(idClient);

        try {
            AccountEntity savedAccount = accountRepository.save(accountEntity);
            return new AccountResponseDTO(
                    savedAccount.getIdAccount(),
                    savedAccount.getIdClient(),
                    savedAccount.getAgencyNumber(),
                    savedAccount.getAccountNumber(),
                    savedAccount.getBalance()
            );
        } catch (DataIntegrityViolationException e) {
            throw new CustomDatabaseException("Erro de integridade de dados ao salvar a conta", e);
        } catch (Exception e) {
            throw new GeneralDatabaseException("Erro desconhecido ao salvar a conta", e);
        }
    }

    public AccountBalanceResponseDTO getBalanceAccount(UUID idAccount) {

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(idAccount);

        if (accountEntityOptional.isEmpty()) {
            throw new NotFoundDatabaseException("Não foi encontrada uma conta com esse id " + idAccount);
        }

        return new AccountBalanceResponseDTO(
                accountEntityOptional.get().getBalance()
        );
    }
}
