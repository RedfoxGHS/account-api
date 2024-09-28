package br.com.coderbank.redfoxghs.account_api.controllers.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        UUID idClient,
        Integer agencyNumber,
        Integer accountNumber,
        BigDecimal balance
) {
}
