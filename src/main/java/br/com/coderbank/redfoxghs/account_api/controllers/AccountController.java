package br.com.coderbank.redfoxghs.account_api.controllers;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.request.AccountRequestDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountBalanceResponseDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createNewAccount(@Valid @RequestBody AccountRequestDTO newAccount) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.create(UUID.fromString(newAccount.idClient())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountBalanceResponseDTO> getBalanceById(@Valid @PathVariable(value = "id") UUID idAccount) {
        return ResponseEntity.ok(accountService.getBalanceAccount(idAccount));
    }
}
