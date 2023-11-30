package vinci.stock.authentication.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnsafeCredentials {
    private String username;
    private String password;

    public SafeCredentials makeSafe(String hashedPassword) {
        return new SafeCredentials(username, hashedPassword);
    }

    public boolean invalid() {
        return username == null || username.isBlank() ||
                password == null || password.isBlank();
    }
}
