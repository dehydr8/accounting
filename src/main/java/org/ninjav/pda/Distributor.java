package org.ninjav.pda;

import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.TransferRequest;
import org.ninjav.doubleentry.TransferService;
import org.ninjav.pda.model.Distribution;

import java.util.Map;

/**
 * Created by ninjav on 2016/05/07.
 */
public class Distributor {
    private TransferService transferService;

    public Distributor(TransferService transferService) {
        this.transferService = transferService;
    }

    public void distribute(Distribution dist) {
        TransferRequest.AccountStep builder = TransferRequest.builder()
                .reference(dist.transactionReference).type(dist.transactionType);

        for (Map.Entry<String, Money> s : dist.sourceAccounts.entrySet()) {
            builder.account(s.getKey()).amount(s.getValue());
        }

        for (Map.Entry<String, Money> d : dist.destinationAccounts.entrySet()) {
            builder.account(d.getKey()).amount(d.getValue());
        }

        transferService.transferFunds(((TransferRequest.Builder) builder).build());
    }
}
