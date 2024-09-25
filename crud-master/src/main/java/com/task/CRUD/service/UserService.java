package com.task.CRUD.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.CRUD.entity.User;
import com.task.CRUD.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String createUser(User user) {
        // Process PAN to uppercase
        user.setPanNum(user.getPanNum().toUpperCase());

        // Standardize mobile number format
        String mobNum = user.getMobNum();
        if (mobNum.startsWith("+91")) {
            mobNum = mobNum.substring(3);
        } else if (mobNum.startsWith("0")) {
            mobNum = mobNum.substring(1);
        }
        user.setMobNum(mobNum);

        // Set timestamps
        user.setCreatedAt(LocalDateTime.now().toString());
        user.setUpdatedAt(null);

        // Save user to database
        userRepository.save(user);

        return "User created successfully!";
    }

    public List<User> getUsers(Optional<String> mobNum, Optional<UUID> userId, Optional<UUID> managerId) {
        if (mobNum.isPresent()) {
            return userRepository.findByMobNum(mobNum.get()).map(List::of).orElse(List.of());
        } else if (userId.isPresent()) {
            return userRepository.findById(userId.get()).map(List::of).orElse(List.of());
        } else if (managerId.isPresent()) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getManagerId() != null && user.getManagerId().equals(managerId.get()))
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll();
        }
    }

    public String deleteUser(Optional<String> mobNum) {
            Optional<User> user = userRepository.findByMobNum(mobNum.get());
            if (user.isPresent()) {
                userRepository.delete(user.get());
                return "User deleted successfully!";
            } else {
                return "User with mobile number " + mobNum.get() + " not found!";
            }
    }

    public String updateUser(List<UUID> userIds, Map<String, Object> updateData) {
        // Validate if only manager_id is passed for bulk update
        if (userIds.size() > 1 && updateData.size() > 1) {
            return "Bulk update is only allowed for manager_id. Extra keys found: " + updateData.keySet();
        }

        // Fetch the users to update
        List<User> users = userRepository.findAllById(userIds);

        if (users.isEmpty()) {
            return "No users found with the provided user IDs.";
        }

        for (User user : users) {
            // Handle manager_id updates
            if (updateData.containsKey("manager_id")) {
                UUID newManagerId = UUID.fromString(updateData.get("manager_id").toString());

                // Check if the user already has a manager
                if (user.getManagerId() != null) {
                    user.setIsActive(false); // Deactivate current record
                    userRepository.save(user);

                    // Create a new record for the updated manager
                    User newUser = User.builder()
                            .userId(UUID.randomUUID())
                            .fullName(user.getFullName())
                            .mobNum(user.getMobNum())
                            .panNum(user.getPanNum())
                            .managerId(newManagerId)
                            .createdAt(user.getCreatedAt())
                            .updatedAt(LocalDateTime.now().toString())
                            .isActive(true)
                            .build();
                    userRepository.save(newUser);
                } else {
                    user.setManagerId(newManagerId);
                    user.setUpdatedAt(LocalDateTime.now().toString());
                    userRepository.save(user);
                }
            }

            // Handle other individual updates (e.g., full_name, mob_num, pan_num)
            if (updateData.containsKey("full_name")) {
                user.setFullName(updateData.get("full_name").toString());
            }

            if (updateData.containsKey("mob_num")) {
                String mobNum = updateData.get("mob_num").toString();
                if (mobNum.startsWith("+91")) {
                    mobNum = mobNum.substring(3);
                } else if (mobNum.startsWith("0")) {
                    mobNum = mobNum.substring(1);
                }
                user.setMobNum(mobNum);
            }

            if (updateData.containsKey("pan_num")) {
                user.setPanNum(updateData.get("pan_num").toString().toUpperCase());
            }

            // Update the updated_at timestamp for individual updates
            if (updateData.size() > 1 || !updateData.containsKey("manager_id")) {
                user.setUpdatedAt(LocalDateTime.now().toString());
                userRepository.save(user);
            }
        }

        return "Users updated successfully!";
    }

}
