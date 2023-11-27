package vinci.stock.gateway.models;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestorData {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private Date birthDate;
}
