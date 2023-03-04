package pl.futurecollars.invoicing.ulits

import pl.futurecollars.invoicing.TestInvoice
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class JsonServiceTest extends Specification {

    JsonService service;
    def "should convert to json and read file"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestInvoice.invoice(1)

        when:
        def invoiceAsString = jsonService.convertToJson(invoice)

        and:
        def invoiceFromJson = jsonService.convertToObject(invoiceAsString, Invoice)

        then:
        invoice == invoiceFromJson
    }
}