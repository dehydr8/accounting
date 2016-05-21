package org.ninjav.doubleentry.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.ninjav.doubleentry.Transaction;
import org.ninjav.doubleentry.dao.TransactionDao;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import org.ninjav.doubleentry.AccountNotFoundException;
import org.ninjav.doubleentry.InsufficientFundsException;
import org.ninjav.doubleentry.TransactionLeg;
import org.ninjav.doubleentry.TransferRequest;
import org.ninjav.doubleentry.TransferService;
import org.ninjav.doubleentry.dao.AccountDao;
import org.ninjav.doubleentry.validation.TransferValidator;

/**
* Implements the methods of the transfer service.
*/
public class TransferServiceImpl implements TransferService {
	private TransferValidator validator;
	private AccountDao accountDao;
	private TransactionDao transactionDao;
	
	@Override
	@Transactional(rollbackFor=Exception.class, isolation = Isolation.SERIALIZABLE)
	public void transferFunds(TransferRequest transferRequest) throws InsufficientFundsException, AccountNotFoundException {
		validateRequest(transferRequest);
		for (TransactionLeg leg : transferRequest.getLegs()) {
			accountDao.updateBalance(leg);
		}
		validator.validBalance(transferRequest.getLegs());
		storeTransaction(transferRequest);
	}
	
	private void validateRequest(TransferRequest request) {
		validator.validateTransferRequest(request);
		validator.isTransactionBalanced(request.getLegs());
		validator.currenciesMatch(request.getLegs());
	}
	
	private void storeTransaction(TransferRequest request) {
		Transaction transaction = new Transaction(
			request.getTransactionRef(),
			request.getTransactionType(),
			new Date(),
			request.getLegs()
		);
		transactionDao.storeTransaction(transaction);
	}
	
	@Override
	public List<Transaction> findTransactionsByAccountRef(String accountRef) throws AccountNotFoundException {
		if (!accountDao.accountExists(accountRef)) {
			throw new AccountNotFoundException(accountRef);
		}
		Set<String> transactionRefs = transactionDao.getTransactionRefsForAccount(accountRef);
		if (transactionRefs.isEmpty()) {
			return Lists.newArrayList();
		}
		return transactionDao.getTransactions(Lists.newArrayList(transactionRefs));
	}
	
	@Override
	public Transaction getTransactionByRef(String transactionRef) {
		return transactionDao.getTransactionByRef(transactionRef);
	}
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public void setTransactionDao(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}
	
	public void setValidator(TransferValidator validator) {
		this.validator = validator;
	}

}


