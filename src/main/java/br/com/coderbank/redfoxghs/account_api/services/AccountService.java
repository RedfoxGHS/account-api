package br.com.coderbank.redfoxghs.account_api.services;

import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.CustomDatabaseException;
import br.com.coderbank.redfoxghs.account_api.exception.dbexceptions.GeneralDatabaseException;
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

    public AccountEntity create(UUID idClient) {
        if (accountRepository.existsByIdClient(idClient)) {
            throw new CustomDatabaseException("Já existe uma conta com esse id de cliente");
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setIdClient(idClient);

        try {
            return accountRepository.save(accountEntity);
        } catch (DataIntegrityViolationException e) {
            throw new CustomDatabaseException("Erro de integridade de dados ao salvar a conta", e);
        } catch (Exception e) {
            throw new GeneralDatabaseException("Erro desconhecido ao salvar a conta", e);
        }
    }
}
