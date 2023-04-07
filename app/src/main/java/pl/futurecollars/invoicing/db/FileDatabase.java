package pl.futurecollars.invoicing.db;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.configuration.AppConfiguration;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;

@Slf4j
@ConditionalOnProperty(value = "invoicing-system.database.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileDatabase implements Database {

  private final FileService fileService;
  private final AppConfiguration config;
  private long currentId;

  @Override
  public long save(Invoice invoice) {

    invoice.setId(getNextId());

    List<Invoice> invoices = getAll();
    invoices.add(invoice);

    log.info("save invoice id = {}", invoice.getId());
    fileService.writeDataToFile(config.getInvoicePath(), invoices);
    log.debug("after DB update invoices.size = {}", invoices.size());

    return invoice.getId();

  }

  @Override
  public Optional<Invoice> getById(long id) {
    log.info("getById(id = {})", id);
    return getAll()
        .stream()
        .filter(invoice -> invoice.getId().equals(id))
        .findFirst();
  }

  @Override
  public List<Invoice> getAll() {
    log.info("getAll");
    List<Invoice> allInvoices = fileService.getDataFromFile(config.getInvoicePath(), Invoice.class);
    log.debug("allInvoices.size = {}", allInvoices.size());
    return allInvoices;

  }

  @Override
  public Optional<Invoice> update(long id, Invoice updatedInvoice) {
    log.info("update invoice id = {}", id);
    List<Invoice> invoicesList = getAll();
    Optional<Invoice> invoiceToBeUpdated = invoicesList
        .stream()
        .filter(invoice -> invoice.getId().equals(id))
        .findFirst();

    if (invoiceToBeUpdated.isPresent()) {
      invoiceToBeUpdated.get().setDate(updatedInvoice.getDate());
      invoiceToBeUpdated.get().setBuyer(updatedInvoice.getBuyer());
      invoiceToBeUpdated.get().setSeller(updatedInvoice.getSeller());
      fileService.writeDataToFile(config.getInvoicePath(), invoicesList);
    }

    return invoiceToBeUpdated;
  }

  @Override

  public Optional<Invoice> delete(long id) {
    log.info("delete invoice id = {}", id);
    Invoice deleteInvoice = null;
    List<Invoice> invoiceList = getAll();
    Iterator<Invoice> invoiceIterator = invoiceList.iterator();
    while (invoiceIterator.hasNext()) {
      Invoice invoice = invoiceIterator.next();
      if (invoice.getId().equals(id)) {
        deleteInvoice = invoice;
        invoiceIterator.remove();
        log.debug("invoice id = {} deleted", id);
      }
      fileService.writeDataToFile(config.getInvoicePath(), invoiceList);
    }
    return Optional.ofNullable(deleteInvoice);
  }

  private long getNextId() {
    fileService.writeDataToFile(config.getIdPath(), ++currentId);
    return currentId;
  }
}