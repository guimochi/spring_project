package vinci.stock.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "investor")
public class Investor {

    @Id
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private LocalDate birthdate;


    public boolean isValid() {
        return  isEmailValid() && isUsernameValid() && isFirstnameValid()
                && isLastnameValid() && isBirthdateValid();
    }

    private boolean isUsernameValid() {
        return username != null && !username.isEmpty()
                && username.matches("^[a-zA-Z0-9_]{3,30}$");
    }
    private boolean isEmailValid() {
        return email != null && !email.isEmpty()
                && email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    }
    private boolean isFirstnameValid() {
        return firstname != null && !firstname.isEmpty()
                && firstname.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isLastnameValid() {
        return lastname != null && !lastname.isEmpty()
                && lastname.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isBirthdateValid() {
        return birthdate != null && !birthdate.isBefore(LocalDate.of(1900, 1, 1))
                && !birthdate.isAfter(LocalDate.now());
    }
}
