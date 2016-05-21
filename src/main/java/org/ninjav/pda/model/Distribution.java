package org.ninjav.pda.model;

import org.ninjav.doubleentry.Money;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ninjav on 2016/05/07.
 */
public class Distribution {
    public String transactionReference;
    public String transactionType;
    public Map<String, Money> sourceAccounts = new HashMap<>();
    public Map<String, Money> destinationAccounts = new HashMap<>();

    public Distribution() {
    }

    public Distribution(String transactionReference, String transactionType, Map<String, Money> sourceAccounts, Map<String, Money> destinationAccounts) {
        this.transactionReference = transactionReference;
        this.transactionType = transactionType;
        this.sourceAccounts = sourceAccounts;
        this.destinationAccounts = destinationAccounts;
    }
}
