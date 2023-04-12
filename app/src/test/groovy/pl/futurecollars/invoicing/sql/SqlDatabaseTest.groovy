package pl.futurecollars.invoicing.sql

import org.flywaydb.core.Flyway
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import pl.futurecollars.invoicing.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import spock.lang.Specification

import javax.sql.DataSource

class SqlDatabaseTest extends AbstractDatabaseTest {

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

        def database = new SqlDatabase(jdbcTemplate)
        database.initVatRatesMap()

        database
    }
}
