package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public User[] listAllUsers() {
        List<User> allUsers = userDao.listAllUsers();
        User[] userArray = new User[allUsers.size()];
        userArray = allUsers.toArray(userArray);
        return userArray;
    }
}
