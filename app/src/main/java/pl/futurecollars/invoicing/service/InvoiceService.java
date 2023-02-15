package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.db.DataBase;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {

  private final DataBase database;

  public InvoiceService(DataBase database) {
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

  public List<Invoice> getInvoicesByBuyer(String buyer) {

    List<Invoice> allInvoices = getAll();
    return allInvoices
        .stream()
        .filter(i -> i.getBuyer().equals(buyer))
        .collect(Collectors.toList());
  }

  public List<Invoice> getInvoicesBySeller(String seller) {

    List<Invoice> allInvoices = getAll();
    return allInvoices
        .stream()
        .filter(i -> i.getSeller().equals(seller))
        .collect(Collectors.toList());
  }
}