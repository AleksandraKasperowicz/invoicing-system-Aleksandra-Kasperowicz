package pl.futurecollars.invoicing.model;

import java.util.List;
import lombok.Data;

@Data
public class Invoice {

  private int id;
  private Company buyer;
  private Company seller;
  private List<InvoiceEntry> entries;

  public Invoice(Company buyer, Company seller, List<InvoiceEntry> entries) {
    this.buyer = buyer;
    this.seller = seller;
    this.entries = entries;
  }
}
