package vinci.stock.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vinci.stock.entities.Investor;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InvestorData {

  private String username;
  private String email;
  private String firstname;
  private String lastname;
  private String birthdate;

  /**
   * @return an Optional of Investor if the data is valid, otherwise an empty Optional
   */
  public Optional<Investor> toInvestor() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try {
      LocalDate parsedBd = LocalDate.parse(this.birthdate, formatter);
      if (!this.valid(parsedBd)) {
        return Optional.empty();
      }

      Investor investor = new Investor(username, email, firstname, lastname, parsedBd);
      if (!investor.valid()) {
        return Optional.empty();
      }
      return Optional.of(new Investor(username, email, firstname, lastname, parsedBd));
    } catch (Exception e) {
      return Optional.empty();
    }
  }


  /**
   * check if the client data is valid
   *
   * @param birthdate the birthdate to check
   * @return true if the data is valid, false otherwise
   */
  public boolean valid(LocalDate birthdate) {
    return isEmailValid() && isUsernameValid() && isFirstnameValid()
        && isLastnameValid() && isBirthdateValid(birthdate);
  }

  private boolean isUsernameValid() {
    return username.matches("^[a-zA-Z0-9_]{3,30}$");
  }

  private boolean isEmailValid() {
    return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
  }

  private boolean isFirstnameValid() {

    return firstname.matches("^[a-zA-Z0-9_]{3,20}$");
  }

  private boolean isLastnameValid() {
    return lastname.matches("^[a-zA-Z0-9_]{3,20}$");
  }

  private boolean isBirthdateValid(LocalDate birthdate) {
    return !birthdate.isBefore(LocalDate.of(1900, 1, 1))
        && !birthdate.isAfter(LocalDate.now());
  }


}
