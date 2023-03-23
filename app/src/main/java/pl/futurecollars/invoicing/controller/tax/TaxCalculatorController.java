package pl.futurecollars.invoicing.controller.tax;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.service.CalculateResult;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@RestController
@AllArgsConstructor
public class TaxCalculatorController implements TaxCalculatorApi{

  private final TaxCalculatorService calculatorService;

  @Override
  public CalculateResult calculateTaxes(String taxIdentificationNumber) {
    return calculatorService.calculateTaxes(taxIdentificationNumber);
  }
}
