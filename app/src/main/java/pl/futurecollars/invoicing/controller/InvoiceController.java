package pl.futurecollars.invoicing.controller;

import java.util.List;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("invoices")
public class InvoiceController {

  final InvoiceService invoiceService;

  @GetMapping(value = "/getAll", produces = {"application/json;charset=UTF-8"})
  public List<Invoice> getAll() {
    return invoiceService.getAll();
  }

  @PostMapping("/add")
  public long add(@RequestBody Invoice invoice) {
    return invoiceService.save(invoice);
  }

  @GetMapping(value = "/getById/{id}", produces = {"application/json;charset=UTF-8"})
  public ResponseEntity<Invoice> getById(@PathVariable long id) {
    return invoiceService.getById(id)
        .map(invoice -> ResponseEntity.ok().body(invoice))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable long id) {
    return invoiceService.delete(id)
    .map(name -> ResponseEntity.noContent().build()).orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(@PathVariable long id, @RequestBody Invoice invoice) {
    return invoiceService.update(id, invoice)
        .map(name -> ResponseEntity.noContent().build())
        .orElse(ResponseEntity.notFound().build());
  }
}
