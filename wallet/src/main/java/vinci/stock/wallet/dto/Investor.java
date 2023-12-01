package vinci.stock.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Investor {

    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;

}


