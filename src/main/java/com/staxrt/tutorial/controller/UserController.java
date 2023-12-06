package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.exception.ErrorResponse;
import com.staxrt.tutorial.exception.ResourceNotFoundException;
import com.staxrt.tutorial.model.User;
import com.staxrt.tutorial.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

  @Autowired
  private UserRepository userRepository;

  /**
   * Get all users flux.
   *
   * @return the flux
   */
  @GetMapping
  public Flux<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Gets users by id.
   *
   * @param userId the user id
   * @return the users by id mono
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<User>> getUsersById(@PathVariable(value = "id") Long userId) {
    return userRepository.findById(userId)
        .map(user -> ResponseEntity.ok(user))
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found on :: " + userId)));
  }

  /**
   * Create user mono.
   *
   * @param user the user
   * @return the mono
   */
  @PostMapping
  public Mono<User> createUser(@Validated @RequestBody User user) {
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(user)
        .onErrorResume(e -> {
          System.out.println(NestedExceptionUtils.getRootCause(e));
          return Mono.error(e);
        });
  }

  /**
   * Update user mono.
   *
   * @param userId       the user id
   * @param userDetails the user details
   * @return the mono
   */
  @PutMapping("/{id}")
  public Mono<ResponseEntity<User>> updateUser(
      @PathVariable(value = "id") Long userId, @Validated @RequestBody User userDetails) {

    return userRepository.findById(userId)
        .flatMap(user -> {
          user.setEmail(userDetails.getEmail());
          user.setLastName(userDetails.getLastName());
          user.setFirstName(userDetails.getFirstName());
          user.setUpdatedAt(LocalDateTime.now());
          return userRepository.save(user);
        })
        .map(updatedUser -> ResponseEntity.ok(updatedUser))
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found on :: " + userId)));
  }

  /**
   * Delete user mono.
   *
   * @param userId the user id
   * @return the mono
   */
  @DeleteMapping("/{id}")
  public Mono<Map<String, Boolean>> deleteUser(@PathVariable(value = "id") Long userId) {
    return userRepository.findById(userId)
        .flatMap(user -> userRepository.delete(user).then(Mono.just(createResponseMap(true))))
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found on :: " + userId)));
  }

  private Map<String, Boolean> createResponseMap(boolean deleted) {
    Map<String, Boolean> responseMap = new HashMap<>();
    responseMap.put("deleted", deleted);
    return responseMap;
  }
}
