package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static String AUTH_TOKEN = "";

    private static String API_BASE_URL = "";

    private RestTemplate restTemplate = new RestTemplate();

    public UserService(String url) {
        this.API_BASE_URL = url;
    }

    public void getAllUsers(AuthenticatedUser currentUser){
        User[] users = null;
        System.out.println("--------------------------------------------------");
        System.out.println("Users                                             ");
        System.out.println("ID             Name                               ");
        System.out.println("--------------------------------------------------");
        try {
            users = restTemplate.exchange(API_BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            for(User user: users) {
                if (!user.getId().equals(currentUser.getUser().getId())) {
                    System.out.println(user.getId() + "         " + user.getUsername());
                }
            }
            System.out.println();
        } catch (Exception e){
            System.out.println("This happened: " + e.getMessage() + " and " + e.getCause());
        }

    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
