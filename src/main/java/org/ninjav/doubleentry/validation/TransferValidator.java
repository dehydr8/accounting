package org.ninjav.doubleentry.validation;

import org.ninjav.doubleentry.AccountNotFoundException;
import org.ninjav.doubleentry.InsufficientFundsException;
import org.ninjav.doubleentry.TransactionLeg;
import org.ninjav.doubleentry.TransferRequest;

public interface TransferValidator {

	void validateTransferRequest(TransferRequest transferRequest);

	void isTransactionBalanced(Iterable<TransactionLeg> legs) throws TransferValidationException;

	void currenciesMatch(Iterable<TransactionLeg> legs) throws TransferValidationException, AccountNotFoundException;

	void validBalance(Iterable<TransactionLeg> legs) throws InsufficientFundsException;

}
