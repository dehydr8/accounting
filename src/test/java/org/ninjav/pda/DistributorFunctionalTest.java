package org.ninjav.pda;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.Transaction;
import org.ninjav.pda.model.Distribution;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/07.
 */
public class DistributorFunctionalTest extends BaseFunctionalTest {

    private String customerAccount;
    private String creditorAccount;

    @Before
    public void setup() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super.setup();
        customerAccount = "ACCNT00001";
        creditorAccount = "ACCNT00001:EDGARS";

        accountService.createAccount(customerAccount, Money.toMoney("100.00", "ZAR"));
        accountService.createAccount(creditorAccount, Money.toMoney("0.00", "ZAR"));
    }

    @Test
    public void givenPayments_canDistributeAccordingly() {
        Distributor d = new Distributor(transferService);
        Distribution dist = new Distribution();
        dist.transactionReference = "DIST00001";
        dist.transactionType = TransactionTypes.DISTRIBUTION;
        dist.sourceAccounts.put("ACCNT00001", Money.toMoney("-100.00", "ZAR"));
        dist.destinationAccounts.put("ACCNT00001:EDGARS", Money.toMoney("100.00", "ZAR"));
        d.distribute(dist);

        Assert.assertEquals(Money.toMoney("0.00", "ZAR"), accountService.getAccountBalance(customerAccount));
        Assert.assertEquals(Money.toMoney("100.00", "ZAR"), accountService.getAccountBalance(creditorAccount));

        List<Transaction> t1 = transferService.findTransactionsByAccountRef(customerAccount);
        assertEquals(1, t1.size());

        List<Transaction> t2 = transferService.findTransactionsByAccountRef(creditorAccount);
        assertEquals(1, t2.size());
    }
}