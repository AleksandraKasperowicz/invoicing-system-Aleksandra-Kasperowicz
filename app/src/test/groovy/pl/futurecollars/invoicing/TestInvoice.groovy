package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestInvoice {
    static company(id){

        new Company("$id".repeat(10),
                "ul.Wojska Polskiego 102, 60-406 Poznan, Polska",
                "iCode Trust Sp. z o.o")
    }
    static product(int id) {
        new InvoiceEntry("Usluga", BigDecimal.valueOf(id * 1000), BigDecimal.valueOf(id * 1000 * 0.08), Vat.VAT23)
    }
    static invoice(int id) {
        new Invoice(LocalDate.now(), company(id),company(id),List.of(product(id)))

    }
}
