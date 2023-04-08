package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class Invoice {
  @ApiModelProperty(value = "Invoice ID ", required = true, example = "1")
  private Long id;
  @ApiModelProperty(value = "operation date", example = "2023-03-18")
  private LocalDate date;
  @ApiModelProperty(value = "company who bought", required = true)
  private Company buyer;
  @ApiModelProperty(value = "company who is selling", required = true)
  private Company seller;
  @ApiModelProperty(value = "list of products", required = true)
  private List<InvoiceEntry> entries;
}
