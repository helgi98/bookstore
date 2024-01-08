package org.example.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookstore.configuration.annotations.SpringIntegrationTest;
import org.example.bookstore.dto.JwtTokenDTO;
import org.example.bookstore.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringIntegrationTest
@AutoConfigureMockMvc(addFilters = true)
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signupAndLogin() throws Exception {
        var signupRequest = new UserDTO.SignupUser("user", "password");
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/books?page=0&size=10"))
            .andExpect(status().isUnauthorized());

        var loginRequest = new UserDTO.LoginUser(signupRequest.userName(), signupRequest.password());
        var tokenResponse = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserDTO.LoginUser("user", "password"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andReturn().getResponse().getContentAsString();
        var jwtToken = objectMapper.readValue(tokenResponse, JwtTokenDTO.class);

        mockMvc.perform(get("/books?page=0&size=10")
                .header("Authorization", "Bearer " + jwtToken.token()))
            .andExpect(status().isOk());
    }

}
