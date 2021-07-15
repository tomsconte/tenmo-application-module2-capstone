package com.techelevator.tenmo.dao;
import java.math.BigDecimal;
public interface AccountDao {
    Long findAccountIdByUserId(Long userId);
    BigDecimal getBalanceByUserId(Long userId);
    BigDecimal getBalanceByAccountId(Long accountId);
    BigDecimal increaseBalanceByAmount(Long userId, BigDecimal amount);
    BigDecimal decreaseBalanceByAmount(Long userId, BigDecimal amount);
}