package vinci.stock.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vinci.stock.gateway.models.Credentials;

@Repository
@FeignClient(name="authentication")
public interface AuthenticationProxy {
  @PostMapping("/authentication/connect")
  String connect(@RequestBody Credentials credentials);

  @PostMapping("/authentication/verify")
  String verify(@RequestBody String token);

  @PostMapping("/authentication/{username}")
  void updateCredentials(@PathVariable String username, Credentials credentials);
}
