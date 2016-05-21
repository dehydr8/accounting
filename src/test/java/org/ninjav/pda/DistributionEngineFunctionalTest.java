package org.ninjav.pda;

import jdk.nashorn.internal.ir.annotations.Ignore;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.Transaction;
import org.ninjav.doubleentry.TransferService;
import org.ninjav.doubleentry.util.BankContextUtil;
import org.ninjav.pda.dao.DistributionDao;
import org.ninjav.pda.model.Distribution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/07.
 */
public class DistributionEngineFunctionalTest extends BaseFunctionalTest {

    private String consumerAccount01;
    private String consumerAccount02;
    private String creditorAccount01;
    private String creditorAccount02;

    private DistributionDao distributionDao;

    @Before
    public void setup() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super.setup();

        consumerAccount01 = "ACCNT00001";
        creditorAccount01 = "ACCNT00001:EDGARS";

        consumerAccount02 = "ACCNT00002";
        creditorAccount02 = "ACCNT00002:EDGARS";

        accountService.createAccount(consumerAccount01, Money.toMoney("100.00", "ZAR"));
        accountService.createAccount(creditorAccount01, Money.toMoney("0.00", "ZAR"));

        accountService.createAccount(consumerAccount02, Money.toMoney("50.00", "ZAR"));
        accountService.createAccount(creditorAccount02, Money.toMoney("0.00", "ZAR"));

        distributionDao = BankContextUtil.getBean("distributionDao");

        Map<String, Money> sourceTransfers01 = new HashMap<>();
        sourceTransfers01.put(consumerAccount01, Money.toMoney("-100.00", "ZAR"));
        Map<String, Money> destinationTransfers01 = new HashMap<>();
        destinationTransfers01.put(creditorAccount01, Money.toMoney("100.00", "ZAR"));

        Distribution d1 = new Distribution("DIST00001", TransactionTypes.DISTRIBUTION,
                sourceTransfers01, destinationTransfers01);
        distributionDao.createDistribution(d1);


        Map<String, Money> sourceTransfers02 = new HashMap<>();
        sourceTransfers02.put(consumerAccount02, Money.toMoney("-50.00", "ZAR"));
        Map<String, Money> destinationTransfers02 = new HashMap<>();
        destinationTransfers02.put(creditorAccount02, Money.toMoney("50.00", "ZAR"));

        Distribution d2 = new Distribution("DIST00002", TransactionTypes.DISTRIBUTION,
                sourceTransfers02, destinationTransfers02);
        distributionDao.createDistribution(d2);
    }

    @Test
    public void givenDistributions_canDistribute() {
        DistributionEngine e = new DistributionEngine(distributionDao, transferService);
        e.distribute();

        Assert.assertEquals(Money.toMoney("0.00", "ZAR"), accountService.getAccountBalance(consumerAccount01));
        Assert.assertEquals(Money.toMoney("100.00", "ZAR"), accountService.getAccountBalance(creditorAccount01));

        Assert.assertEquals(Money.toMoney("0.00", "ZAR"), accountService.getAccountBalance(consumerAccount02));
        Assert.assertEquals(Money.toMoney("50.00", "ZAR"), accountService.getAccountBalance(creditorAccount02));


        List<Transaction> t1 = transferService.findTransactionsByAccountRef(consumerAccount01);
        assertEquals(1, t1.size());

        List<Transaction> t2 = transferService.findTransactionsByAccountRef(creditorAccount01);
        assertEquals(1, t2.size());

        List<Transaction> t3 = transferService.findTransactionsByAccountRef(consumerAccount02);
        assertEquals(1, t3.size());

        List<Transaction> t4 = transferService.findTransactionsByAccountRef(creditorAccount02);
        assertEquals(1, t4.size());
    }
}