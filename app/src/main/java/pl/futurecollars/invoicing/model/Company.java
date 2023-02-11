package pl.futurecollars.invoicing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {
  private int id;
  private String taxIdentificationNumber;
  private String address;
}
