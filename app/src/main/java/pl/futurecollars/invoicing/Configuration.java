package pl.futurecollars.invoicing;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Configuration {

  public static final String DB_PATH = "invoice.json";
  public static final String ID_DB_PATH = "id.txt";

}