package com.task.CRUD;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testCreateUser() throws Exception {
                Map<String, Object> user = new HashMap<>();
                user.put("fullName", "rasheed");
                user.put("mobNum", "+910000000000");
                user.put("panNum", "AABCP1234C");

                mockMvc.perform(post("/api/create_user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("User created successfully!"));
        }

        @Test
        void testGetUsers() throws Exception {
                mockMvc.perform(post("/api/get_users"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0)); // Assuming no users in DB initially
        }

}
