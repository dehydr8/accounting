package org.ninjav.pda.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by ninjav on 2016/05/09.
 */
@Entity
public class DistributionDestinationEntity {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min = 1, max = 80)
    private String accountReference;

    @NotNull
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "distributionId", nullable=false, updatable=false)
    private DistributionEntity distribution;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DistributionEntity getDistribution() {
        return distribution;
    }

    public void setDistribution(DistributionEntity distribution) {
        this.distribution = distribution;
    }
}
