package br.com.coderbank.redfoxghs.account_api.controllers.dtos;

import java.util.UUID;

public class NewAccountDTO {
    public UUID idClient;

    public UUID getIdClient() {
        return idClient;
    }

    public void setIdClient(UUID idClient) {
        this.idClient = idClient;
    }
}
