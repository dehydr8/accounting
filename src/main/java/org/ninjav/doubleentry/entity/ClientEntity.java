package org.ninjav.doubleentry.entity;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Columns;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ninjav on 2016/05/09.
 */
@Entity
public class ClientEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Length(min = 1, max = 100)
    private String reference;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
