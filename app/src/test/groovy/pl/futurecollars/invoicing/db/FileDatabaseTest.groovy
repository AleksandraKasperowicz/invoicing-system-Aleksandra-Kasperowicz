package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.AbstractDatabaseTest
import pl.futurecollars.invoicing.configuration.ConfigTest
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.FileService
import static pl.futurecollars.invoicing.TestInvoice.invoice

class FileDatabaseTest extends AbstractDatabaseTest {

    List<Invoice> invoices = (1..12).collect { invoice(it) }

    def setup() {
        database = initDatabase()
        invoices.collect({ database.save(it) })
    }

    @Override
    Database initDatabase() {
        def fileService = new FileService()
        return new FileDatabase(fileService, new ConfigTest())

    }
}