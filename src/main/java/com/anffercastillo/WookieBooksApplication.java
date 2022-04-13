package com.anffercastillo;

import java.util.stream.LongStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.anffercastillo.repositories.UserRepository;

@SpringBootApplication
public class WookieBooksApplication {

  public static void main(String[] args) {
    SpringApplication.run(WookieBooksApplication.class, args);
  }

  // This is for demo purposes only
  @Bean
  CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args ->
        LongStream.range(1, 11)
            .forEach(
                id -> {
                  var user = userRepository.findById(id).get();
                  user.setPassword(passwordEncoder.encode("password123"));
                  userRepository.save(user);
                });
  }
}
