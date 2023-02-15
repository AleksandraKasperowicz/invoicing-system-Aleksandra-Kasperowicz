package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pl.futurecollars.invoicing.db.DataBase;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDatabase implements DataBase {

  final Map<Integer, Invoice> invoices = new HashMap<>();
  private int currentId = 0;

  @Override
  public int save(Invoice invoice) {
    invoice.setId(getNextId());
    invoices.put(invoice.getId(), invoice);
    return invoice.getId();
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.ofNullable(invoices.get(id));
  }

  @Override
  public List<Invoice> getAll() {
    return new ArrayList<>(invoices.values());
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {

    if (!invoices.containsKey(id)) {
      throw new IllegalArgumentException("Faktura o numerze: " + id + "nie istnieje");
    }

    updatedInvoice.setId(id);
    invoices.put(id, updatedInvoice);
  }

  @Override
  public void delete(int id) {
    invoices.remove(id);
  }

  private int getNextId() {
    return ++currentId;
  }
}
