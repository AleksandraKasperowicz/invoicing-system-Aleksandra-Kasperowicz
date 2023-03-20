package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.configuration.ConfigTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.FileDatabase
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import pl.futurecollars.invoicing.service.FileService
import spock.lang.Specification
import java.time.LocalDate

class AppTest extends Specification {

    def "main test"() {
        given:
        def app = new App()
        Database database = new FileDatabase(new FileService(), new ConfigTest())

        when:
        Invoice invoice = Invoice.builder()
                .buyer(new Company("1234", "88888888", "Warszawa"))
                .seller(new Company("4321", "7777777", "Poznań"))
                .data(LocalDate.now())
                .entries(List.of(InvoiceEntry.builder().price(BigDecimal.valueOf(40)).description("programming system").rateVat(Vat.VAT8).valueVat(BigDecimal.valueOf(3.2)).build()))
                .build()

        then:
        database.save(invoice)
        and:
        app.main()
    }
}