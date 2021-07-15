package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.ViewTransfers;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

   boolean createSendTransfer(Long transferTypeId, Long transferStatusId, Long accountFrom, Long accountTo, BigDecimal amount);
   List<ViewTransfers> getTransfersById();
   ViewTransfers getTransferDescription (Long transferId);
   String updateTransferRequest(Transfer transfer, int statusId);

//   boolean updateTransferStatus(Long transferId, Long transferStatusId, Long accountFrom, Long accountTo, BigDecimal amount);

//
//   Transfer getTransferByTransferId(Long transferId);
}
