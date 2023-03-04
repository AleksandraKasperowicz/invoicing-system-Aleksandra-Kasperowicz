package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.AbstractDatabaseTest
import pl.futurecollars.invoicing.Configuration
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.FileService

import static pl.futurecollars.invoicing.TestInvoice.invoice

class FileDatabaseTest extends AbstractDatabaseTest {

    List<Invoice> invoices = (1..12).collect { invoice(it)}

    def setup() {
    new File(Configuration.DB_PATH).delete()
    new File(Configuration.ID_DB_PATH).delete()
        database = initDatabase()
        invoices.collect({database.save(it)})
    }

    @Override
    Database initDatabase() {
        def fileService = new FileService()
        return new FileDatabase(fileService)
    }


}
