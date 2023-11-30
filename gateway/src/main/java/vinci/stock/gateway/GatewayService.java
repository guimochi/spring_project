package vinci.stock.gateway;

import feign.FeignException;
import java.util.ArrayList;
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
   * Verifies a connection token. If the token is invalid, an exception is thrown.
   *
   * @param token Connection token
   * @return true if the token is valid and the token is for the given username, false otherwise
   */
  public boolean isAuthorized(String token, String username) {
    try {
      String usernameReceived = authenticationProxy.verify(token);
      return usernameReceived != null && usernameReceived.equals(username);
    } catch (FeignException e) {
      if (e.status() == 401) {
        return false;
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

  /**
   * Update credentials of an investor
   * @param username of the investor
   * @param credentials of the investor
   * @throws BadRequestException if invalid data
   * @throws NotFoundException if investor not found
   */
  public void updateCredentials(String username, Credentials credentials) throws BadRequestException, NotFoundException {
    try {
      authenticationProxy.updateCredentials(username, credentials);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  /**
   * Create an order in the VSX platform
   * @param order to create
   * @return the created order
   * @throws BadRequestException if invalid data
   */
  public Order createOrder(Order order) throws BadRequestException {
    try {
      return orderProxy.createOne(order);
    } catch (FeignException e) {
      if (e.status() == 400) throw new BadRequestException();
      else throw e;
    }
  }

  /**
   * Read all orders of an investor
   * @param username of the investor
   * @return array of orders
   * @throws NotFoundException if order not found
   */
  public Iterable<Order> readOrdersByUser(String username) throws NotFoundException {

    try {
      return orderProxy.readByUser(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }

  }

  /**
   * Read all positions of an investor
   * @param username of the investor
   * @return array of positions
   * @throws NotFoundException if position not found
   */
  public Iterable<Position> readWallet(String username) throws NotFoundException {
    try {
      return walletProxy.readWallet(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  /**
   * Add or remove cash from an investor's wallet
   * @param username of the investor
   * @param cash to add or remove
   * @return updated array of positions
   * @throws NotFoundException if position not found
   */
  public Iterable<Position> addOrRemoveCash(String username, int cash) throws NotFoundException {
    try {
      Position cashPosition = new Position();
      cashPosition.setTicker("CASH");
      cashPosition.setQuantity(cash);
      cashPosition.setUnitValue(1);
      return walletProxy.addPositions(username, new ArrayList<>() {{
        add(cashPosition);
      }});
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }


  }

  /**
   * Read net worth of an investor
   * @param username of the investor
   * @return net worth (int)
   * @throws NotFoundException if position not found
   */
  public int readNetWorth(String username) throws NotFoundException {
    try {
      return walletProxy.readNetWorth(username);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }

  /**
   * Deposit or withdraw a position from an investor's wallet
   * @param username of the investor
   * @param ticker of the position
   * @param quantity of the position
   * @return the position
   * @throws NotFoundException if position not found
   */
  public Iterable<Position> depositOrWithdrawPositionFromWallet(String username, String ticker, int quantity) throws NotFoundException {
    try {
      Iterable<Position> positionToAdd = new ArrayList<>() {{
        add(new Position(ticker, quantity, 0));
      }};
      return walletProxy.addPositions(username, positionToAdd);
    } catch (FeignException e) {
      if (e.status() == 404) throw new NotFoundException();
      else throw e;
    }
  }
}
