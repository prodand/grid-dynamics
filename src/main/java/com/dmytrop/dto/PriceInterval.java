package com.dmytrop.dto;

import java.util.Date;
import java.util.Objects;

public class PriceInterval {

  private double amount;
  private Date start;
  private Date end;

  public PriceInterval(double amount, Date start, Date end) {
    this.amount = amount;
    this.start = start;
    this.end = end;
  }

  public double getAmount() {
    return amount;
  }

  public Date getStart() {
    return start;
  }

  public Date getEnd() {
    return end;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceInterval that = (PriceInterval) o;
    return Double.compare(that.getAmount(), getAmount()) == 0 &&
        Objects.equals(getStart(), that.getStart()) &&
        Objects.equals(getEnd(), that.getEnd());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAmount(), getStart(), getEnd());
  }

  @Override
  public String toString() {
    return "PriceInterval{" +
        "price=" + amount +
        ", start=" + start +
        ", end=" + end +
        '}';
  }
}
