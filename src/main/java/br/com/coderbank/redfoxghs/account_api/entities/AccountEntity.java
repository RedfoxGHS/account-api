package br.com.coderbank.redfoxghs.account_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idAccount;

    @Column(nullable = false, unique = true)
    private UUID idClient;

    @Column(nullable = false)
    private Integer agencyNumber;

    @Column(nullable = false, length = 6, unique = true)
    @Min(100000)
    @Max(999999)
    private Integer accountNumber;

    @Column
    private BigDecimal balance;

    public AccountEntity() {
        this.balance = BigDecimal.ZERO;
        agencyNumber = 1;
        accountNumber = 100000 + new Random().nextInt(900000);
    }

    public UUID getIdAccount() {
        return idAccount;
    }

    public UUID getIdClient() {
        return idClient;
    }

    public void setIdClient(UUID idClient) {
        this.idClient = idClient;
    }

    public Integer getAgencyNumber() {
        return agencyNumber;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void addBalance(BigDecimal balance) {
        this.balance = this.balance.add(balance);
    }

    public void subtractBalance(BigDecimal balance) {
        this.balance = this.balance.subtract(balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(idAccount, that.idAccount) && Objects.equals(idClient, that.idClient) && Objects.equals(agencyNumber, that.agencyNumber) && Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount, idClient, agencyNumber, accountNumber);
    }
}
