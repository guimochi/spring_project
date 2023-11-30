package vinci.stock.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vinci.stock.entities.Investor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
     * @return true if the data is valid, false otherwise (regex)
     * -> should use annotation validation instead of regex for more flexibility and clarity
     */
    public boolean valid(LocalDate birthdate) {
        return isEmailValid() && isUsernameValid() && isFirstnameValid()
                && isLastnameValid() && isBirthdateValid(birthdate);
    }

    /**
     * @return true if the username is valid, false otherwise (regex) (1-30 characters) (alphanumeric)
     */
    private boolean isUsernameValid() {
        return username.matches("^[a-zA-Z0-9_]{1,30}$");
    }

    /**
     * @return true if the email is valid, false otherwise (regex) (alphanumeric) (max 50 characters) (must contain @)
     * (must contain .) (must contain at least one character before and after @)
     */
    private boolean isEmailValid() {
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }


    /**
     * @return true if the firstname is valid, false otherwise (regex) (3-30 characters) (alphanumeric)
     */
    private boolean isFirstnameValid() {

        return firstname.matches("^[a-zA-Z0-9_]{1,30}$");
    }

    /**
     * @return true if the lastname is valid, false otherwise (regex) (3-30 characters) (alphanumeric)
     */
    private boolean isLastnameValid() {
        return lastname.matches("^[a-zA-Z0-9_]{1,30}$");
    }

    /**
     * @param birthdate the birthdate to check, must be in the format dd/MM/yyyy and be between 1900 and now
     * @return true if the birthdate is valid, false otherwise
     */
    private boolean isBirthdateValid(LocalDate birthdate) {
        return !birthdate.isBefore(LocalDate.of(1900, 1, 1))
                && !birthdate.isAfter(LocalDate.now());
    }


}
