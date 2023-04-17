package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Car
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestInvoice {

    static company(long id) {
        Company.builder()
                .name("$id")
                .taxIdentificationNumber("$id")
                .address("ul. Wojska Polskiego/$id 02-703 Warszawa, Polska, iCode Trust $id Sp. z o.o")
                .pensionInsurance(BigDecimal.TEN * BigDecimal.valueOf(id).setScale(2))
                .healthInsurance(BigDecimal.valueOf(100) * BigDecimal.valueOf(id).setScale(2))
                .build()
    }

    static product(long id) {
        InvoiceEntry.builder()
                .description("Programming course $id")
                .quantity(22.00)
                .netPrice(BigDecimal.valueOf(id * 1000).setScale(2))
                .vatValue(BigDecimal.valueOf(id * 1000 * 0.08).setScale(2))
                .vatRate(Vat.VAT8)
                .expenseRelatedToCar(id % 2 == 0 ? null :
                        Car.builder()
                                .registrationNumber("XXX")
                                .personalUse(false)
                                .build())
                .build()
    }

    static invoice(long id) {
        Invoice.builder()
                .number(String.valueOf(id))
                .date(LocalDate.now())
                .buyer(company(id + 10))
                .seller(company(id))
                .entries((1..id).collect({ product(it) }))
                .build()
    }
}
