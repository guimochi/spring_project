package vinci.stock.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestorWithPassword {
  @JsonProperty("investor_data")
  private InvestorData investorData;
  private String password;
}
