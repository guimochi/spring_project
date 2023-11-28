package vinci.stock.repositories;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vinci.stock.dto.Credentials;

@Repository
@FeignClient(name = "authentication")
public interface AuthentificationProxy {


  @PostMapping("/authentication/{username}")
  void createCredentials(@PathVariable String username, @RequestBody Credentials credentials);

  @DeleteMapping("/authentication/{username}")
  void deleteCredentials(@PathVariable String username);
}
