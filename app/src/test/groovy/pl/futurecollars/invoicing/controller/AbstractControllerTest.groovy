package pl.futurecollars.invoicing.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.CalculateResult
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestInvoice.invoice

@AutoConfigureMockMvc
@SpringBootTest
class AbstractControllerTest extends Specification {

    static final String INVOICE_ENDPOINT = "/invoices"
    static final String TAX_CALCULATOR_ENDPOINT = "/tax"

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id as int) }
    }

    int addInvoiceAndReturnId(String invoiceAsJson) {
        mockMvc.perform(post("$INVOICE_ENDPOINT/add").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString as int

    }

    Invoice getInvoiceById(long id) {
        def invoiceAsString = mockMvc.perform(get("$INVOICE_ENDPOINT/getById/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        objectMapper.readValue(invoiceAsString, Invoice)
    }

    String invoiceAsJson(int id) {
        objectMapper.writeValueAsString(invoice(id))
    }

    List<Invoice> addMultipleInvoices(int numberOfInvoices) {
        (1..numberOfInvoices).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(objectMapper.writeValueAsString(invoice))
            return invoice
        }
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get("$INVOICE_ENDPOINT/getAll"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        objectMapper.readValue(response, Invoice[])
    }

    ResultActions deleteInvoice(int id) {
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id"))
                .andExpect(status().isOk())
    }

    CalculateResult calculateTax(String taxIdentificationNumber) {
        def response = mockMvc.perform(get("$TAX_CALCULATOR_ENDPOINT/$taxIdentificationNumber"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        objectMapper.readValue(response, CalculateResult)
    }
}


