package com.lucasm.taskman.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lucasm.taskman.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
        // auth parsing
        var authorizationHeader = request.getHeader("Authorization");
        var authPayloadEncoded = authorizationHeader.replace("Basic ", "").trim();
        byte[] rawAuthPayloadDecoded = Base64.getDecoder().decode(authPayloadEncoded);
        var authPayloadDecoded = new String(rawAuthPayloadDecoded);
        String[] credentials = authPayloadDecoded.split(":");
        String username = credentials[0];
        String password = credentials[1];
        System.out.println(username);
        System.out.println(password);

        // user validation
        var user = userRepository.findByUsername(username);
        if (user == null) {
          response.sendError(401); // user does not exist
        } else {
          var passwordVerification = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
          if (passwordVerification.verified) {
            filterChain.doFilter(request, response);
          } else {
            response.sendError(401);
          }
        }
        System.out.println(user);
  }
  
}
