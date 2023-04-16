package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
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

}

