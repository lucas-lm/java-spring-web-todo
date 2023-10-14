package com.lucasm.taskman.filter;

import java.io.IOException;
import java.util.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");
        var authPayloadEncoded = authorizationHeader.replace("Basic ", "").trim();
        byte[] rawAuthPayloadDecoded = Base64.getDecoder().decode(authPayloadEncoded);
        var authPayloadDecoded = new String(rawAuthPayloadDecoded);
        String[] credentials = authPayloadDecoded.split(":");
        String username = credentials[0];
        String password = credentials[1];
        System.out.println(username);
        System.out.println(password);
        filterChain.doFilter(request, response);
  }
  
}
