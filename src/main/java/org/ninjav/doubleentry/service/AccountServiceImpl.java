package org.ninjav.doubleentry.service;

import org.ninjav.doubleentry.AccountNotFoundException;
import org.ninjav.doubleentry.AccountService;
import org.ninjav.doubleentry.InfrastructureException;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.dao.AccountDao;
import org.ninjav.doubleentry.model.Account;

/**
 * Implements the methods of the account service.
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    @Override
    public void createAccount(String accountRef, Money amount) throws InfrastructureException {
        if (accountDao.accountExists(accountRef)) {
            throw new InfrastructureException("AccountEntity already exists: " + accountRef);
        }
        accountDao.createAccount(accountDao.getClientRef(), accountRef, amount);
    }

    @Override
    public Money getAccountBalance(String accountRef) throws AccountNotFoundException {
        Account account = accountDao.getAccount(accountRef);
        if (account.isNullAccount()) {
            throw new AccountNotFoundException(accountRef);
        }
        return account.getBalance();
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

}
