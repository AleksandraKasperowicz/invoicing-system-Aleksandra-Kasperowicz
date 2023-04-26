package pl.futurecollars.invoicing.sql

import org.flywaydb.core.Flyway
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import pl.futurecollars.invoicing.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice

import javax.sql.DataSource

import static pl.futurecollars.invoicing.TestInvoice.invoice

class SqlDatabaseTest extends AbstractDatabaseTest {

    List<Invoice> invoices = (1..12).collect { invoice(it) }

    def setup() {
        database = initDatabase()
        invoices.collect({ database.save(it) })
    }

    @Override
    Database initDatabase() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)

        Flyway flyway = Flyway.configure().cleanDisabled(false)
                .dataSource(dataSource)
                .locations("db/migration")
                .load()

        flyway.clean()
        flyway.migrate()

        new SqlDatabase(jdbcTemplate)
    }
}
