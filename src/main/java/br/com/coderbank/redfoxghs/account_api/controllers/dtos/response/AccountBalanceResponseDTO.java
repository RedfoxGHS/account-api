package br.com.coderbank.redfoxghs.account_api.controllers.dtos.response;

import java.math.BigDecimal;

public record AccountBalanceResponseDTO(
        BigDecimal balance
) {
}
