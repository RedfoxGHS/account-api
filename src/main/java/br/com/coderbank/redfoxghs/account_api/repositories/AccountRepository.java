package br.com.coderbank.redfoxghs.account_api.repositories;

import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    boolean existsByIdClient(UUID idClient);
}
