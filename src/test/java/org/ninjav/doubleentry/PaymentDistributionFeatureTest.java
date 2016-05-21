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
public class PaymentDistributionFeatureTest {
    private AccountService accountService;

    private TransferService transferService;
    private final String PDA_CONTROL = "CONTROL"; // Imported statements land here
    private final String PDA_FUNDS = "FUNDS";     // NPDA funding account for backing bad payers
    private final String PDA_LOANS = "LOANS";     // NPDA keeps track of subsidised distributions

    private final String PIETIE = "PIETIE";       // Pietie is a consumer
    private final String PIETIE_PDA_OWED = "PIETIE_PDA_OWED";        // The PDA might loan money to make payments
    private final String PIETIE_EDGARS_OWED = "PIETIE_EDGARS_OWED";
    private final String PIETIE_EDGARS_DISTRIBUTING = "PIETIE_EDGARS_DISTRIBUTING";   // Pietie's money in transit
    private final String PIETIE_EDGARS_PAID = "PIETIE_EDGARS_PAID";           // Pietie paid Edgars money

    private final String EDGARS_DISTRIBUTING = "EDGARS_DISTRIBUTING";

    private final String SANNIE = "SANNIE";       // Sannie is a consumer
    private final String SANNIE_EDGARS = "SANNIE_EDGARS";   // Sannie owes Edgars money

    private final String EDGARS = "EDGARS"; // Edgars is a creditor, consumers ows them money


    // Bank
    // Control
    // Unidentified

    // Consumer
    // Consumer-Creditor-owing
    // Consumer-Creditor-distributing
    // Consumer-Creditor-paid

    // Creditor-distributing

    // Pda-funding
    // Pda-funded

    @Before
    public void setupSystemStateBeforeEachTest() throws Exception {
        BankFactory bankFactory = (BankFactory) Class.forName(
                "org.ninjav.doubleentry.BankFactoryImpl").newInstance();

        accountService = bankFactory.getAccountService();
        transferService = bankFactory.getTransferService();

        bankFactory.setupInitialData();
    }

    @Test
    public void whenTheStarsAlign_mustDistributePayments() {

        //------------------------------------------------------------------------------------------------------------
        // Stage 1 - Reconciliation
        //------------------------------------------------------------------------------------------------------------
        // We recevied money in the PDA_CONTROL account
        accountService.createAccount(PDA_CONTROL, toMoney("100", "ZAR"));

        // We received nothing from Pietie yet
        accountService.createAccount(PIETIE, toMoney("0.00", "ZAR"));

        // Pietie owes Edgars 500
        accountService.createAccount(PIETIE_EDGARS_OWED, toMoney("-500.00", "ZAR"));

        // Pietie has no money allocated for distribution
        accountService.createAccount(PIETIE_EDGARS_DISTRIBUTING, toMoney("0.00", "ZAR"));

        // Pietie paid nothing to Edgars as of yet
        accountService.createAccount(PIETIE_EDGARS_PAID, toMoney("0.00", "ZAR"));

        // No money is avaiable for distribution to Edgars
        accountService.createAccount(EDGARS_DISTRIBUTING, toMoney("0.00", "ZAR"));

        // Money in PDA_CONTROL account is reconciled to PIETIE
        transferService.transferFunds(TransferRequest.builder()
                .reference("T1").type("Reconcile")
                .account(PDA_CONTROL).amount(toMoney("-100.00", "ZAR"))
                .account(PIETIE).amount(toMoney("100.00", "ZAR"))
                .build());

        // Control account is empty, and Pietie has a balance of 100, Pietie owes Edgars 500
        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(PDA_CONTROL));
        Assert.assertEquals(toMoney("100.00", "ZAR"), accountService.getAccountBalance(PIETIE));

        // Control account had one transaction
        List<Transaction> t1 = transferService.findTransactionsByAccountRef(PDA_CONTROL);
        Assert.assertEquals(1, t1.size());

        // Pietie's account had one transaction
        List<Transaction> t2 = transferService.findTransactionsByAccountRef(PIETIE);
        Assert.assertEquals(1, t2.size());

        //------------------------------------------------------------------------------------------------------------
        // Stage 2 - Distribution
        //------------------------------------------------------------------------------------------------------------

        // Distrbution engine sees Pieie has money for Edgars
        transferService.transferFunds(TransferRequest.builder()
                .reference("T2").type("Distribution")
                .account(PIETIE).amount(toMoney("-100.00", "ZAR"))
                .account(PIETIE_EDGARS_DISTRIBUTING).amount(toMoney("100.00", "ZAR"))
                .build());

        // Pietie's account is empty
        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(PIETIE));
        // Money is ready for distribution to Edgars
        Assert.assertEquals(toMoney("100.00", "ZAR"), accountService.getAccountBalance(PIETIE_EDGARS_DISTRIBUTING));

        // Pietie's account has 2 transactions
        List<Transaction> t4 = transferService.findTransactionsByAccountRef(PIETIE);
        Assert.assertEquals(2, t4.size());

        // Pietie's Edgars account has 1 transaction
        List<Transaction> t5 = transferService.findTransactionsByAccountRef(PIETIE_EDGARS_DISTRIBUTING);
        Assert.assertEquals(1, t5.size());

        //------------------------------------------------------------------------------------------------------------
        // Stage 3 - Consolidation
        //------------------------------------------------------------------------------------------------------------

        // The consolidator moves all money for edgars distribution into one place
        transferService.transferFunds(TransferRequest.builder()
                .reference("T3").type("Consolidation")
                .account(PIETIE_EDGARS_DISTRIBUTING).amount(toMoney("-100.00", "ZAR"))
                .account(EDGARS_DISTRIBUTING).amount(toMoney("100.00", "ZAR"))
                .build());

        // Pietie's Edgars distribution account is empty
        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(PIETIE_EDGARS_DISTRIBUTING));
        // Edgars has money to receive
        Assert.assertEquals(toMoney("100.00", "ZAR"), accountService.getAccountBalance(EDGARS_DISTRIBUTING));

        // Pietie's Edgars distribution account has 2 transactions
        List<Transaction> t6 = transferService.findTransactionsByAccountRef(PIETIE_EDGARS_DISTRIBUTING);
        Assert.assertEquals(2, t6.size());

        // Edgars Distribution account has 1 transaction
        List<Transaction> t7 = transferService.findTransactionsByAccountRef(EDGARS_DISTRIBUTING);
        Assert.assertEquals(1, t7.size());

        //------------------------------------------------------------------------------------------------------------
        // Stage 4 - Payment
        //------------------------------------------------------------------------------------------------------------
        // Edgars payment was a success
        transferService.transferFunds(TransferRequest.builder()
                .reference("T4").type("Payment")
                .account(EDGARS_DISTRIBUTING).amount(toMoney("-100.00", "ZAR"))
                .account(PIETIE_EDGARS_PAID).amount(toMoney("100.00", "ZAR"))
                .build());


        // Look at report and move money where it should go



        //------------------------------------------------------------------------------------------------------------
        // Stage 5 (or 1) - Recon
        //------------------------------------------------------------------------------------------------------------
        // Bank statement imports and the control account is set up
    }

    @Test
    public void whenInsufficientFunds_mustFundDistribution() {
        //------------------------------------------------------------------------------------------------------------
        // Stage 1 - Assume recon already took place
        //------------------------------------------------------------------------------------------------------------

        // We received 100 from Pietie
        accountService.createAccount(PIETIE, toMoney("50.00", "ZAR"));
        // Pietie might need a loan from the PDA
        accountService.createAccount(PIETIE_PDA_OWED, toMoney("0.00", "ZAR"));
        // Pietie owes Edgars 500
        accountService.createAccount(PIETIE_EDGARS_DISTRIBUTING, toMoney("-500.00", "ZAR"));
        // PDA has some funds to stand in for underpaying consumers
        accountService.createAccount(PDA_FUNDS, toMoney("1000.00", "ZAR"));
        // PDA keeps track of how much funds were subsidised for distribution
        accountService.createAccount(PDA_LOANS, toMoney("0.00", "ZAR"));

        //------------------------------------------------------------------------------------------------------------
        // Stage 2 - Distribution
        //------------------------------------------------------------------------------------------------------------

        // Pietie pays Edgars 100, but only has 50 to pay, so NPDA borrows pietie money
        transferService.transferFunds(TransferRequest.builder()
                .reference("T1").type("Subsidised distribution")
                .account(PDA_LOANS).amount(toMoney("50", "ZAR"))
                .account(PDA_FUNDS).amount(toMoney("-50.00", "ZAR"))
                .account(PIETIE_PDA_OWED).amount(toMoney("-50.00", "ZAR"))
                .account(PIETIE).amount(toMoney("-50.00", "ZAR"))
                .account(PIETIE_EDGARS_DISTRIBUTING).amount(toMoney("100.00", "ZAR"))
                .build());

        // PDA is 50 poorer
        Assert.assertEquals(toMoney("950.00", "ZAR"), accountService.getAccountBalance(PDA_FUNDS));
        // PDA knows how much money it subsidised
        Assert.assertEquals(toMoney("50.00", "ZAR"), accountService.getAccountBalance(PDA_LOANS));
        // Pietie now owes the PDA money too
        Assert.assertEquals(toMoney("-50.00", "ZAR"), accountService.getAccountBalance(PIETIE_PDA_OWED));
        // Pietie has no money for distribution any more
        Assert.assertEquals(toMoney("0.00", "ZAR"), accountService.getAccountBalance(PIETIE));
        // Pietie owes Edgars only 400
        Assert.assertEquals(toMoney("-400.00", "ZAR"), accountService.getAccountBalance(PIETIE_EDGARS_DISTRIBUTING));

        List<Transaction> trns = transferService.findTransactionsByAccountRef(PIETIE);
        dumpTransactions(trns);
    }

    @Test
    public void whenManyConsumersOweOneCreditor_mustConsolidateDistribution() {
        // Pietie has 100 to distribute and owes Edgars 500
        accountService.createAccount(PIETIE, toMoney("100.00", "ZAR"));
        accountService.createAccount(PIETIE_EDGARS_DISTRIBUTING, toMoney("-500.00", "ZAR"));
        accountService.createAccount(EDGARS, toMoney("0.00", "ZAR"));

        // Sannie has 50 to distribute and owes Edgars 300
        accountService.createAccount(SANNIE, toMoney("50.00", "ZAR"));
        accountService.createAccount(SANNIE_EDGARS, toMoney("-300.00", "ZAR"));

        // The payment engine distributes funds for each consumer to creditors
        transferService.transferFunds(TransferRequest.builder()
                .reference("T1").type("Distribution")
                .account(PIETIE).amount(toMoney("-100.00", "ZAR"))
                .account(PIETIE_EDGARS_DISTRIBUTING).amount(toMoney("100.00", "ZAR"))
                .build());

        transferService.transferFunds(TransferRequest.builder()
                .reference("T2").type("Distribution")
                .account(SANNIE).amount(toMoney("-50.00", "ZAR"))
                .account(SANNIE_EDGARS).amount(toMoney("50.00", "ZAR"))
                .build());


        // The payment engine consolidates payments into a transmission account for Edgars
        transferService.transferFunds(TransferRequest.builder()
                .reference("T3").type("Consolidation")
                .account(PIETIE_EDGARS_DISTRIBUTING).amount(toMoney("-100.00", "ZAR"))
                .account(SANNIE_EDGARS).amount(toMoney("-50.00", "ZAR"))
                .account(EDGARS).amount(toMoney("150.00", "ZAR"))
                .build());

    }

    private void dumpTransactions(List<Transaction> trns) {
        for (Transaction t : trns) {
            System.out.println(t);
        }
    }

    private static Money toMoney(String amount, String currency) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }

}
