package org.ninjav.doubleentry.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.entity.AccountEntity;
import org.ninjav.doubleentry.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/09.
 */
public class JpaAccountEntityDaoTest {

    private EntityManager em;
    private String accountRef01;
    private String customerRef01;
    private AccountDao sut;

    @Before
    public void setup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-test");
        em = emf.createEntityManager();
        em.getTransaction().begin();

        sut = new JpaAccountDao(em);

        accountRef01 = "ACCNT00001";
        customerRef01 = "CLI00001";
    }

    @After
    public void teardown() {
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testAccountExists() throws Exception {
        insertTestAccount(accountRef01);

        boolean exists = sut.accountExists(accountRef01);

        assertTrue(exists);
    }

    @Test
    public void testCreateAccount() throws Exception {

        sut.createAccount(customerRef01, accountRef01, Money.toMoney("100.00", "ZAR"));

        assertAccountExists(accountRef01);
    }

    @Test
    public void testGetAccount() throws Exception {
        insertTestAccount(accountRef01);

        Account a = sut.getAccount(accountRef01);

        assertThat(a, is(notNullValue()));
    }

    @Test
    public void testUpdateBalance() throws Exception {
    }

    private void insertTestAccount(String accountReference) {
        AccountEntity a = new AccountEntity();
        a.setClientReference(customerRef01);
        a.setAccountReference(accountReference);
        Money m = Money.toMoney("100.00", "ZAR");
        a.setAmount(m.getAmount());
        a.setCurrency(m.getCurrency().getCurrencyCode());
        em.persist(a);
        em.flush();
    }

    private void assertAccountExists(CharSequence accountReference) {
        TypedQuery<AccountEntity> query = em.createQuery(
                "select a from AccountEntity a where a.accountReference = :accountReference", AccountEntity.class);
        query.setParameter("accountReference", accountReference);
        AccountEntity newAccountEntity = query.getSingleResult();
        assertThat(newAccountEntity, is(notNullValue()));
    }
}