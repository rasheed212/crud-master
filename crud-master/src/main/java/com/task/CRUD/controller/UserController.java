package com.task.CRUD.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task.CRUD.entity.User;
import com.task.CRUD.service.UserService;

import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create_user")
    public ResponseEntity<String> createUser(@RequestBody @Valid User user) {
        String result = userService.createUser(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/get_users")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) Optional<String> mobNum,
            @RequestParam(required = false) Optional<UUID> userId,
            @RequestParam(required = false) Optional<UUID> managerId) {

        List<User> users = userService.getUsers(mobNum, userId, managerId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/delete_user")
    public ResponseEntity<String> deleteUser(@RequestParam(required = false) Optional<String> mobNum) {

        String result = userService.deleteUser(mobNum);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update_user")
    public ResponseEntity<String> updateUser(
            @RequestBody Map<String, Object> requestBody) {

        List<UUID> userIds = (List<UUID>) requestBody.get("user_ids");
        Map<String, Object> updateData = (Map<String, Object>) requestBody.get("update_data");

        String result = userService.updateUser(userIds, updateData);
        return ResponseEntity.ok(result);
    }
}
