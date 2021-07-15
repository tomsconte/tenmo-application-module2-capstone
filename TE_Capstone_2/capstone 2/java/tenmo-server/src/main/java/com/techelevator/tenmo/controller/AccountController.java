package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.math.BigDecimal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/accounts/{userId}/balance", method = RequestMethod.GET)
    public BigDecimal balance(@Valid @PathVariable Long userId) {
        BigDecimal balance = accountDao.getBalanceByUserId(userId);
        return balance;
    }

    @RequestMapping(value = "/accounts/{userId}", method = RequestMethod.GET)
    public Long accountId(@Valid @PathVariable Long userId) {
        Long accountId = accountDao.findAccountIdByUserId(userId);
        return accountId;
    }
}