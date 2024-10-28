package br.com.coderbank.redfoxghs.account_api.controllers;

import br.com.coderbank.redfoxghs.account_api.controllers.dtos.request.AccountRequestDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.request.IncreaseAccountBalanceRequestDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountBalanceResponseDTO;
import br.com.coderbank.redfoxghs.account_api.controllers.dtos.response.AccountResponseDTO;
import br.com.coderbank.redfoxghs.account_api.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @Operation(summary = "Cria uma nova conta para o cliente",
            description = "Cria uma nova conta bancária associada ao cliente com base no ID fornecido.")
    public ResponseEntity<AccountResponseDTO> createNewAccount(@Valid @RequestBody AccountRequestDTO newAccount) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.create(UUID.fromString(newAccount.idClient())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta saldo da conta do cliente",
            description = "Retorna o saldo da conta do cliente com base no ID da conta fornecido.")
    public ResponseEntity<AccountBalanceResponseDTO> getBalanceById(@PathVariable(value = "id") UUID idAccount) {
        return ResponseEntity.ok(accountService.getBalanceAccount(idAccount));
    }

    @PatchMapping("/deposit")
    public ResponseEntity<Void> increaseAccountBalance(@Valid @RequestBody IncreaseAccountBalanceRequestDTO increaseAccountBalance) {
        accountService.increaseAccountBalance(increaseAccountBalance);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
