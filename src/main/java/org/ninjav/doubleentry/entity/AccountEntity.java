package org.ninjav.doubleentry.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by ninjav on 2016/05/09.
 */
@Entity
public class AccountEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min = 1, max = 100)
    private String clientReference;

    @NotNull
    @Column(unique=true)
    @Length(min = 1, max = 80)
    private String accountReference;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Length(min = 3, max = 3)
    private String currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public String getAccountReference() {
        return accountReference;
    }

    public void setAccountReference(String accountReference) {
        this.accountReference = accountReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
