package com.example.usercrud.controller;

import com.example.usercrud.entity.User;
import com.example.usercrud.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST API for users:   /api/users
 *
 * The URLs and annotations are already written. The method bodies are the
 * student task. Until you write them, every method answers "501 Not Implemented".
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Spring gives us the repository automatically (constructor injection).
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ------------------------------------------------------------------
    // TASK 1:  POST /api/users        -> create a user, answer 201 Created
    // ------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        // TODO:
        // 1. Save the user with userRepository.save(...)
        // 2. Return status 201 (HttpStatus.CREATED) with the saved user in the body.
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // ------------------------------------------------------------------
    // TASK 2:  GET /api/users         -> list all users, answer 200 OK
    // ------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // TODO:
        // 1. Get all users with userRepository.findAll()
        // 2. Return them with status 200 (ResponseEntity.ok(...)).
        return ResponseEntity.ok(userRepository.findAll());
    }

    // ------------------------------------------------------------------
    // TASK 3:  GET /api/users/{id}    -> one user, or 404 Not Found
    // ------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // TODO:
        // 1. Find the user with userRepository.findById(id)
        // 2. If it exists, return it with status 200.
        // 3. If it does not exist, return status 404 (ResponseEntity.notFound().build()).
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------------------------------------------------------
    // TASK 4:  PUT /api/users/{id}    -> change a user, or 404 Not Found
    // ------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User newData) {
        // TODO:
        // 1. If the user does not exist (userRepository.existsById(id)), return 404.
        // 2. Put the id from the URL into newData: newData.setId(id)
        // 3. Save newData with userRepository.save(...)
        // 4. Return the saved user with status 200.
        boolean existsOrNot = userRepository.existsById(id);
        if(existsOrNot){
            return ResponseEntity.notFound().build();
        }
        newData.setId(id);
        userRepository.save(newData);
        return ResponseEntity.ok(newData);
    }

    // ------------------------------------------------------------------
    // TASK 5:  DELETE /api/users/{id} -> delete a user, answer 204 No Content, or 404
    // ------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // TODO:
        // 1. If the user does not exist, return 404.
        // 2. Delete it with userRepository.deleteById(id)
        // 3. Return status 204 (ResponseEntity.noContent().build()).
        boolean existsOrNot = userRepository.existsById(id);
        if(existsOrNot){
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PROVIDED - the temporary answer used above. Delete it with the TEMPORARY lines if you want.
    private <T> ResponseEntity<T> notImplemented() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
