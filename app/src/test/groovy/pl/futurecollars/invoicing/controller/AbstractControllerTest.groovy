package pl.futurecollars.invoicing.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.service.CalculatorResult
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
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    int addInvoiceAndReturnId(Invoice invoice) {
        mockMvc.perform(post("$INVOICE_ENDPOINT/add").content(objectMapper.writeValueAsString(invoice)).contentType(MediaType.APPLICATION_JSON))
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
            invoice.id = addInvoiceAndReturnId(invoice)
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

    void deleteInvoice(long id) {
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

    CalculatorResult calculateTax(Company company) {
        def response = mockMvc.perform(post("$TAX_CALCULATOR_ENDPOINT").content(objectMapper.writeValueAsString(company)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        objectMapper.readValue(response, CalculatorResult)
    }
}


