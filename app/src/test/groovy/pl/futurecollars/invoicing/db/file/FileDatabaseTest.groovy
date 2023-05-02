package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.TestInvoice
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.FileService
import pl.futurecollars.invoicing.ulits.JsonService

import java.nio.file.Files

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

        def tmpFilePath = File.createTempFile('test', '.txt').toPath()

        def tmpIdPath = File.createTempFile("tmpTxt", '.txt').toPath()
        def idService = new IdProvider(tmpIdPath,fileService)

        return  new FileDatabase<>(tmpFilePath,idService,fileService, new JsonService(), Invoice)
    }
}