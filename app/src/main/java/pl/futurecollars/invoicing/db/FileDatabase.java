package pl.futurecollars.invoicing.db;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.Configuration;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;

public class FileDatabase implements Database {

  FileService fileService;
  long currentId;

  public FileDatabase(FileService fileService) {
    currentId = fileService.readLastIdFromDb(Configuration.ID_DB_PATH);
    this.fileService = fileService;
  }

  @Override
  public long save(Invoice invoice) {

    invoice.setId(getNextId());

    List<Invoice> invoices = getAll();
    invoices.add(invoice);

    fileService.writeDataToFile(Configuration.DB_PATH, invoices);

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
    return fileService.getDataFromFile(Configuration.DB_PATH, Invoice.class);

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

    fileService.writeDataToFile(Configuration.DB_PATH, invoicesList);
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
      fileService.writeDataToFile(Configuration.DB_PATH, invoiceList);
    }

  }

  private long getNextId() {
    fileService.writeDataToFile("id.txt", currentId + 1);
    return ++currentId;
  }
}