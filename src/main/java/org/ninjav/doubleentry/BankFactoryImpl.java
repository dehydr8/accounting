package org.ninjav.doubleentry;

import java.util.Date;

import org.ninjav.doubleentry.dao.AccountDao;
import org.ninjav.doubleentry.dao.ClientDao;
import org.ninjav.doubleentry.dao.TransactionDao;
import org.ninjav.doubleentry.util.BankContextUtil;
import org.ninjav.pda.dao.DistributionDao;

public class BankFactoryImpl implements BankFactory {
	
	@Override
    public AccountService getAccountService() {
        return BankContextUtil.getBean("accountService");
    }

    @Override
    public TransferService getTransferService() {
        return BankContextUtil.getBean("transferService");
    }

    @Override
    public void setupInitialData() {
        ClientDao clientDao = BankContextUtil.getBean("clientDao");
        clientDao.truncateTables();
        String clientRef = "Client_" + System.currentTimeMillis();
        clientDao.createClient(clientRef, new Date());
        
        AccountDao accountDao = BankContextUtil.getBean("accountDao");
        accountDao.truncateTables();
        accountDao.setClientRef(clientRef);
        
        TransactionDao transactionDao = BankContextUtil.getBean("transactionDao");
        transactionDao.truncateTables();
        transactionDao.setClientRef(clientRef);

        DistributionDao distributionDao = BankContextUtil.getBean("distributionDao");
        distributionDao.truncateTables();
    }
    
}