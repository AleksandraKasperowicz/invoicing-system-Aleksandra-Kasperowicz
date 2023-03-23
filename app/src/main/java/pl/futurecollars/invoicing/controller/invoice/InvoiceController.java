package pl.futurecollars.invoicing.controller.invoice;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RestController
@AllArgsConstructor
public class InvoiceController implements InvoiceApi {

  final InvoiceService invoiceService;

  @Override
  public List<Invoice> getAll() {
    return invoiceService.getAll();
  }

  @Override
  public long add(@RequestBody Invoice invoice) {
    return invoiceService.save(invoice);
  }

  @Override
  public ResponseEntity<Invoice> getById(@PathVariable long id) {
    return invoiceService.getById(id)
        .map(invoice -> ResponseEntity.ok().body(invoice))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<?> deleteById(@PathVariable long id) {
    if (invoiceService.getById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    invoiceService.delete(id);
    return ResponseEntity.ok("ok");
  }

  @Override
  public ResponseEntity<?> update(@PathVariable long id, @RequestBody Invoice invoice) {
    if (invoiceService.getById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    invoiceService.update(id, invoice);
    return ResponseEntity.ok("ok");
  }
}
