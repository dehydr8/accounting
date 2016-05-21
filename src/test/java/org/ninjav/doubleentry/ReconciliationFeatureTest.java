/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ninjav.doubleentry;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ninjav
 */
public class ReconciliationFeatureTest {

    private AccountService accountService;
    private TransferService transferService;
    
    private static final String BANK_CONTROL_ACCOUNT = "bank_control_ZAR";
    private static final String POSITIVE_UNIDENTIFIED_ACCOUNT = "positive_unidentified_ZAR";
    
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
        accountService.createAccount(BANK_CONTROL_ACCOUNT, toMoney("1000.00", "ZAR"));
        accountService.createAccount(POSITIVE_UNIDENTIFIED_ACCOUNT, toMoney("0.00", "ZAR"));

        Assert.assertEquals(toMoney("1000.00", "ZAR"), accountService.getAccountBalance(BANK_CONTROL_ACCOUNT));
        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(POSITIVE_UNIDENTIFIED_ACCOUNT));

        transferService.transferFunds(TransferRequest.builder()
                .reference("T1").type("testing")
                .account(BANK_CONTROL_ACCOUNT).amount(toMoney("-1000.00", "ZAR"))
                .account(POSITIVE_UNIDENTIFIED_ACCOUNT).amount(toMoney("1000.00", "ZAR"))
                .build());

        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(BANK_CONTROL_ACCOUNT));
        Assert.assertEquals(toMoney("1000.00", "ZAR"), accountService.getAccountBalance(POSITIVE_UNIDENTIFIED_ACCOUNT));

        List<Transaction> t1 = transferService.findTransactionsByAccountRef(BANK_CONTROL_ACCOUNT);
        Assert.assertEquals(1, t1.size());

        List<Transaction> t2 = transferService.findTransactionsByAccountRef(POSITIVE_UNIDENTIFIED_ACCOUNT);
        Assert.assertEquals(1, t2.size());
    }

    private static Money toMoney(String amount, String currency) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }
}
