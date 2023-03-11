package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class InvoiceEntry {

  private String description;
  private BigDecimal price;
  private BigDecimal valueVat;
  private Vat rateVat;

}
