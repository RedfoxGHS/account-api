package br.com.coderbank.redfoxghs.account_api.services;

import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import br.com.coderbank.redfoxghs.account_api.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<AccountEntity> createNewAccount(UUID idClient) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setIdClient(idClient);

        try {
            return Optional.of(accountRepository.save(accountEntity));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
