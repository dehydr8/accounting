package org.ninjav.pda;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ninjav.doubleentry.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/07.
 */
public class CollectionFunctionalTest extends BaseFunctionalTest {

    @Before
    public void setup() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super.setup();
    }

    @Test
    public void canImportLine() {
        StatementLine line = new StatementLine(
                Date.from(Instant.parse("2016-12-03T10:15:30.00Z")), "ANNCT00001", Money.toMoney("100.00", "ZAR"));
        Importer i = new Importer(transferService);
        i.importLine(line);

        Assert.assertEquals(Money.toMoney("-100.00", "ZAR"), accountService.getAccountBalance(ChartOfAccounts.BANK));
        Assert.assertEquals(Money.toMoney("100.00", "ZAR"), accountService.getAccountBalance(ChartOfAccounts.BANK_CONTROL));

        List<Transaction> t1 = transferService.findTransactionsByAccountRef(ChartOfAccounts.BANK_CONTROL);
        assertEquals(1, t1.size());

        List<Transaction> t2 = transferService.findTransactionsByAccountRef(ChartOfAccounts.BANK);
        assertEquals(1, t2.size());
    }
}