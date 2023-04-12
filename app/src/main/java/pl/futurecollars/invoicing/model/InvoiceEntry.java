package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class InvoiceEntry {
  @ApiModelProperty(value = "product description", example = "MacPro/ 13'' M2 8 GB")
  private String description;

  @ApiModelProperty(value = "Number of items", required = true, example = "85")
  private int quantity;

  @ApiModelProperty(value = "product net price", example = "12299.99")
  private BigDecimal netPrice;

  @ApiModelProperty(value = "product tax value", required = true)
  @Builder.Default
  private BigDecimal valueVat = BigDecimal.ZERO;

  @ApiModelProperty(value = "Tax rate", required = true)
  private Vat rateVat;

  @ApiModelProperty(value = "Car this expense is related to, empty if expense is not related to car")
  private Car expenseRelatedToCar;
}
