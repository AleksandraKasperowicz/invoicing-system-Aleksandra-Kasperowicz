package pl.futurecollars.invoicing;

import lombok.Data;
import lombok.NoArgsConstructor;

@org.springframework.context.annotation.Configuration
@NoArgsConstructor
@Data
public class Configuration {

  public static final String DB_PATH = "invoice.json";
  public static final String ID_DB_PATH = "id.txt";

}