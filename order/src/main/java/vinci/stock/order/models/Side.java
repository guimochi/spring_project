package vinci.stock.order.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enum for side of order.
 */
@JsonFormat(shape = Shape.STRING)
public enum Side {
  BUY,
  SELL
}
