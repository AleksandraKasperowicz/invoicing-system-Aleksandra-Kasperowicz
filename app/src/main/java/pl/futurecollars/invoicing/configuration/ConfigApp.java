package pl.futurecollars.invoicing.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
public class ConfigApp implements Config {

  public static final String DB_PATH = "invoice.json";
  public static final String ID_DB_PATH = "id.txt";

  @Override
  public String getIdPath() {
    return ID_DB_PATH;
  }

  @Override
  public String getInvoicePath() {
    return DB_PATH;
  }
}
