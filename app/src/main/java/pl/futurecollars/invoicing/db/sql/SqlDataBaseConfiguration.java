package pl.futurecollars.invoicing.db.sql;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
public class SqlDataBaseConfiguration {

  @Bean
  public Database<Invoice> invoiceSqlDatabase(JdbcTemplate jdbcTemplate) {
    return new SqlDatabase(jdbcTemplate);
  }

  @Bean
  public Database<Company> companySqlDatabase(JdbcTemplate jdbcTemplate) {
    return new CompanySqlDatabase(jdbcTemplate);
  }
}
