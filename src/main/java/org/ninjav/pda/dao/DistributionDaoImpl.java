package org.ninjav.pda.dao;

import com.google.common.collect.Sets;
import org.ninjav.doubleentry.Money;
import org.ninjav.doubleentry.util.DbUtil;
import org.ninjav.pda.model.Distribution;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ninjav on 2016/05/07.
 */
public class DistributionDaoImpl extends JdbcDaoSupport implements DistributionDao {
    private DbUtil dbUtil;
    private int distributionId;

    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    @Override
    public void truncateTables() {
        getJdbcTemplate().execute("DELETE FROM distribution_source");
        getJdbcTemplate().execute("DELETE FROM distribution_destination");
        getJdbcTemplate().execute("DELETE FROM distribution");
        getJdbcTemplate().execute("TRUNCATE TABLE distribution_source");
        getJdbcTemplate().execute("TRUNCATE TABLE distribution_destination");
        //getJdbcTemplate().execute("TRUNCATE TABLE distribution");
    }

    @Override
    public Set<String> getDistributionTransactionRefs() {
        String sql = "SELECT transaction_ref FROM distribution";
        List<String> transactionRefs = getJdbcTemplate().query(sql, dbUtil.stringRowMapper());
        return Sets.newHashSet(transactionRefs);
    }


    private int getDistributionIdByTransactionReference(String transactionReference) {
        String sql = "SELECT id FROM distribution where transaction_ref = ?";
        List<Long> distributionIds = getJdbcTemplate().query(sql, dbUtil.longRowMapper(), transactionReference);
        return distributionIds.get(0).intValue();
    }

    @Override
    public void createDistribution(Distribution distribution) {
        String sql = "INSERT INTO distribution (transaction_ref, transaction_type) VALUES (?, ?)";
        getJdbcTemplate().update(sql,
                distribution.transactionReference, distribution.transactionType);

        distributionId = getDistributionIdByTransactionReference(distribution.transactionReference);

        output("DistributionID: " + distributionId);

        createDistributionSources(distribution.sourceAccounts);
        createDistributionDestinations(distribution.destinationAccounts);
    }

    private void createDistributionSources(Map<String, Money> sourceAccounts) {
        for (Map.Entry<String, Money> s : sourceAccounts.entrySet()) {
            String sql = "INSERT INTO distribution_source (account_ref, amount, currency, fk_distribution) VALUES (?, ?, ?, ?)";

            output("Source SQL: " + distributionId);

            getJdbcTemplate().update(sql,
                    s.getKey(), getAmount(s), getCurrencyCode(s), distributionId);
        }
    }

    private void createDistributionDestinations(Map<String, Money> destinationAccounts) {
        for (Map.Entry<String, Money> s : destinationAccounts.entrySet()) {
            String sql = "INSERT INTO distribution_destination (account_ref, amount, currency, fk_distribution) VALUES (?, ?, ?, ?)";

            output("Dest SQL: " + distributionId);

            getJdbcTemplate().update(sql,
                    s.getKey(), getAmount(s), getCurrencyCode(s), distributionId);
        }
    }

    private String getCurrencyCode(Map.Entry<String, Money> s) {
        return s.getValue().getCurrency().getCurrencyCode();
    }

    private BigDecimal getAmount(Map.Entry<String, Money> s) {
        return s.getValue().getAmount();
    }

    @Override
    public List<Distribution> getDistributions(Set<String> transactionRefs) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT id, transaction_ref, transaction_type ")
                .append("FROM distribution WHERE transaction_ref IN (")
                .append(dbUtil.commaSeparatedQuestionMarks(transactionRefs.size()))
                .append(")");
        return getJdbcTemplate().query(sql.toString(), new DistributionMapper(), transactionRefs.toArray());
    }

    private void output(String s) {
        return;
//        System.out.println("===============================================================");
//        System.out.println(s);
//        System.out.println("===============================================================");
    }

    private class DistributionMapper implements RowMapper<Distribution> {

        @Override
        public Distribution mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            String transactionRef = rs.getString("transaction_ref");
            String transactionType = rs.getString("transaction_type");
            List<AccountTransfer> sourceAccountTransfers = getSourceAccountTransfers(id);
            List<AccountTransfer> destinationAccountTransfers = getDestinationAccountTransfers(id);
            return new Distribution(transactionRef, transactionType,
                    mapAccountTransfers(sourceAccountTransfers),
                    mapAccountTransfers(destinationAccountTransfers));
        }
    }

    private Map<String, Money> mapAccountTransfers(List<AccountTransfer> transfers) {
        Map<String, Money> result = new HashMap<>();
        for (AccountTransfer t : transfers) {
            result.put(t.accountRef, t.funds);
        }
        return result;
    }

    private List<AccountTransfer> getSourceAccountTransfers(int id) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT account_ref, amount, currency ")
                .append("FROM distribution_source WHERE fk_distribution = ?");
        return getJdbcTemplate().query(sql.toString(), new AccountTransferMapper(), id);
    }

    private List<AccountTransfer> getDestinationAccountTransfers(int id) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT account_ref, amount, currency ")
                .append("FROM distribution_destination WHERE fk_distribution = ?");
        return getJdbcTemplate().query(sql.toString(), new AccountTransferMapper(), id);
    }

    private static class AccountTransferMapper implements RowMapper<AccountTransfer> {

        @Override
        public AccountTransfer mapRow(ResultSet rs, int rowNum) throws SQLException {
            String accountRef = rs.getString("account_ref");
            BigDecimal amount = new BigDecimal(rs.getString("amount"));
            Currency currency = Currency.getInstance(rs.getString("currency"));
            Money transferredFunds = new Money(amount, currency);
            return new AccountTransfer(accountRef, transferredFunds);
        }
    }

    private static class AccountTransfer {
        public String accountRef;
        public Money funds;

        public AccountTransfer(String accountRef, Money funds) {
            this.accountRef = accountRef;
            this.funds = funds;
        }
    }
}
