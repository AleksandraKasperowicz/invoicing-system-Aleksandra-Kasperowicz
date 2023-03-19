package pl.futurecollars.invoicing.configuration;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@Primary

public class ConfigTest implements Config {

  private final String invoicePath;
  private final String idPath;

  public ConfigTest() {
    try {
      this.invoicePath = File.createTempFile("tmpInvoices", ".json").getAbsolutePath();
      log.debug("temporary DB file = {}", this.invoicePath);
      log.info("temporary DB file = {}", this.invoicePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      this.idPath = File.createTempFile("tmpTxt", ".txt").getAbsolutePath();
      log.debug("temporary ID file = {}", this.idPath);
      log.info("temporary ID file = {}", this.idPath);
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
