package pl.futurecollars.invoicing.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum Vat {
  VAT23(23),
  VAT8(8),
  VAT5(5),
  VAT0(0);

  final Integer rate;
  Vat(int rate) {
    this.rate = rate;
  }
}
