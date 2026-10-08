package com.example.usercrud;

import com.example.usercrud.entity.User;
import com.example.usercrud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Checks the student's UserController. Run this class (or `mvn test`).
 * Green = the task is done. Each test is one endpoint behavior.
 *
 * PROVIDED CODE - students do not need to change it.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    private static final String JOHN_JSON =
            "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"email\":\"john@test.com\",\"age\":25}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User saveJohn() {
        return userRepository.save(new User("John", "Smith", "john@test.com", 25));
    }

    @Test
    void createUser_returns201AndSavesInDatabase() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JOHN_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("John"));

        org.junit.jupiter.api.Assertions.assertEquals(1, userRepository.count());
    }

    @Test
    void createUser_withBadEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Bad\",\"lastName\":\"Email\",\"email\":\"nope\",\"age\":20}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_returnsTheList() throws Exception {
        saveJohn();
        userRepository.save(new User("Anna", "Brown", "anna@test.com", 31));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getUserById_returnsTheUser() throws Exception {
        User john = saveJohn();

        mockMvc.perform(get("/api/users/" + john.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    void getUserById_whenMissing_returns404() throws Exception {
        mockMvc.perform(get("/api/users/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_changesTheData() throws Exception {
        User john = saveJohn();

        mockMvc.perform(put("/api/users/" + john.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Johnny\",\"lastName\":\"Smith\",\"email\":\"john@test.com\",\"age\":26}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(john.getId()))
                .andExpect(jsonPath("$.firstName").value("Johnny"))
                .andExpect(jsonPath("$.age").value(26));

        org.junit.jupiter.api.Assertions.assertEquals(1, userRepository.count());
    }

    @Test
    void updateUser_whenMissing_returns404() throws Exception {
        mockMvc.perform(put("/api/users/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JOHN_JSON))
                .andExpect(status().isNotFound());

        org.junit.jupiter.api.Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    void deleteUser_returns204AndRemovesIt() throws Exception {
        User john = saveJohn();

        mockMvc.perform(delete("/api/users/" + john.getId()))
                .andExpect(status().isNoContent());

        org.junit.jupiter.api.Assertions.assertFalse(userRepository.existsById(john.getId()));
    }

    @Test
    void deleteUser_whenMissing_returns404() throws Exception {
        mockMvc.perform(delete("/api/users/999999"))
                .andExpect(status().isNotFound());
    }
}
