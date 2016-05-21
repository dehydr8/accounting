package org.ninjav.pda;

import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.TransferRequest;
import org.ninjav.doubleentry.TransferService;

/**
 * Created by ninjav on 2016/05/07.
 *
 * Transfers money from a bank account to a control account.
 */
public class Importer {

    private TransferService transferService;

    public Importer(TransferService transferService) {
        this.transferService = transferService;
    }

    public void importLine(StatementLine line) {
        transferService.transferFunds(TransferRequest.builder()
                .reference(line.reference).type(classifyTransactionType(line.amount))
                .account(ChartOfAccounts.BANK).amount(line.amount.negate())
                .account(ChartOfAccounts.BANK_CONTROL).amount(line.amount)
                .build());
    }

    private String classifyTransactionType(Money money) {
        if (money.getAmount().intValue() >= 0) {
            return TransactionTypes.DEPOSIT;
        } else {
            return TransactionTypes.WITHDRAWL;
        }
    }
}
