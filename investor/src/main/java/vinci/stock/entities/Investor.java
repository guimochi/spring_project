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
        return username != null && email != null && firstname != null && lastname != null && birthdate != null
                && !username.isEmpty() && !email.isEmpty() && !firstname.isEmpty() && !lastname.isEmpty()
                && !birthdate.toString().isEmpty() && isEmailValid();
    }

    private boolean isEmailValid() {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
