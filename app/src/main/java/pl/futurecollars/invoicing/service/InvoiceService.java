package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Service
@RequiredArgsConstructor
public class InvoiceService {
  private final Database database;

  public long save(Invoice invoice) {
    return database.save(invoice);
  }

  public Optional<Invoice> getById(long id) {
    return database.getById(id);
  }

  public List<Invoice> getAll() {
    return database.getAll();
  }

  public Optional<Invoice> update(long id, Invoice updatedInvoice) {
    return database.update(id, updatedInvoice);
  }

  public Optional<Invoice> delete(long id) {
    return database.delete(id);
  }

  public List<Invoice> getInvoicesByBuyerId(String id) {

    List<Invoice> allInvoices = getAll();
    return allInvoices
        .stream()
        .filter(i -> i.getBuyer().getId().equals(id))
        .collect(Collectors.toList());
  }

  public List<Invoice> getInvoicesBySellerId(String id) {

    List<Invoice> allInvoices = getAll();
    return allInvoices
        .stream()
        .filter(i -> i.getSeller().getId().equals(id))
        .collect(Collectors.toList());
  }
}