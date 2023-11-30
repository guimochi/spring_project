package vinci.stock.matching.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enum for the side of the order.
 */
@JsonFormat(shape = Shape.STRING)
public enum Type {
  MARKET, LIMIT

}
