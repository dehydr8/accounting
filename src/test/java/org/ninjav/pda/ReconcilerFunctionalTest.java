package org.ninjav.pda;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.*;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/07.
 */
public class ReconcilerFunctionalTest extends BaseFunctionalTest {
    protected Reconciler reconciler;

    @Before
    public void setup() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super.setup();
        reconciler = new Reconciler(transferService);
        accountService.createAccount("ACNT00001", Money.toMoney("0.00", "ZAR"));
    }

    @Test
    public void whenReferenceNotMatched_mustReconcileToUnidentfied() {
        StatementLine line = new StatementLine(new Date(), "BAD REFERENCE", Money.toMoney("100.00", "ZAR"));
        reconciler.reconcile(line);

        Assert.assertEquals(Money.toMoney("-100.00", "ZAR"), accountService.getAccountBalance(ChartOfAccounts.BANK_CONTROL));
        Assert.assertEquals(Money.toMoney("100.00", "ZAR"), accountService.getAccountBalance(ChartOfAccounts.UNIDENTIFIED));

        List<Transaction> t1 = transferService.findTransactionsByAccountRef(ChartOfAccounts.BANK_CONTROL);
        assertEquals(1, t1.size());

        List<Transaction> t2 = transferService.findTransactionsByAccountRef(ChartOfAccounts.UNIDENTIFIED);
        assertEquals(1, t2.size());
    }

    @Test
    public void whenReferenceMatched_mustReconcileToDestinationAccount() {
        String customerAccount = "ACNT00001";
        StatementLine line = new StatementLine(new Date(), customerAccount, Money.toMoney("100.00", "ZAR"));

        reconciler.reconcile(line);

        Assert.assertEquals(Money.toMoney("-100.00", "ZAR"), accountService.getAccountBalance(ChartOfAccounts.BANK_CONTROL));
        Assert.assertEquals(Money.toMoney("100.00", "ZAR"), accountService.getAccountBalance(customerAccount));

        List<Transaction> t1 = transferService.findTransactionsByAccountRef(ChartOfAccounts.BANK_CONTROL);
        assertEquals(1, t1.size());

        List<Transaction> t2 = transferService.findTransactionsByAccountRef(customerAccount);
        assertEquals(1, t2.size());
    }
}