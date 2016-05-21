package org.ninjav.doubleentry;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

/**
 * Created by ninjav on 2016/05/06.
 */
public class StatementImportFeatureTest {
    private AccountService accountService;
    private TransferService transferService;

    private static final String BANK_ACCOUNT = "bank_ZAR";
    private static final String BANK_CONTROL_ACCOUNT = "bank_control_ZAR";

    @Before
    public void setupSystemStateBeforeEachTest() throws Exception {
        BankFactory bankFactory = (BankFactory) Class.forName(
                "org.ninjav.doubleentry.BankFactoryImpl").newInstance();

        accountService = bankFactory.getAccountService();
        transferService = bankFactory.getTransferService();

        bankFactory.setupInitialData();
    }

    @Test
    public void whenReconcilerRuns_controlAccountIsCleared() {
        accountService.createAccount(BANK_ACCOUNT, toMoney("10000.00", "ZAR"));
        accountService.createAccount(BANK_CONTROL_ACCOUNT, toMoney("0.00", "ZAR"));

        Assert.assertEquals(toMoney("10000.00", "ZAR"), accountService.getAccountBalance(BANK_ACCOUNT));
        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(BANK_CONTROL_ACCOUNT));

        // A statement comes in with 2 transactions
        transferService.transferFunds(TransferRequest.builder()
                .reference("TRNREF1").type("Deposit")
                .account(BANK_ACCOUNT).amount(toMoney("-5000.00", "ZAR"))
                .account(BANK_CONTROL_ACCOUNT).amount(toMoney("5000.00", "ZAR"))
                .build());

        transferService.transferFunds(TransferRequest.builder()
                .reference("TRNREF2").type("Deposit")
                .account(BANK_ACCOUNT).amount(toMoney("-5000.00", "ZAR"))
                .account(BANK_CONTROL_ACCOUNT).amount(toMoney("5000.00", "ZAR"))
                .build());

        // Now the control account has transactions to be reconciled
        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(BANK_ACCOUNT));
        Assert.assertEquals(toMoney("10000.00", "ZAR"), accountService.getAccountBalance(BANK_CONTROL_ACCOUNT));

        List<Transaction> t1 = transferService.findTransactionsByAccountRef(BANK_ACCOUNT);
        Assert.assertEquals(2, t1.size());

        List<Transaction> t2 = transferService.findTransactionsByAccountRef(BANK_CONTROL_ACCOUNT);
        Assert.assertEquals(2, t2.size());
    }

    private static Money toMoney(String amount, String currency) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }


}
