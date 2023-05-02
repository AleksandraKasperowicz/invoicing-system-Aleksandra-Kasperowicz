package pl.futurecollars.invoicing.controller.tax;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.tax.CalculatorResult;
import pl.futurecollars.invoicing.service.tax.TaxCalculatorService;

@RestController
@AllArgsConstructor
public class TaxCalculatorController implements TaxCalculatorApi {

  private final TaxCalculatorService calculatorService;

  @Override
  public CalculatorResult calculateTaxes(@RequestBody Company company) {
    return calculatorService.calculateTaxes(company);
  }
}
