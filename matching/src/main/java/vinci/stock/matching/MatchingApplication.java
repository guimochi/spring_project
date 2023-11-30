package vinci.stock.matching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entry point for the matching service.
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class MatchingApplication {

  public static void main(String[] args) {
    SpringApplication.run(MatchingApplication.class, args);
  }

}
