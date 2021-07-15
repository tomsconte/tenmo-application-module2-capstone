package com.techelevator.tenmo.model;

import java.math.BigDecimal;

//TODO: Make sure that everything in ViewTransfers is over in Transfer for the consolidation..

public class ViewTransfers {

    private Long transferId;
    private BigDecimal amount;
    private String user;
    private String userFrom;
    private String userTo;
    private String transferStatusDesc;
    private String transferTypeDesc;
    private Long accountId;


    public Long getTransferId() {
        return transferId;
    }
    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getUserFrom() {
        return userFrom;
    }
    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }
    public String getUserTo() {
        return userTo;
    }
    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }
    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }
    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }
    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }
    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
