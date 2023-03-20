package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

  private String id;
  @ApiModelProperty(value = "tax Identification", required = true, example = "555-444-33-22")
  private String taxIdentificationNumber;
  @ApiModelProperty(value = "company address", required = true, example = "ul. Wojska Polskiego, 99-333 Poznań")
  private String address;

  public Company(String id, String taxIdentificationNumber, String address) {
    this.id = id;
    this.taxIdentificationNumber = taxIdentificationNumber;
    this.address = address;
  }
}

