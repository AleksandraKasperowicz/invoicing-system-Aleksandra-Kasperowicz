package pl.futurecollars.invoicing.configuration;

import java.io.File;
import java.io.IOException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
public class ConfigTest implements Config {
  private final String invoicePath;
  private final String idPath;

  public ConfigTest() {
    try {
      this.invoicePath = File.createTempFile("tmpInvoices", ".json").getAbsolutePath();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      this.idPath = File.createTempFile("tmpTxt", ".txt").getAbsolutePath();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getIdPath() {
    return this.idPath;
  }

  @Override
  public String getInvoicePath() {
    return this.invoicePath;
  }
}
