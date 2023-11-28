package vinci.stock.authentication;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vinci.stock.authentication.models.SafeCredentials;

@Repository
public interface AuthenticationRepository extends CrudRepository<SafeCredentials, String > {
}
