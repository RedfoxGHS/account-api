package br.com.coderbank.redfoxghs.account_api.controllers;

import br.com.coderbank.redfoxghs.account_api.controllers.vos.NewAccountVO;
import br.com.coderbank.redfoxghs.account_api.entities.AccountEntity;
import br.com.coderbank.redfoxghs.account_api.services.AccountService;
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
    public ResponseEntity<AccountEntity> createNewAccount(@RequestBody NewAccountVO newAccount) {
        var account = accountService.createNewAccount(newAccount.getIdClient());
        return account
                .map(accountEntity -> ResponseEntity.status(HttpStatus.CREATED).body(accountEntity))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

}
