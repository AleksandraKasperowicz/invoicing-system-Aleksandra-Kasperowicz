package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.tax.CalculatorResult
import pl.futurecollars.invoicing.ulits.JsonService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestInvoice.invoice
import static pl.futurecollars.invoicing.TestInvoice.company

@AutoConfigureMockMvc
@SpringBootTest
class AbstractControllerTest extends Specification {

    static final String INVOICE_ENDPOINT = "/invoices"
    static final String COMPANY_ENDPOINT = "/companies"
    static final String TAX_CALCULATOR_ENDPOINT = "/tax"

    @Autowired
    MockMvc mockMvc

    @Autowired
    JsonService jsonService

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
        getAllCompanies().each { company -> deleteCompany(company.id) }
    }


    int addInvoiceAndReturnId(Invoice invoice) {
        addAndReturnId(invoice, INVOICE_ENDPOINT)
    }

    int addCompanyAndReturnId(Company company) {
        addAndReturnId(company, COMPANY_ENDPOINT)

    }
    List<Invoice> getAllInvoices() {
        getAll(Invoice[], INVOICE_ENDPOINT)
    }

    List<Company> getAllCompanies() {
        getAll(Company[], COMPANY_ENDPOINT)
    }


    Invoice getInvoiceById(long id) {
        getById(id, Invoice, "$INVOICE_ENDPOINT")
    }

    Company getCompanyById(long id) {
        getById(id, Company, COMPANY_ENDPOINT)
    }

    String invoiceAsJson(int id) {
        jsonService.convertToJson(invoice(id))
    }

    String companyAsJson(long id) {
        jsonService.convertToJson(company(id))
    }

    List<Invoice> addMultipleInvoices(int numberOfInvoices) {
        (1..numberOfInvoices).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(invoice)
            return invoice
        }
    }

    List<Company> addUniqueCompanies(int count) {
        (1..count).collect { id ->
            def company = company(id)
            company.id = addCompanyAndReturnId(company)
            company
        }
    }

    void deleteInvoice(long id) {
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

    void deleteCompany(long id) {
        mockMvc.perform(delete("$COMPANY_ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

    CalculatorResult calculateTax(Company company) {
        def response = mockMvc.perform(post("$TAX_CALCULATOR_ENDPOINT").content(jsonService.convertToJson(company)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.convertToObject(response, CalculatorResult)
    }

    private <T> int addAndReturnId(T item, String endpoint) {
        Integer.valueOf(
                mockMvc.perform(
                        post(endpoint)
                                .content(jsonService.convertToJson(item))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString
        )
    }

    private <T> T getAll(Class<T> clazz, String endpoint) {
        def response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.convertToObject(response, clazz)
    }

    private <T> T getById(long id, Class<T> clazz, String endpoint) {
        def invoiceAsString = mockMvc.perform(get("$endpoint/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.convertToObject(invoiceAsString, clazz)
    }

}


