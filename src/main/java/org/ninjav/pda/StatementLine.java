package org.ninjav.pda;

import org.ninjav.doubleentry.Money;

import java.util.Date;

/**
 * Created by ninjav on 2016/05/07.
 */
public class StatementLine {
    public Date date;
    public String reference;
    public Money amount;

    public StatementLine(Date date, String reference, Money amount) {
        this.date = date;
        this.reference = reference;
        this.amount = amount;
    }
}
