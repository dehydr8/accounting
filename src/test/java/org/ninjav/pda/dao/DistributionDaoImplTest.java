package org.ninjav.pda.dao;

import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.util.BankContextUtil;
import org.ninjav.pda.BaseFunctionalTest;
import org.ninjav.pda.TransactionTypes;
import org.ninjav.pda.model.Distribution;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/07.
 */
public class DistributionDaoImplTest extends BaseFunctionalTest {

    private DistributionDao distributionDao;
    private static final String consumerAccount01 = "ACCNT00001";
    private static final String creditorAccount01 = "ACCNT00001:EDGARS";

    @Before
    public void setup() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super.setup();
        distributionDao = BankContextUtil.getBean("distributionDao");
        distributionDao.truncateTables();
    }

    @Test
    public void canCreateDistribution() {

        Map<String, Money> sourceTransfers = new HashMap<>();
        sourceTransfers.put(consumerAccount01, Money.toMoney("-100.00", "ZAR"));
        Map<String, Money> destinationTransfers = new HashMap<>();
        destinationTransfers.put(creditorAccount01, Money.toMoney("100.00", "ZAR"));

        Distribution d = new Distribution("DIST00001", TransactionTypes.DISTRIBUTION,
                sourceTransfers, destinationTransfers);

        distributionDao.createDistribution(d);

        Set<String> distributionRefs = distributionDao.getDistributionTransactionRefs();
        assertThat(distributionRefs.size(), is(equalTo(1)));

        List<Distribution> distributions = distributionDao.getDistributions(distributionRefs);
        assertThat(distributions, is(notNullValue()));
        assertThat(distributions.get(0).sourceAccounts.size(), is(equalTo(1)));
        assertThat(distributions.get(0).destinationAccounts.size(), is(equalTo(1)));
    }
}