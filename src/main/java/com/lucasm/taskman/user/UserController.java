package com.lucasm.taskman.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity<?> create(@RequestBody UserModel user) {
    var foundUser = this.userRepository.findByUsername(user.getUsername());

    if (foundUser != null) {
      return ResponseEntity.badRequest().body("User already exists");
    }

    var hashedPW = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
    user.setPassword(hashedPW);

    var newUser = this.userRepository.save(user);
    return ResponseEntity.ok(newUser);
  }
}
