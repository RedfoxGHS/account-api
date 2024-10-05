package br.com.coderbank.redfoxghs.account_api.controllers;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.NewAccountRequestDTO;
import br.com.coderbank.redfoxghs.account_api.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createNewAccount(@Valid @RequestBody NewAccountRequestDTO newAccount) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.create(UUID.fromString(newAccount.idClient())));
    }
}
