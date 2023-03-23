package pl.futurecollars.invoicing.controller.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping("invoices")
@Api(tags = {"invoice-controller"})
public interface InvoiceApi {

  @ApiOperation(value = "API to list all invoices in the system")
  @GetMapping("/getAll")
  List<Invoice> getAll();

  @PostMapping("/add")
  long add(@RequestBody Invoice invoice);

  @ApiOperation(value = "API to get invoice by ID")
  @ApiResponses(value = {
      @ApiResponse(code = 404, message = "Invoice not found")})
  @GetMapping("/getById/{id}")
  ResponseEntity<Invoice> getById(@PathVariable long id);

  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteById(@PathVariable long id);

  @PutMapping("/update/{id}")
  ResponseEntity<?> update(@PathVariable long id, @RequestBody Invoice invoice);
}
