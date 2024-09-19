package br.com.coderbank.redfoxghs.account_api.controllers.vos;

import java.util.UUID;

public class NewAccountVO {
    public UUID idClient;

    public UUID getIdClient() {
        return idClient;
    }

    public void setIdClient(UUID idClient) {
        this.idClient = idClient;
    }
}
