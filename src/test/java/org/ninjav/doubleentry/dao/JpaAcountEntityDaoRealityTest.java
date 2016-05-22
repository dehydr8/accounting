package org.ninjav.doubleentry.dao;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.Money;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by ninjav on 2016/05/22.
 */
public class JpaAcountEntityDaoRealityTest {

    private EntityManager em;
    private AccountDao sut;

    @Before
    public void setup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence");
        em = emf.createEntityManager();
        em.getTransaction().begin();

        sut = new JpaAccountDao(em);
    }

    @After
    public void teardown() {
        em.getTransaction().commit();
        em.close();
    }

    @Test
    @Ignore
    public void canInsertAccount() {
        sut.createAccount("CUST00001", "ACCNT00001", Money.toMoney("100.00", "ZAR"));
    }
}
