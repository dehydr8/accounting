package org.ninjav.doubleentry.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ninjav on 2016/05/09.
 */
@Entity
public class TransactionHistoryEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min = 1, max=100)
    private String clientReference;

    @NotNull
    @Length(min = 1, max=80)
    private String transactionReference;

    @NotNull
    @Length(min = 1, max=80)
    private String transactionType;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

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

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
