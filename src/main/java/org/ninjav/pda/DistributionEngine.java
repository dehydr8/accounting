package org.ninjav.pda;

import org.ninjav.doubleentry.TransferService;
import org.ninjav.pda.dao.DistributionDao;
import org.ninjav.pda.model.Distribution;

/**
 * Created by ninjav on 2016/05/07.
 */
public class DistributionEngine {

    private DistributionDao distributionDao;
    private TransferService transferService;

    public DistributionEngine(DistributionDao distributionDao, TransferService transferService) {
        this.distributionDao = distributionDao;
        this.transferService = transferService;
    }

    public void distribute() {
        Distributor distributor = new Distributor(transferService);
        for (Distribution d : distributionDao.getDistributions(distributionDao.getDistributionTransactionRefs())) {
            distributor.distribute(d);
        }
    }
}
