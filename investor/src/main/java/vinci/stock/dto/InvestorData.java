package vinci.stock.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.datetime.DateFormatter;
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

    public Optional<Investor> toInvestor() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate parsedBd = LocalDate.parse(this.birthdate, formatter);
            return Optional.of(new Investor(username, email, firstname, lastname, parsedBd));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
