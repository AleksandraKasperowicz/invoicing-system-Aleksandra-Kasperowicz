package pl.futurecollars.invoicing.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface Config {

  String getIdPath();

  String getInvoicePath();
}
