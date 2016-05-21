package org.ninjav.pda.dao;

import org.ninjav.pda.model.Distribution;

import java.util.List;
import java.util.Set;

/**
 * Created by ninjav on 2016/05/07.
 */
public interface DistributionDao {

    void createDistribution(Distribution distribution);

    Set<String> getDistributionTransactionRefs();

    List<Distribution> getDistributions(Set<String> transactionRefs);

    void truncateTables();
}
