package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {

  private final Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  public int save(Invoice invoice) {
    return database.save(invoice);
  }

  public Optional<Invoice> getById(int id) {
    return database.getById(id);
  }

  public List<Invoice> getAll() {
    return database.getAll();
  }

  public void update(int id, Invoice updatedInvoice) {
    database.update(id, updatedInvoice);
  }

  public void delete(int id) {
    database.delete(id);
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