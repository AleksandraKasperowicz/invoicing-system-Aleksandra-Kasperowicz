package pl.futurecollars.invoicing.controller

import org.springframework.http.MediaType

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestInvoice.invoice

class InvoiceControllerTest extends AbstractControllerTest {

    def "should return empty array  when no invoices were added"() {
        expect:
        getAllInvoices() == []
    }

    def "should add five invoices"() {

        expect:
        def firstId = addInvoiceAndReturnId(invoice(1))
        addInvoiceAndReturnId(invoice(2)) == firstId + 1
        addInvoiceAndReturnId(invoice(3)) == firstId + 2
        addInvoiceAndReturnId(invoice(4)) == firstId + 3
        addInvoiceAndReturnId(invoice(5)) == firstId + 4
    }

    def "all invoices are returned when getting all invoices"() {
        given:
        def numberOfInvoices = 4
        def expectedInvoices = addMultipleInvoices(numberOfInvoices)

        when:
        def invoices = getAllInvoices()

        then:
        invoices.size() == numberOfInvoices
        invoices == expectedInvoices
    }

    def "404 is returned when invoice id is not found when getting invoice by id [#id]"() {
        given:
        addMultipleInvoices(12)

        expect:
        mockMvc.perform(get("$INVOICE_ENDPOINT/getById/$id")).andExpect(status().isNotFound())

        where:
        id << [-15, -5, 0, 1000]
    }

    def "404 is returned when invoice id is not found when deleting invoice [#id]"() {
        given:
        addMultipleInvoices(12)

        expect:
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id")).andExpect(status().isNotFound())

        where:
        id << [-15, -5, 0, 1000]

    }

    def "404 is returned when invoice id is not found when updating invoice [#id]"() {
        given:
        addMultipleInvoices(12)

        expect:
        mockMvc.perform(put("$INVOICE_ENDPOINT/update/$id").content(invoiceAsJson(1)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())

        where:
        id << [-15, -5, 0, 1000]

    }

    def "invoice data can be modified"() {
        given:
        def id = addInvoiceAndReturnId(invoice(111))
        def updatedInvoice = invoice(111)
        updatedInvoice.id = id

        expect:
        mockMvc.perform(
                put("$INVOICE_ENDPOINT/update/$id")
                        .content(objectMapper.writeValueAsString(updatedInvoice))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())

        getInvoiceById(id) == updatedInvoice
    }

    def "invoice can be deleted"() {
        given:
        def invoices = addMultipleInvoices(40)

        expect:
        invoices.each { invoice -> deleteInvoice(invoice.getId() as int) }
        getAllInvoices().size() == 0
    }
}