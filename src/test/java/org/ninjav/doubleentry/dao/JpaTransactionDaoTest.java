package org.ninjav.doubleentry.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/22.
 */
public class JpaTransactionDaoTest {
    private EntityManager em;
    private String accountRef01;
    private String customerRef01;
    private TransactionDao sut;

    @Before
    public void setup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-test");
        em = emf.createEntityManager();
        em.getTransaction().begin();

        sut = new JpaTransactionDao(em);

        accountRef01 = "ACCNT00001";
        customerRef01 = "CLI00001";

        sut.setClientRef(customerRef01);
    }

    @After
    public void teardown() {
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void failit() {
        fail("You still have to implement these tests");
    }
}