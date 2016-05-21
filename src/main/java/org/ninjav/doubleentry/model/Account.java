package org.ninjav.doubleentry.model;

import java.math.BigDecimal;
import java.util.Currency;

import org.ninjav.doubleentry.Money;

/**
 * Immutable value object representing an account
 */
public class Account {

    private final String accountRef;
    private final Money balance;
    private final static Account NULL_ACCOUNT = new Account("", new Money(new BigDecimal("0.00"), Currency.getInstance("XXX")));

    public static Account nullAccount() {
        return NULL_ACCOUNT;
    }

    public Account(String accountRef, Money balance) {
        if (accountRef == null) {
            throw new NullPointerException("Argument accountRef is NULL");
        }
        if (balance == null) {
            throw new NullPointerException("Argument balance is NULL");
        }
        this.accountRef = accountRef;
        this.balance = balance;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public Money getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return balance.getCurrency();
    }

    public boolean isOverdrawn() {
        return balance.getAmount().doubleValue() < 0.0;
    }

    public boolean isNullAccount() {
        return this.equals(NULL_ACCOUNT);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Account)) {
            return false;
        }
        Account account = (Account) other;
        if (!accountRef.equals(account.accountRef)) {
            return false;
        }
        if (!balance.equals(account.balance)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = accountRef.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AccountEntity{"
                + ", accountRef='" + accountRef + '\''
                + ", balance=" + balance
                + '}';
    }
}
