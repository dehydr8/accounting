package org.ninjav.doubleentry.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.entity.ClientEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Date;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/09.
 */
public class JpaClientDaoTest {
    private EntityManager em;
    private String accountRef01;
    private String customerRef01;
    private ClientDao sut;

    @Before
    public void setup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-test");
        em = emf.createEntityManager();
        em.getTransaction().begin();

        sut = new JpaClientDao(em);

        accountRef01 = "ACCNT00001";
        customerRef01 = "CLI00001";
    }

    @After
    public void teardown() {
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testCreateClient() throws Exception {
        sut.createClient(customerRef01, new Date());

        assertClientExists(customerRef01);
    }

    private void assertClientExists(CharSequence clientReference) {
        TypedQuery<ClientEntity> query = em.createQuery(
                "select c from ClientEntity c where c.reference = :reference", ClientEntity.class);
        query.setParameter("reference", clientReference);
        ClientEntity newAccountEntity = query.getSingleResult();
        assertThat(newAccountEntity, is(notNullValue()));
    }
}