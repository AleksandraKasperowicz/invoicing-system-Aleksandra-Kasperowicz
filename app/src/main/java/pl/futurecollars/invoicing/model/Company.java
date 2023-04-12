package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

  @ApiModelProperty(value = "id", required = true)
  private String id;

  @ApiModelProperty(value = "tax Identification", required = true, example = "555-444-33-22")
  private String taxIdentificationNumber;

  @ApiModelProperty(value = "company address", required = true, example = "ul. Wojska Polskiego, 99-333 Poznań")
  private String address;

  @ApiModelProperty(value = "Company name", required = true, example = "Invoice House Ltd.")
  private String name;
  @Builder.Default
  @ApiModelProperty(value = "Pension insurance amount", required = true, example = "1328.75")
  private BigDecimal pensionInsurance = BigDecimal.ZERO;

  @Builder.Default
  @ApiModelProperty(value = "Health insurance amount", required = true, example = "458.34")
  private BigDecimal healthInsurance = BigDecimal.ZERO;

}

