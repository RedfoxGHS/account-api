package br.com.coderbank.redfoxghs.account_api.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

public record IncreaseAccountBalanceRequestDTO(
        @NotBlank(message = "Account ID is mandatory")
        @UUID(message = "Invalid UUID format")
        String idAccount,

        @NotNull(message = "Please provide a value increase; it is required.")
        @Positive(message = "The value increase must be greater than zero.")
        BigDecimal balance
) {
}
