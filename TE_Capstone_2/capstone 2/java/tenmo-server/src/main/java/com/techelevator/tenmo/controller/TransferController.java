package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.ViewTransfers;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private AccountDao accountDao;
    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }


    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public String transfer(@RequestBody Transfer transfer) {
        Transfer newTransfer = new Transfer(transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        if (transferDao.createSendTransfer(newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(), newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount())) {
            return "Successful transfer! Please review your previous transfers!";
        }
        return "Unsuccessful transfer! Please input an amount that does not exceed your balance.";
    }

    @RequestMapping(value = "/transfers", method = RequestMethod.GET)
    public ViewTransfers[] listTransfersById(/*@PathVariable Long accountId*/) {
        List<ViewTransfers> allTransfersById = transferDao.getTransfersById();
        ViewTransfers[] transfersArray = new ViewTransfers[allTransfersById.size()];
        transfersArray = allTransfersById.toArray(transfersArray);
        return transfersArray;
    }

    @RequestMapping(value = "/transfers/details/{transferId}", method = RequestMethod.GET)
    public ViewTransfers transferInformation(@PathVariable Long transferId) {
        ViewTransfers transferInformation = transferDao.getTransferDescription(transferId);
        return transferInformation;
    }

    @RequestMapping(path = "/transfers/status/{statusId}", method = RequestMethod.PUT)
    public String updateReuest(@RequestBody Transfer transfer, @PathVariable int statusId) {
        String output = transferDao.updateTransferRequest(transfer, statusId);
        return output;
    }

}