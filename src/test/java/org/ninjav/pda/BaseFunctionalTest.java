package org.ninjav.pda;

import org.junit.Before;
import org.ninjav.doubleentry.AccountService;
import org.ninjav.doubleentry.BankFactory;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.TransferService;

/**
 * Created by ninjav on 2016/05/07.
 */
public class BaseFunctionalTest {
    protected TransferService transferService;
    protected AccountService accountService;

    @Before
    public void setup() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        BankFactory bankFactory = (BankFactory) Class.forName(
                "org.ninjav.doubleentry.BankFactoryImpl").newInstance();

        bankFactory.setupInitialData();

        transferService = bankFactory.getTransferService();

        accountService = bankFactory.getAccountService();
        accountService.createAccount(ChartOfAccounts.BANK, Money.toMoney("0.00", "ZAR"));
        accountService.createAccount(ChartOfAccounts.BANK_CONTROL, Money.toMoney("0.00", "ZAR"));
        accountService.createAccount(ChartOfAccounts.UNIDENTIFIED, Money.toMoney("0.00", "ZAR"));
    }
}
