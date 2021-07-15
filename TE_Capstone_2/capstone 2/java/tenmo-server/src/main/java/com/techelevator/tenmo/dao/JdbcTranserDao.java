package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.ViewTransfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTranserDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    public JdbcTranserDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

    @Override
    public boolean createSendTransfer(Long transferTypeId, Long transferStatusId, Long accountIdFrom, Long accountIdTo, BigDecimal amount) {
        if (amount.compareTo(accountDao.getBalanceByAccountId(accountIdFrom)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?);";
            if(transferTypeId == 1) {
                try {
                    jdbcTemplate.update(sql, transferTypeId, transferStatusId, accountIdFrom, accountIdTo, amount);
                } catch (Exception e) {
                    System.out.println("Message " + e.getMessage() + " and also " + e.getCause());
                    return false;
                }
                return true;
            }
            if(transferTypeId == 2) {
                try {
                    jdbcTemplate.update(sql, transferTypeId, transferStatusId, accountIdFrom, accountIdTo, amount);
                    accountDao.decreaseBalanceByAmount(accountIdFrom, amount);
                    accountDao.increaseBalanceByAmount(accountIdTo, amount);
                } catch (Exception e) {
                    System.out.println("Message " + e.getMessage() + " and also " + e.getCause());
                    return false;
                }
                return true;
            }
        } else {
            System.out.println("You don't have enough funds to complete the transaction");
            return false;
        }
        return false;
    }

    //TODO: Make sure to change ViewTransfers to Transfers when the consolidation is completed.

    public List<ViewTransfers> getTransfersById() {
        List<ViewTransfers> allTransfersById = new ArrayList<>();
        String sql = "SELECT t.transfer_id, 'To: '||u.username AS username, t.amount, ts.transfer_status_desc, t.account_from AS account_id "
                + "FROM transfers t "
                + "INNER JOIN accounts a ON t.account_to = a.account_id "
                + "INNER JOIN users u ON a.user_id = u.user_id "
                + "INNER JOIN transfer_statuses ts "
                + "ON ts.transfer_status_id = t.transfer_status_id "
                + "UNION "
                + "SELECT t.transfer_id, 'From: '||u.username AS username, t.amount, ts.transfer_status_desc, t.account_to AS account_id "
                + "FROM transfers t "
                + "INNER JOIN accounts a ON t.account_from = a.account_id "
                + "INNER JOIN users u ON a.user_id = u.user_id "
                + "INNER JOIN transfer_statuses ts "
                + "ON ts.transfer_status_id = t.transfer_status_id "
                + "ORDER BY transfer_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            ViewTransfers viewTransfers = mapRowToViewTransfers(results);
            allTransfersById.add(viewTransfers);
        }
        return allTransfersById;
    }

    //TODO: Same with this ViewTransfers. This needs to be Transfer.

    public ViewTransfers getTransferDescription (Long transferId) {
        ViewTransfers allTransfersById = new ViewTransfers();
        String sql = "SELECT t.transfer_id, t.amount, tt.transfer_type_desc, ts.transfer_status_desc, ua.username AS user_from, ub.username AS user_to " +
                        "FROM transfers t " +
                        "INNER JOIN transfer_statuses ts " +
                        "ON ts.transfer_status_id = t.transfer_status_id " +
                        "INNER JOIN transfer_types tt " +
                        "ON t.transfer_type_id = tt.transfer_type_id " +
                        "INNER JOIN accounts a ON t.account_from = a.account_id " +
                        "INNER JOIN accounts b ON t.account_to = b.account_id " +
                        "INNER JOIN users ua ON ua.user_id = a.user_id " +
                        "INNER JOIN users ub ON ub.user_id = b.user_id " +
                        "WHERE t.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()) {
            allTransfersById = mapRowToViewTransfers_AllFields(results);
        }
        return allTransfersById;
    }

    @Override
    public String updateTransferRequest(Transfer transfer, int statusId) {
        if (statusId == 3) {
            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            return "Update successful";
        }
        if (!(accountDao.getBalanceByAccountId(transfer.getAccountFrom()).compareTo(transfer.getAmount()) == -1)) {
            String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
            jdbcTemplate.update(sql, statusId, transfer.getTransferId());
            accountDao.increaseBalanceByAmount(transfer.getAccountTo(), transfer.getAmount());
            accountDao.decreaseBalanceByAmount(transfer.getAccountFrom(), transfer.getAmount());
            return "Update successful";
        } else {
            return "Insufficient funds for transfer";
        }
    }

    //TODO: This needs to be consolidated using try/catches. Have a base and then go from there.

    private ViewTransfers mapRowToViewTransfers(SqlRowSet rs) {
        ViewTransfers viewTransfers = new ViewTransfers();
        viewTransfers.setTransferId(rs.getLong("transfer_id"));
        viewTransfers.setUser(rs.getString("username"));
        viewTransfers.setAmount(rs.getBigDecimal("amount"));
        viewTransfers.setTransferStatusDesc(rs.getString("transfer_status_desc"));
        try {
            viewTransfers.setAccountId(rs.getLong("account_id"));
        } catch (Exception ex) {}
        return viewTransfers;
    }

    private ViewTransfers mapRowToViewTransfers_AllFields(SqlRowSet rs) {
        ViewTransfers viewTransfers = new ViewTransfers();
        viewTransfers.setTransferId(rs.getLong("transfer_id"));
        viewTransfers.setAmount(rs.getBigDecimal("amount"));
        viewTransfers.setTransferTypeDesc(rs.getString("transfer_type_desc"));
        viewTransfers.setTransferStatusDesc(rs.getString("transfer_status_desc"));
        viewTransfers.setUserFrom(rs.getString("user_from"));
        viewTransfers.setUserTo(rs.getString("user_to"));
        return viewTransfers;
    }
}