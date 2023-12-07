package com.staxrt.tutorial.repository;

import com.staxrt.tutorial.model.User;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

  public static WebClient webClient =  WebClient.create("http://localhost:9000");

  public Mono<User> getUserById(Long id){
   return  webClient.get()
        .uri("/api/v1/users/{id}", id)
        .retrieve()
        .bodyToMono(User.class)
        .onErrorResume(e->{
          System.out.println(NestedExceptionUtils.getRootCause(e));
          throw new RuntimeException("Resource not found using rest call");
        });
  }

}
