package org.ninjav.doubleentry.dao;

import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.Transaction;
import org.ninjav.doubleentry.TransactionLeg;
import org.ninjav.doubleentry.entity.TransactionHistoryEntity;
import org.ninjav.doubleentry.entity.TransactionLegEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by ninjav on 2016/05/22.
 */
public class JpaTransactionDao  implements TransactionDao {

    private EntityManager em;

    private String clientRef;

    public JpaTransactionDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public void storeTransaction(Transaction transaction) {
        TransactionHistoryEntity t = new TransactionHistoryEntity();
        t.setClientReference(clientRef);
        t.setTransactionReference(transaction.getTransactionRef());
        t.setTransactionType(transaction.getTransactionType());
        t.setTransactionDate(transaction.getTransactionDate());

        storeTransactionLegs(transaction.getLegs(), clientRef, transaction.getTransactionRef());

        em.persist(t);
    }

    private void storeTransactionLegs(List<TransactionLeg> legs, String clientRef, String transactionRef) {
        for (TransactionLeg l : legs) {
            TransactionLegEntity leg = new TransactionLegEntity();
            leg.setClientReference(clientRef);
            leg.setAccountReference(l.getAccountRef());
            leg.setTransactionReference(transactionRef);
            leg.setAmount(l.getAmount().getAmount());
            leg.setCurrency(l.getAmount().getCurrency().getCurrencyCode());
            em.persist(l);
        }
    }

    @Override
    public Set<String> getTransactionRefsForAccount(String accountRef) {
        List<String> results = em.createQuery(
                "select t.transactionReference from TransactionHistoryEntity t where t.accountReference = :accountReference", String.class)
                .setParameter("accountReference", accountRef)
                .getResultList();
        return new HashSet<>(results);
    }

    @Override
    public List<Transaction> getTransactions(List<String> transactionRefs) {
        List<TransactionHistoryEntity> transactions = em.createQuery(
                "select t from TransactionHistoryEntity t where t.transactionReference in :transactionReferences", TransactionHistoryEntity.class)
                .setParameter("transactionReferences", transactionRefs)
                .getResultList();
        List<Transaction> results = new ArrayList<>();
        for (TransactionHistoryEntity h : transactions) {

            Transaction t = new Transaction(
                    h.getTransactionReference(),
                    h.getTransactionType(),
                    h.getTransactionDate(),
                    getTransactionLegs(h.getTransactionReference()));

            results.add(t);
        }
        return results;
    }

    private List<TransactionLeg> getTransactionLegs(CharSequence transactionReference) {
        List<TransactionLegEntity> transactionLegs = em.createQuery(
                "select l from TransactionLegEntity l where l.transactionReference = :transactionReference", TransactionLegEntity.class)
                .setParameter("transactionReference", transactionReference)
                .getResultList();
        List<TransactionLeg> results = new ArrayList<>();
        for (TransactionLegEntity leg : transactionLegs) {
            TransactionLeg l = new TransactionLeg(
                    leg.getAccountReference(),
                    new Money(leg.getAmount(), Currency.getInstance(leg.getCurrency())));
            results.add(l);
        }
        return results;
    }

    @Override
    public Transaction getTransactionByRef(String transactionRef) {
        TransactionHistoryEntity t = em.createQuery(
                "select t from TransactionHistoryEntity t where t.transactionReference = :transactionReference", TransactionHistoryEntity.class)
                .setParameter("transactionReference", transactionRef)
                .getSingleResult();

        Transaction result = new Transaction(
                t.getTransactionReference(),
                t.getTransactionType(),
                t.getTransactionDate(),
                getTransactionLegs(t.getTransactionReference()));

        return result;
    }

    @Override
    public void truncateTables() {

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
