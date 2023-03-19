package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Slf4j
@ConditionalOnProperty(value = "invoicing-system.database.type", havingValue = "inmemory")
@Repository
public class InMemoryDatabase implements Database {

  final Map<Long, Invoice> invoices = new HashMap<>();
  private long currentId = 0;

  @Override
  public long save(Invoice invoice) {
    invoice.setId(getNextId());
    log.debug("save invoice id = {}", invoice.getId());
    log.info("save invoice id = {}", invoice.getId());
    invoices.put(invoice.getId(), invoice);
    log.debug("after DB update invoices.size = {}", invoices.size());
    return invoice.getId();

  }

  @Override
  public Optional<Invoice> getById(long id) {
    log.debug("getById(id = {})", id);
    log.info("getById(id = {})", id);
    return Optional.ofNullable(invoices.get(id));
  }

  @Override
  public List<Invoice> getAll() {
    log.debug("getAll");
    log.info("getAll");
    List<Invoice> allInvoices = new ArrayList<>(invoices.values());
    log.debug("allInvoices.size = {}", allInvoices.size());
    return allInvoices;
  }

  @Override
  public void update(long id, Invoice updatedInvoice) {
    log.debug("update invoice id = {}", id);
    log.info("update invoice id = {}", id);

    if (!invoices.containsKey(id)) {
      log.debug("invoice id = {} doesn't exist", id);
      throw new IllegalArgumentException("Faktura o numerze: " + id + " nie istnieje");
    }

    updatedInvoice.setId(id);
    invoices.put(id, updatedInvoice);
  }

  @Override
  public void delete(long id) {
    log.debug("delete invoice id = {}", id);
    log.info("delete invoice id = {}", id);
    invoices.remove(id);
    log.debug("invoice id = {} deleted", id);

  }

  private long getNextId() {
    return ++currentId;
  }
}
