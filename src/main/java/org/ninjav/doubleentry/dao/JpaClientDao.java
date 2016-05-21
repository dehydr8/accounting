package org.ninjav.doubleentry.dao;

import org.ninjav.doubleentry.entity.ClientEntity;
import org.ninjav.util.TimeSource;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created by ninjav on 2016/05/09.
 */
public class JpaClientDao implements ClientDao {

    private EntityManager em;

    private TimeSource time;

    public JpaClientDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public void createClient(String clientRef, Date creationDate) {
        ClientEntity c = new ClientEntity();
        c.setReference(clientRef);
        c.setCreationDate(time.now());
        em.persist(c);
    }

    @Override
    public void truncateTables() {

    }
}
