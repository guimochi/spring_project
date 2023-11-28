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
import org.springframework.web.bind.annotation.RestController;
import vinci.stock.gateway.exceptions.BadRequestException;
import vinci.stock.gateway.exceptions.ConflictException;
import vinci.stock.gateway.exceptions.NotFoundException;
import vinci.stock.gateway.exceptions.UnauthorizedException;
import vinci.stock.gateway.models.Credentials;
import vinci.stock.gateway.models.InvestorData;
import vinci.stock.gateway.models.InvestorWithPassword;

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
    if (!Objects.equals(investorWithPassword.getUsername(), username)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
   * Update an investor in the VSX platform
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
}

