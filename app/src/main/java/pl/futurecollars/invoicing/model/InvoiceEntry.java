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

  @ApiModelProperty(value = "product net price", example = "12299.99")
  private BigDecimal price;

  @ApiModelProperty(value = "product tax value", required = true)
  private BigDecimal valueVat;

  @ApiModelProperty(value = "Tax rate", required = true)
  private Vat rateVat;

}
