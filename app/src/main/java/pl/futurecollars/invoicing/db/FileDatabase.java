package pl.futurecollars.invoicing.db;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.configuration.Config;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;

@Repository
@RequiredArgsConstructor

public class FileDatabase implements Database {

  private final FileService fileService;

  @Autowired
  private final Config config;
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
  public void update(long id, Invoice updatedInvoice) {

    List<Invoice> invoicesList = getAll();
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

    fileService.writeDataToFile(config.getInvoicePath(), invoicesList);
  }

  @Override
  public void delete(long id) {
    List<Invoice> invoiceList = getAll();
    Iterator<Invoice> invoiceIterator = invoiceList.iterator();
    while (invoiceIterator.hasNext()) {
      Invoice invoice = invoiceIterator.next();
      if (invoice.getId().equals(id)) {
        invoiceIterator.remove();
      }
      fileService.writeDataToFile(config.getInvoicePath(), invoiceList);
    }

  }

  private long getNextId() {
    fileService.writeDataToFile(config.getIdPath(), ++currentId);
    return currentId;
  }
}