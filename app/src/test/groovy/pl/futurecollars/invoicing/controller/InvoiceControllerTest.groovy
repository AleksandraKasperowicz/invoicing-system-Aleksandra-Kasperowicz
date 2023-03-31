package pl.futurecollars.invoicing.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestInvoice.invoice


@AutoConfigureMockMvc
@SpringBootTest
class InvoiceControllerTest extends Specification {

    private static final String ENDPOINT = "/invoices"

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id as int) }
    }

    def "should return empty array  when no invoices were added"() {
        expect:
        getAllInvoices() == []
    }

    def "should add five invoices"() {
        given:
        def invoiceAsJson = invoiceAsJson(1)

        expect:
        def firstId = addInvoiceAndReturnId(invoiceAsJson)
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 1
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 2
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 3
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 4
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
        mockMvc.perform(get("$ENDPOINT/getById/$id")).andExpect(status().isNotFound())

        where:
        id << [-15, -5, 0, 1000]
    }

    def "404 is returned when invoice id is not found when deleting invoice [#id]"() {
        given:
        addMultipleInvoices(12)

        expect:
        mockMvc.perform(delete("$ENDPOINT/$id")).andExpect(status().isNotFound())

        where:
        id << [-15, -5, 0, 1000]

    }

    def "404 is returned when invoice id is not found when updating invoice [#id]"() {
        given:
        addMultipleInvoices(12)

        expect:
        mockMvc.perform(put("$ENDPOINT/update/$id")
                .content(invoiceAsJson(1)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())

        where:
        id << [-15, -5, 0, 1000]

    }

    def "invoice data can be modified"() {
        given:
        def id = addInvoiceAndReturnId(invoiceAsJson(111))
        def updatedInvoice = invoice(111)
        updatedInvoice.id = id

        expect:
        mockMvc.perform(
                put("$ENDPOINT/update/$id")
                        .content(objectMapper.writeValueAsString(updatedInvoice))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())

        getInvoiceById(id) == updatedInvoice
    }

    def "invoice can be deleted"() {
        given:
        def invoices = addMultipleInvoices(40)

        expect:
        invoices.each { invoice -> deleteInvoice(invoice.getId() as int)}
        getAllInvoices().size() == 0
    }

    private int addInvoiceAndReturnId(String invoiceAsJson) {
        return mockMvc.perform(post("$ENDPOINT/add").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString as int

    }

    private Invoice getInvoiceById(long id) {
        def invoiceAsString = mockMvc.perform(get("$ENDPOINT/getById/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        objectMapper.readValue(invoiceAsString, Invoice)
    }

    private String invoiceAsJson(int id) {
        objectMapper.writeValueAsString(invoice(id))
    }

    private List<Invoice> addMultipleInvoices(int numberOfInvoices) {
        (1..numberOfInvoices).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(objectMapper.writeValueAsString(invoice))
            return invoice
        }
    }

    private List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get("$ENDPOINT/getAll"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        return objectMapper.readValue(response, Invoice[])
    }

    private ResultActions deleteInvoice(int id) {
        mockMvc.perform(delete("$ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }
}