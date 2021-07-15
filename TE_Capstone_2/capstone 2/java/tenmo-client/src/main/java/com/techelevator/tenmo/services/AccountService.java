package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.ViewTransfers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AccountService {

    public static String AUTH_TOKEN = "";

    private static String API_BASE_URL = "";
    private RestTemplate restTemplate = new RestTemplate();


    public AccountService(String url) {
        this.API_BASE_URL = url;
    }

    public BigDecimal getBalanceByUserId(int userId){
        BigDecimal account =  new BigDecimal("0.00");

        try {
            account = restTemplate.exchange(API_BASE_URL + "accounts/" + userId + "/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (Exception e){
            System.out.println("This happened: " + e.getMessage() + " and " + e.getCause());
        }
        return account;
    }

    public Long getAccountByUserId(int userId){
        Long account = 0L;

        try {
            account = restTemplate.exchange(API_BASE_URL + "accounts/" + userId, HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
        } catch (Exception e){
            System.out.println("This happened: " + e.getMessage() + " and " + e.getCause());
        }
        return account;
    }

    public void displayTransfers(AuthenticatedUser currentUser){
        ViewTransfers[] transfers = null;
        System.out.println("--------------------------------------------------");
        System.out.println("Transfers                                         ");
        System.out.println("ID             From/To                    Amount  ");
        System.out.println("--------------------------------------------------");
        Long accountId = getAccountByUserId(currentUser.getUser().getId());
        if (accountId == 0) {
            return;
        } else {
            transfers = restTemplate.exchange(API_BASE_URL + "transfers", HttpMethod.GET, makeAuthEntity(), ViewTransfers[].class).getBody();
            for (ViewTransfers transfer : transfers) {
                if (transfer.getAccountId().equals(getAccountByUserId(currentUser.getUser().getId()))) {
                    System.out.println(transfer.getTransferId() + "          " + transfer.getUser() + "                " + transfer.getAmount());
                }
            }
        }
    }

    public void displayPendingTransfers(AuthenticatedUser currentUser) {
        ViewTransfers[] transfers = null;
        System.out.println("--------------------------------------------------");
        System.out.println("Transfers                                         ");
        System.out.println("ID             From/To                    Amount  ");
        System.out.println("--------------------------------------------------");
        Long accountId = getAccountByUserId(currentUser.getUser().getId());
        transfers = restTemplate.exchange(API_BASE_URL + "transfers/" + accountId, HttpMethod.GET, makeAuthEntity(), ViewTransfers[].class).getBody();
        for (ViewTransfers transfer : transfers) {

            if (transfer.getTransferStatusDesc().equals("Pending")) {
                System.out.println(transfer.getTransferId() + "          " + transfer.getUser() + "                " + transfer.getAmount());
            }
        }
    }

    public void updatePendingTransfer(AuthenticatedUser authenticatedUser) {
        displayPendingTransfers(authenticatedUser);
        Scanner newScanner = new Scanner(System.in);
        ViewTransfers requestedTransfer = null;
        System.out.println("Please enter transfer ID to approve/reject (0 to cancel):");
        Long transferToId = Long.parseLong(newScanner.next());

        if(transferToId == 0L) {
            return;
        }

        requestedTransfer = restTemplate.exchange(API_BASE_URL + "transfers/" + transferToId, HttpMethod.GET, makeAuthEntity(), ViewTransfers.class).getBody();

        if(requestedTransfer.getTransferId() != null) {
            System.out.println("--------------------------");
            System.out.println("1: Approve");
            System.out.println("2: Reject");
            System.out.println("0: Don't approve or reject");
            System.out.println("--------------------------");
            System.out.println("Please choose an option:");

            Integer choice = Integer.parseInt(newScanner.next());

            if (choice == 0) {
                return;
            }


        }
    }

    public String sendMoney(Transfer transfer){
        try {
           String message =  restTemplate.exchange(API_BASE_URL + "transfers", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
            System.out.println(message);
           return message;
        } catch(Exception e){
            System.out.println("This happened: " + e.getMessage() + " and " + e.getCause());
            return "Transfer unsuccessful";
        }
    }

    public void displayTransferInformation(AuthenticatedUser currentUser){
        displayTransfers(currentUser);
        ViewTransfers requestedTransfer = null;
        Scanner userInput = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter ID of transfer to see additional details (0 to cancel)");
        Integer transferId = Integer.parseInt(userInput.next());
        requestedTransfer = restTemplate.exchange(API_BASE_URL + "transfers/details/" + transferId, HttpMethod.GET, makeAuthEntity(), ViewTransfers.class).getBody();

        if(requestedTransfer.getTransferId() != null &&
                (requestedTransfer.getUserTo().equals(currentUser.getUser().getUsername()) ||
                        requestedTransfer.getUserFrom().equals(currentUser.getUser().getUsername()))) {

            System.out.println();
            System.out.println("--------------------------------------------------");
            System.out.println("Transfer Details                                  ");
            System.out.println("--------------------------------------------------");
            System.out.println("Id: " + requestedTransfer.getTransferId());
            System.out.println("From: " + requestedTransfer.getUserFrom());
            System.out.println("To: " + requestedTransfer.getUserTo());
            System.out.println("Type: " + requestedTransfer.getTransferTypeDesc());
            System.out.println("Status: " + requestedTransfer.getTransferStatusDesc());
            System.out.println("Amount: $" + requestedTransfer.getAmount());
        } else {
            System.out.println();
            System.out.println("That is an invalid transfer ID!");
        }
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    private HttpEntity makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }



}
