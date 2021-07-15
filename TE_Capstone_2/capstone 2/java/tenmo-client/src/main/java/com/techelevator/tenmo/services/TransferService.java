package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

    public static String AUTH_TOKEN = "";

    private static String API_BASE_URL = "";
    private RestTemplate restTemplate = new RestTemplate();
    private UserService userService;

    public TransferService(String url) {
        this.API_BASE_URL = url;
    }

    public void sendMoney(Transfer transfer){
        try {
            String message =  restTemplate.exchange(API_BASE_URL + "transfers", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
            System.out.println(message);
        } catch(Exception e){
            System.out.println("This happened: " + e.getMessage() + " and " + e.getCause());
        }
    }

    public void sendTransfer(AuthenticatedUser currentUser) {
        Scanner s = new Scanner(System.in);
        AccountService accountService = new AccountService(API_BASE_URL);
        UserService userService1 = new UserService(API_BASE_URL);
        Integer transferTypeId = 2;
        Integer transferStatusId = 2;
        userService1.getAllUsers(currentUser);
        Long acctFromId = accountService.getAccountByUserId(currentUser.getUser().getId());
        System.out.println("Enter ID of user you are sending to (0 to cancel): ");
        Integer userToId = Integer.parseInt(s.nextLine());

        Long acctToId = 0L;

        if (userToId == 0) {
            return;
        }else if (userToId != 0) {
            acctToId = accountService.getAccountByUserId(userToId);
        }
        if(acctToId == 0L) {
            return;
        } else if (!acctFromId.equals(acctToId)) {
            System.out.println("Enter amount: ");
            BigDecimal amount = new BigDecimal(s.nextLine());
            Transfer newTransfer = new Transfer();
            newTransfer.setTransferTypeId(transferTypeId);
            newTransfer.setTransferStatusId(transferStatusId);
            newTransfer.setAccountFrom(acctFromId);
            newTransfer.setAccountTo(acctToId);
            newTransfer.setAmount(amount);
            sendMoney(newTransfer);
        } else {
            System.out.println("Invalid ID! Please enter a valid ID to send money.");
        }
    }

    public void requestTransfer(AuthenticatedUser currentUser) {
        Scanner s = new Scanner(System.in);
        AccountService accountService = new AccountService(API_BASE_URL);
        UserService userService1 = new UserService(API_BASE_URL);
        Integer transferTypeId = 1;
        Integer transferStatusId = 1;
        userService1.getAllUsers(currentUser);
        Long acctFromId = accountService.getAccountByUserId(currentUser.getUser().getId());
        System.out.println("Enter ID of user you are requesting money from (0 to cancel): ");
        Integer userToId = Integer.parseInt(s.nextLine());

        Long acctToId = 0L;

        if (userToId == 0) {
            return;
        }else if (userToId != 0) {
            acctToId = accountService.getAccountByUserId(userToId);
        }
        if(acctToId == 0L) {
            return;
        } else if (!acctFromId.equals(acctToId)) {
            System.out.println("Enter amount: ");
            BigDecimal amount = new BigDecimal(s.nextLine());
            Transfer newTransfer = new Transfer();
            newTransfer.setTransferTypeId(transferTypeId);
            newTransfer.setTransferStatusId(transferStatusId);
            newTransfer.setAccountFrom(acctFromId);
            newTransfer.setAccountTo(acctToId);
            newTransfer.setAmount(amount);
            sendMoney(newTransfer);
        } else {
            System.out.println("Invalid ID! Please enter a valid ID to send money.");
        }
    }

//AUTHENTICATION ENTITIES: DO NOT TOUCH THESE_______________________

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
