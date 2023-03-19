package pl.futurecollars.invoicing.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor

public class ConfigApp implements Config {

  @Value("${invoicing-system.database.dbPath}")
  private String dbPath;

  @Value("${invoicing-system.database.idDbPath}")
  private String idDbPath = "id.txt";

  @Override
  public String getIdPath() {
    return idDbPath;
  }

  @Override
  public String getInvoicePath() {
    return dbPath;
  }
}
