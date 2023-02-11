package pl.futurecollars.invoicing.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invoice {
  private int id;
  private String buyer;
  private String seller;
  private List<InvoiceEntry> entries;

}
