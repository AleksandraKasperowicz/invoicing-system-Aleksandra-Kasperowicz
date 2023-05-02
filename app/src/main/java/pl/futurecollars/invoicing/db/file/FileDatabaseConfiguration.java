package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.ulits.JsonService;

@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
public class FileDatabaseConfiguration {

  @Bean
  public IdProvider idProvider(
      FileService filesService,
      @Value("${invoicing-system.database.directory}") String databaseDirectory,
      @Value("${invoicing-system.database.id.file}") String idFile
  ) throws IOException {
    Path idFilePath = Files.createTempFile(databaseDirectory, idFile);
    return new IdProvider(idFilePath, filesService);
  }

  @Bean
  public Database<Invoice> invoiceFileBasedDatabase(
      IdProvider idProvider,
      FileService filesService,
      JsonService jsonService,
      @Value("${invoicing-system.database.directory}") String databaseDirectory,
      @Value("${invoicing-system.database.invoices.file}") String invoicesFile
  ) throws IOException {
    Path databaseFilePath = Files.createTempFile(databaseDirectory, invoicesFile);
    return new FileDatabase<>(databaseFilePath, idProvider, filesService, jsonService, Invoice.class);
  }

  @Bean
  public Database<Company> companyFileBasedDatabase(
      IdProvider idProvider,
      FileService fileService,
      JsonService jsonService,
      @Value("${invoicing-system.database.directory}") String databaseDirectory,
      @Value("${invoicing-system.database.companies.file}") String companiesFile
  ) throws IOException {
    Path databaseFilePath = Files.createTempFile(databaseDirectory, companiesFile);
    return new FileDatabase<>(databaseFilePath, idProvider, fileService, jsonService, Company.class);
  }

}
