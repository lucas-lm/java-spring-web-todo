package com.lucasm.taskman.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel user) {
    var foundUser = this.userRepository.findByUsername(user.getUsername());

    if (foundUser != null) {
      System.out.println("User already exists.");
      return ResponseEntity.badRequest().body("User already exists");
    }

    var newUser = this.userRepository.save(user);
    return ResponseEntity.ok(newUser);
  }
}
