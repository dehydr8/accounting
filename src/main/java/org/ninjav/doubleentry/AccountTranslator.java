package org.ninjav.doubleentry;

import org.ninjav.doubleentry.entity.AccountEntity;
import org.ninjav.doubleentry.model.Account;

import java.util.Currency;

/**
 * Created by ninjav on 2016/05/09.
 */
public class AccountTranslator {
    public static Account from(AccountEntity e) {
        Account a = new Account(e.getAccountReference(), new Money(e.getAmount(), Currency.getInstance(e.getCurrency())));
        return a;
    }
}
