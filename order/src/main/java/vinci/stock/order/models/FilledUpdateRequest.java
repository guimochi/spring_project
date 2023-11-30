package vinci.stock.order.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// format for patch request
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilledUpdateRequest {

  private int filled;

}

