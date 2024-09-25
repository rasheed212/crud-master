package com.task.CRUD.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.CRUD.entity.User;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByMobNum(String mobNum);
    List<User> findByManagerId(UUID managerId);
}
