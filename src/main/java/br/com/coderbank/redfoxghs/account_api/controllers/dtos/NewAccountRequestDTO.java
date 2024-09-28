package br.com.coderbank.redfoxghs.account_api.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record NewAccountRequestDTO(

        @NotBlank(message = "Client ID is mandatory")
        @UUID(message = "Invalid UUID format")
        String idClient
) {
}
