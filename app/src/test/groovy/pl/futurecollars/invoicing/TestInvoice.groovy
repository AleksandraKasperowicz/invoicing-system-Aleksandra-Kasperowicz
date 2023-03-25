package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestInvoice {

    static company(int id) {
        Company.builder()
                .id("$id")
                .taxIdentificationNumber("$id")
                .address("ul. Wojska Polskiego/$id 02-703 Warszawa, Polska, iCode Trust $id Sp. z o.o")
                .build()
    }

    static product(int id) {
        InvoiceEntry.builder()
                .description("Programming course $id")
                .price(BigDecimal.valueOf(id * 1000))
                .valueVat(BigDecimal.valueOf(id * 1000 * 0.08))
                .rateVat(Vat.VAT8)
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .date(LocalDate.now())
                .buyer(company(id + 10))
                .seller(company(id))
                .entries((1..id).collect({ product(it) }))
                .build()
    }
}
