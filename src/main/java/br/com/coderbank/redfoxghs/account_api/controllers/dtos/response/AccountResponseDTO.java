package br.com.coderbank.redfoxghs.account_api.controllers.dtos.response;

import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        UUID idClient,
        Integer agencyNumber,
        Integer accountNumber,
        BigDecimal balance
) {
    public AccountResponseDTO(AccountEntity accountEntity) {
        this(
                accountEntity.getIdAccount(),
                accountEntity.getIdClient(),
                accountEntity.getAgencyNumber(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance()
        );
    }
}
