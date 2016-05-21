package org.ninjav.doubleentry.dao;

import org.ninjav.doubleentry.AccountTranslator;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.TransactionLeg;
import org.ninjav.doubleentry.entity.AccountEntity;
import org.ninjav.doubleentry.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Created by ninjav on 2016/05/09.
 */
public class JpaAccountDao implements AccountDao {
    private String clientRef;

    EntityManager em;

    public JpaAccountDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean accountExists(String accountRef) {
        TypedQuery<AccountEntity> q = em.createQuery(
                "select a from AccountEntity a where a.accountReference = :accountReference", AccountEntity.class);
        q.setParameter("accountReference", accountRef);
        return q.getResultList().size() == 1;
    }

    @Override
    public void createAccount(String clientRef, String accountRef, Money initialAmount) {
        AccountEntity a = new AccountEntity();
        a.setClientReference(clientRef);
        a.setAccountReference(accountRef);
        a.setAmount(initialAmount.getAmount());
        a.setCurrency(initialAmount.getCurrency().getCurrencyCode());
        em.persist(a);
    }

    @Override
    public Account getAccount(String accountRef) {
        TypedQuery<AccountEntity> q = em.createQuery(
                "select a from AccountEntity a where a.accountReference = :accountReference", AccountEntity.class);
        q.setParameter("accountReference", accountRef);
        return AccountTranslator.from(q.getSingleResult());
    }

    @Override
    public void truncateTables() {

    }

    @Override
    public void updateBalance(TransactionLeg leg) {

    }

    @Override
    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    @Override
    public String getClientRef() {
        return clientRef;
    }
}
