package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.Data;

@Data

public class InvoiceEntry {

  private String description;
  private BigDecimal price;
  private BigDecimal valueVat;
  private Vat rateVat;

  public InvoiceEntry(String description, BigDecimal price, BigDecimal valueVat, Vat rateVat) {
    this.description = description;
    this.price = price;
    this.valueVat = valueVat;
    this.rateVat = rateVat;
  }

}
