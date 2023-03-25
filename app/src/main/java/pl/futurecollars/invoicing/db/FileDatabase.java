package pl.futurecollars.invoicing.db;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.configuration.AppConfiguration;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;

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

    fileService.writeDataToFile(config.getInvoicePath(), invoices);

    return invoice.getId();

  }

  @Override
  public Optional<Invoice> getById(long id) {
    return getAll()
        .stream()
        .filter(invoice -> invoice.getId().equals(id))
        .findFirst();
  }

  @Override
  public List<Invoice> getAll() {
    return fileService.getDataFromFile(config.getInvoicePath(), Invoice.class);

  }

  @Override
  public Optional<Invoice> update(long id, Invoice updatedInvoice) {
    List<Invoice> invoicesList = getAll();
    try {
      invoicesList
          .stream()
          .filter(invoice -> invoice.getId().equals(id))
          .findFirst()
          .ifPresentOrElse(invoice -> {
            invoice.setData(updatedInvoice.getData());
            invoice.setBuyer(updatedInvoice.getBuyer());
            invoice.setSeller(updatedInvoice.getSeller());
          },
              () -> {
                throw new IllegalArgumentException("Faktura o numerze: " + id + " nie istnieje");
              });
    } catch (IllegalArgumentException illegalArgumentException) {
      return Optional.empty();
    }
    fileService.writeDataToFile(config.getInvoicePath(), invoicesList);
    return getById(updatedInvoice.getId());
  }

  @Override
  public Optional<Invoice> delete(long id) {
    Invoice deleteInvoice = null;
    List<Invoice> invoiceList = getAll();
    Iterator<Invoice> invoiceIterator = invoiceList.iterator();
    while (invoiceIterator.hasNext()) {
      Invoice invoice = invoiceIterator.next();
      if (invoice.getId().equals(id)) {
        deleteInvoice = invoice;
        invoiceIterator.remove();
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