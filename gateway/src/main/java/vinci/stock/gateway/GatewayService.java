package vinci.stock.gateway;

import feign.FeignException;
import vinci.stock.gateway.data.AuthenticationProxy;
import vinci.stock.gateway.data.OrderProxy;
import vinci.stock.gateway.data.WalletProxy;
import org.springframework.stereotype.Service;
import vinci.stock.gateway.exceptions.BadRequestException;
import vinci.stock.gateway.exceptions.UnauthorizedException;
import vinci.stock.gateway.models.Credentials;

@Service
public class GatewayService {
  private final AuthenticationProxy authenticationProxy;
  private final OrderProxy orderProxy;
  private final WalletProxy walletProxy;


  public GatewayService(AuthenticationProxy authenticationProxy, OrderProxy orderProxy, WalletProxy walletProxy) {
    this.authenticationProxy = authenticationProxy;
    this.orderProxy = orderProxy;
    this.walletProxy = walletProxy;
  }

  /**
   * Get connection token from credentials
   *
   * @param credentials Credentials of the user
   * @return Connection token
   * @throws BadRequestException when the credentials are invalid
   * @throws UnauthorizedException when the credentials are incorrect
   */
  public String connect(Credentials credentials) throws BadRequestException, UnauthorizedException {
    try {
      return authenticationProxy.connect(credentials);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 401) throw new UnauthorizedException();
      else throw e;
    }
  }

  /**
   * Get user pseudo from connection token
   *
   * @param token Connection token
   * @return User pseudo, or null if token invalid
   */
  public String verify(String token) {
    try {
      return authenticationProxy.verify(token);
    } catch (FeignException e) {
      if (e.status() == 401) return null;
      else throw e;
    }
  }
}
