package vinci.stock.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vinci.stock.entities.Investor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public Investor toInvestor() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new Investor(username, email, firstname, lastname, LocalDate.parse(birthdate, formatter));
    }

}
