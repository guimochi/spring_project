package vinci.stock.gateway;

import feign.FeignException;
import feign.FeignException.NotFound;
import vinci.stock.gateway.data.AuthenticationProxy;
import vinci.stock.gateway.data.InvestorProxy;
import vinci.stock.gateway.data.OrderProxy;
import vinci.stock.gateway.data.WalletProxy;
import org.springframework.stereotype.Service;
import vinci.stock.gateway.exceptions.BadRequestException;
import vinci.stock.gateway.exceptions.ConflictException;
import vinci.stock.gateway.exceptions.NotFoundException;
import vinci.stock.gateway.exceptions.UnauthorizedException;
import vinci.stock.gateway.models.Credentials;
import vinci.stock.gateway.models.InvestorData;
import vinci.stock.gateway.models.InvestorWithPassword;
import vinci.stock.gateway.models.Order;
import vinci.stock.gateway.models.Position;

@Service
public class GatewayService {

  private final AuthenticationProxy authenticationProxy;
  private final OrderProxy orderProxy;
  private final WalletProxy walletProxy;

  private final InvestorProxy investorProxy;

  public GatewayService(AuthenticationProxy authenticationProxy, OrderProxy orderProxy,
      WalletProxy walletProxy, InvestorProxy investorProxy) {
    this.authenticationProxy = authenticationProxy;
    this.orderProxy = orderProxy;
    this.walletProxy = walletProxy;
    this.investorProxy = investorProxy;
  }

  /**
   * Get connection token from credentials
   * @param credentials Credentials of the user
   * @return Connection token
   * @throws BadRequestException   when the credentials are invalid
   * @throws UnauthorizedException when the credentials are incorrect
   */
  public String connect(Credentials credentials) throws BadRequestException, UnauthorizedException {
    try {
      return authenticationProxy.connect(credentials);
    } catch (FeignException e) {
      if (e.status() == 400) {
        throw new BadRequestException();
      } else if (e.status() == 401) {
        throw new UnauthorizedException();
      } else {
        throw e;
      }
    }
  }

  /**
   * Verifies a connection token
   * @param token Connection token
   * @return true if the token is valid, false otherwise
   */
  public String verify(String token) {
    try {
      return authenticationProxy.verify(token);
    } catch (FeignException e) {
      if (e.status() == 401) {
        return null;
      } else {
        throw e;
      }
    }
  }

  /**
   * Get Investor data
   * @param username of the investor
   * @return Investor data
   * @throws BadRequestException if invalid data
   * @throws NotFoundException if investor not found
   */
  public InvestorData readInvestorInfo(String username) throws BadRequestException, NotFoundException {
    try {
      return investorProxy.readOne(username);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  /**
   * Create an investor in the VSX platform
   * @param username of the investor
   * @param investorWithPassword Investor to create
   * @throws BadRequestException if invalid data
   * @throws ConflictException if investor already exists
   */
  public void createInvestor(String username, InvestorWithPassword investorWithPassword)
      throws BadRequestException, ConflictException {
    try {
      investorProxy.createOne(username, investorWithPassword);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 409) throw new ConflictException();
      else throw e;
    }
  }

  /**
   * Update an investor in the VSX platform
   * @param username of the investor
   * @param investorData Investor to create
   * @throws BadRequestException if invalid data
   * @throws NotFoundException if investor not found
   */
  public void updateInvestorData(String username, InvestorData investorData) throws BadRequestException, NotFoundException {
    try {
      investorProxy.updateOne(username, investorData);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  /**
   * Delete an investor in the VSX platform, as well as its credentials and its wallet.
   * @param username
   * @return true if investor was found and could be deleted, false otherwise
   */
  public void deleteInvestor(String username) throws BadRequestException, NotFoundException {
    try {
      investorProxy.deleteOne(username);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }


  public void updateCredentials(String username, Credentials credentials) throws BadRequestException, NotFoundException {
    try {
      authenticationProxy.updateCredentials(username, credentials);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  public Order createOrder(Order order) throws BadRequestException {
    try {
      return orderProxy.createOne(order);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else throw e;
    }
  }

  public Iterable<Order> readOrdersByUser(String username) throws NotFoundException {

    try {
      return orderProxy.readByUser(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }

  }

  public Iterable<Position> readWallet(String username) throws NotFoundException {
    try {
      return walletProxy.readWallet(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  public Iterable<Position> addOrRemoveCash(String username, int cash) throws NotFoundException {
    Iterable<Position> positions;
    try {
      positions = walletProxy.readWallet(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
    try {
      for (Position position : positions) {
        if (position.getTicker().equals("CASH")) {
          position.setQuantity(position.getQuantity() + cash);
          break;
        }
      }
      return walletProxy.addPositions(username, positions);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }


  }

  public int readNetWorth(String username) throws NotFoundException {
    try {
      return walletProxy.readNetWorth(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }
}
