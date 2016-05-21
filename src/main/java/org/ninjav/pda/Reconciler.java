package org.ninjav.pda;

import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.TransferRequest;
import org.ninjav.doubleentry.TransferService;
import org.omg.IOP.TransactionService;

/**
 * Created by ninjav on 2016/05/07.
 */
public class Reconciler {
    private TransferService transferService;

    public Reconciler(TransferService transferService) {
        this.transferService = transferService;
    }

    public void reconcile(StatementLine line) {
        try {

            transferService.transferFunds(TransferRequest.builder()
                    .reference(line.reference).type(TransactionTypes.DEPOSIT)
                    .account(ChartOfAccounts.BANK_CONTROL).amount(line.amount.negate())
                    .account(line.reference).amount(line.amount)
                    .build());

        } catch (Exception ex) {

            transferService.transferFunds(TransferRequest.builder()
                    .reference(line.reference).type("Deposit")
                    .account(ChartOfAccounts.BANK_CONTROL).amount(line.amount.negate())
                    .account(ChartOfAccounts.UNIDENTIFIED).amount(line.amount)
                    .build());
        }
    }
}
