package vinci.stock.gateway;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import vinci.stock.gateway.exceptions.BadRequestException;
import vinci.stock.gateway.exceptions.ConflictException;
import vinci.stock.gateway.exceptions.NotFoundException;
import vinci.stock.gateway.exceptions.UnauthorizedException;
import vinci.stock.gateway.models.Credentials;
import vinci.stock.gateway.models.InvestorData;
import vinci.stock.gateway.models.InvestorWithPassword;
import vinci.stock.gateway.models.Order;
import vinci.stock.gateway.models.Position;

@RestController
public class GatewayController {
  private GatewayService service;

  public GatewayController(GatewayService service) {
    this.service = service;
  }

  /**
   * Get trivial investor data
   * @param username of the investor
   * @return Investor data or 404 if not found
   */
  @GetMapping("/investor/{username}")
  public ResponseEntity<InvestorData> readInvestorInfo(@PathVariable String username){
    try {
      InvestorData investorData = service.readInvestorInfo(username);
      return new ResponseEntity<>(investorData, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch(BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Create an investor in the VSX platform
   * @param investorWithPassword
   * @return 200 if created, 400 if invalid data, 409 if investor already exists
   */
  @PostMapping("/investor/{username}")
  public ResponseEntity<Void> createInvestor(@PathVariable String username, @RequestBody InvestorWithPassword investorWithPassword){
    if (!Objects.equals(investorWithPassword.getInvestorData().getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    try {
      service.createInvestor(username, investorWithPassword);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (ConflictException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Update an Investor's data (password excluded) in the VSX platform
   * @param username of the investor
   * @param investorData
   * @return 200 if updated, 400 if invalid data, 404 if investor not found
   */
  @PutMapping ("/investor/{username}")
  public ResponseEntity<Void> updateInvestor(@PathVariable String username, @RequestBody InvestorData investorData){
    if (!Objects.equals(investorData.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    try {
      service.updateInvestorData(username, investorData);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Delete an investor in the VSX platform, as well as its credentials and its wallet.
   * @param username of the investor
   * @return 200 if credentials deleted,
   *         404 if credentials not found,
   *         400 if the investor can't be deleted (ex: it has stocks in its wallet)
   */
  @DeleteMapping("/investor/{username}")
  public ResponseEntity<Void> deleteInvestor(@PathVariable String username) {
    try {
      service.deleteInvestor(username);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Initiate a session for an investor in the VSX platform
   * @param credentials of the investor
   * @return Connection token or 400 if credentials are invalid, 401 if credentials are incorrect
   */
  @PostMapping("/authentication/connect")
  public ResponseEntity<String> connect(@RequestBody Credentials credentials) {
    try {
      String token = service.connect(credentials);
      return new ResponseEntity<>(token, HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (UnauthorizedException e) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * Update the credentials of an investor in the VSX platform
   * @param username of the investor
   * @param credentials new credentials
   * @return 200 if credentials updated, 400 if invalid data, 404 if credentials not found
   */
  @PutMapping("/authentication/{username}")
  public ResponseEntity<Void> updateCredentials(@PathVariable String username, @RequestBody Credentials credentials) {
    if (!Objects.equals(credentials.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    try {
      service.updateCredentials(username, credentials);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Create an order in the VSX platform
   * @param order Order to create
   * @return 200 if created, 400 if order not valid.
   */
  @PostMapping("/order")
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    try {
      Order orderCreated = service.createOrder(order);
      return new ResponseEntity<>(orderCreated, HttpStatus.OK);
    } catch (BadRequestException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Get orders of an investor
   * @param username of the investor
   * @param token of the investor
   * @return 200 if orders found, 401 if token invalid, 404 if investor not found
   */
  @GetMapping("/order/by-user/{username}")
  public ResponseEntity<Iterable<Order>> readOrdersByInvestor(@PathVariable String username, @RequestHeader("Authorization") String token) {
    String user = service.verify(token);
    if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    try {
      Iterable<Order> orders = service.readOrdersByUser(username);
      return new ResponseEntity<>(orders, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Get all positions of an investor (in its wallet)
   * @param username of the investor
   * @param token of the investor
   * @return 200 if positions found, 401 if token invalid, 404 if investor not found
   */
  @GetMapping("/wallet/{username}")
  public ResponseEntity<Iterable<Position>> readWallet(@PathVariable String username, @RequestHeader("Authorization") String token) {
    String user = service.verify(token);
    if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    try {
      Iterable<Position> wallet = service.readWallet(username);
      return new ResponseEntity<>(wallet, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
    * Add or remove cash from an investor's wallet
    * @param username of the investor
    * @param token of the investor
    * @return 200 if cash added or removed with new positions of investor, 401 if token invalid, 404 if investor not found
    */
  @PostMapping("/wallet/{username}/cash")
  public ResponseEntity<Iterable<Position>> addOrRemoveCash(@PathVariable String username, @RequestHeader String token, @RequestBody int cash){
    String user = service.verify(token);
    if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    try {
      Iterable<Position> wallet = service.addOrRemoveCash(username, cash);
      return new ResponseEntity<>(wallet, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Get the net worth of an investor
   * @param username of the investor
   * @param token of the investor
   * @return 200 if net worth found, 401 if token invalid, 404 if investor not found
   */
  @GetMapping("/wallet/{username}/net-worth")
  public ResponseEntity<Integer> readNetWorth(@PathVariable String username, @RequestHeader String token) {
    String user = service.verify(token);
    if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    try {
      int netWorth = service.readNetWorth(username);
      return new ResponseEntity<>(netWorth, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   *
   * @param username
   * @param ticker
   * @param quantity
   * @param token
   * @return
   */
  @PostMapping("/wallet/{username}/position/{ticker}")
  public ResponseEntity<Iterable<Position>> depositOrWithdrawPosition(@PathVariable String username, @PathVariable String ticker, @RequestBody int quantity, @RequestHeader String token) {
    String user = service.verify(token);
    if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    try {
      Iterable<Position> position = service.depositOrWithdrawPositionFromWallet(username, ticker, quantity);
      return new ResponseEntity<>(position, HttpStatus.OK);
    } catch (NotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}

