package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.Invoice;

public interface Database {

  long save(Invoice invoice);

  Optional<Invoice> getById(long id);

  List<Invoice> getAll();

  Optional<Invoice> update(long id, Invoice updatedInvoice);

  Optional<Invoice> delete(long id);

}
