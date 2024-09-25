package com.task.CRUD;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.task.CRUD.entity.User;
import com.task.CRUD.repository.UserRepository;
import com.task.CRUD.service.UserService;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() {
        User user = new User();
        user.setFullName("rasheed");
        user.setMobNum("+910000000000");
        user.setPanNum("zbcdp1234z");

        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.createUser(user);
        assertEquals("User created successfully!", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUsersByMobileNumber() {
        String mobNum = "0000000000";
        User user = new User();
        user.setFullName("rasheed");
        user.setMobNum(mobNum);

        when(userRepository.findByMobNum(mobNum)).thenReturn(Optional.of(user));

        List<User> result = userService.getUsers(Optional.of(mobNum), Optional.empty(), Optional.empty());
        assertFalse(result.isEmpty());
        assertEquals("rasheed", result.get(0).getFullName());
    }
}
