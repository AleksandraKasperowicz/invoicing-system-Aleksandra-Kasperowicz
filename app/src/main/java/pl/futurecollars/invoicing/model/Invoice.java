package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class Invoice {
  private Long id;
  private LocalDate data;
  private Company buyer;
  private Company seller;
  private List<InvoiceEntry> entries;

  public Invoice(LocalDate data, Company buyer, Company seller, List<InvoiceEntry> entries) {
    this.data = data;
    this.buyer = buyer;
    this.seller = seller;
    this.entries = entries;
  }
}
