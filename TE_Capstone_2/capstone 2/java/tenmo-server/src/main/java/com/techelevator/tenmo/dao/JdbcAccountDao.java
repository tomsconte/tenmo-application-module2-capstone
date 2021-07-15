package com.techelevator.tenmo.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long findAccountIdByUserId(Long userId) {
        try {
            String sql = "SELECT account_id FROM accounts WHERE user_id = ?";
            Long id = jdbcTemplate.queryForObject(sql, Long.class, userId);
            if (id != null) {
                return id;
            }
        } catch (DataAccessException e) {
            return 0L;
        }
        return 0L;
    }

    @Override
    public BigDecimal getBalanceByUserId(Long userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        if (balance != null) {
            return balance;
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getBalanceByAccountId(Long accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        if (balance != null) {
            return balance;
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal increaseBalanceByAmount(Long accountId, BigDecimal amount) {
        String sql = "UPDATE accounts " +
                "SET balance = balance + " + amount +
                " WHERE account_id = " + accountId +
                ";";
        try {
            jdbcTemplate.update(sql);
        } catch(Exception e){
            System.out.println("Problem: " + e.getMessage());
        }
        return getBalanceByAccountId(accountId);
    }

    @Override
    public BigDecimal decreaseBalanceByAmount(Long accountId, BigDecimal amount) {
        String sql = "UPDATE accounts "+
                "SET balance = balance - " + amount +
                " WHERE account_id = " + accountId + ";";
        try {
            jdbcTemplate.update(sql);
        } catch(Exception e){
            System.out.println("Problem: " + e.getMessage());
        }
        return getBalanceByAccountId(accountId);
    }
}