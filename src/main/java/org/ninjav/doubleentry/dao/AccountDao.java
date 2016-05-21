package org.ninjav.doubleentry.dao;

import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.TransactionLeg;
import org.ninjav.doubleentry.model.Account;

public interface AccountDao {

    boolean accountExists(String accountRef);

    void createAccount(String clientRef, String accountRef, Money initialAmount);

    Account getAccount(String accountRef);

    void truncateTables();

    void updateBalance(TransactionLeg leg);

    void setClientRef(String clientRef);

    String getClientRef();
}
