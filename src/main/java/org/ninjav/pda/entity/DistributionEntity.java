package org.ninjav.pda.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by ninjav on 2016/05/09.
 */
@Entity
public class DistributionEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min = 1, max = 80)
    private String transactionReference;

    @NotNull
    @Length(min = 1, max = 80)
    private String transactionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
