package pl.futurecollars.invoicing.controller;

import java.util.List;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@Generated
@RestController
@RequiredArgsConstructor
@RequestMapping("invoices")
public class InvoiceController {

  final InvoiceService invoiceService;

  @GetMapping("/getAll")
  public List<Invoice> getAll() {
    return invoiceService.getAll();
  }

  @PostMapping("/add")
  public long add(@RequestBody Invoice invoice) {
    return invoiceService.save(invoice);
  }

  @GetMapping("/getById/{id}")
  public ResponseEntity<Invoice> getById(@PathVariable long id) {
    return invoiceService.getById(id)
        .map(invoice -> ResponseEntity.ok().body(invoice))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable long id) {
    invoiceService.delete(id);
    return ResponseEntity.ok("ok");
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(@PathVariable long id, @RequestBody Invoice invoice) {
    invoiceService.update(id, invoice);
    return ResponseEntity.ok("ok");
  }
}
