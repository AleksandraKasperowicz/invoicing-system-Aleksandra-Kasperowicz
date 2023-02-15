package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

class TestInvoice {
    static company(id){

        new Company("$id".repeat(10),
                "ul.Wojska Polskiego 102, 60-406 Poznań, Polska",
                "iCode Trust Sp. z o.o")
    }
    static product(int id) {
        new InvoiceEntry("Usługa", BigDecimal.valueOf(id * 1000), BigDecimal.valueOf(id * 1000 * 0.08), Vat.VAT23)
    }

    static invoice(int id) {
        new Invoice(company(id),company(id),List.of(product(id)))
    }
}
