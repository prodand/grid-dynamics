package com.dmytrop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:main.properties")
public class GridDynamicsApplication {

  public static void main(String[] args) {
    SpringApplication.run(GridDynamicsApplication.class, args);
  }
}
