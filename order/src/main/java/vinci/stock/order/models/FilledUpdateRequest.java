package vinci.stock.order.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body for updating filled.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilledUpdateRequest {

  private int filled;

}

