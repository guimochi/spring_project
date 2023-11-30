package vinci.stock.order.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enum for type of order.
 */
@JsonFormat(shape = Shape.STRING)
public enum Type {
  MARKET, LIMIT

}
