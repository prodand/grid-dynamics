package com.dmytrop.dto;

import java.util.Date;

public class PriceItem {

  private PriceType type = PriceType.DEFAULT;
  private double amount;
  private Date start;
  private Date end;

  public PriceType getType() {
    return type;
  }

  public PriceItem setType(PriceType type) {
    this.type = type;
    return this;
  }

  public double getAmount() {
    return amount;
  }

  public PriceItem setAmount(double amount) {
    this.amount = amount;
    return this;
  }

  public Date getStart() {
    return start;
  }

  public PriceItem setStart(Date start) {
    this.start = start;
    return this;
  }

  public Date getEnd() {
    return end;
  }

  public PriceItem setEnd(Date end) {
    this.end = end;
    return this;
  }

  @Override
  public String toString() {
    return "PriceItem{" +
        "type=" + type +
        ", amount=" + amount +
        ", start=" + start +
        ", end=" + end +
        '}';
  }
}
