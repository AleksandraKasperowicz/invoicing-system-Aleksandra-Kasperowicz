package pl.futurecollars.invoicing.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestInvoice
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class InvoiceControllerStepwiseTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @Shared
    private long invoiceId

    private Invoice originalInvoice = TestInvoice.invoice(1)

    private LocalDate updateDate = LocalDate.now()

    @Autowired
    private ApplicationContext context

    @Requires({ System.getProperty('spring.profiles.active', 'memory').contains("mongo") })
    def "database is dropped to ensure clean state"() {
        expect:
        MongoDatabase mongoDatabase = context.getBean(MongoDatabase)
        mongoDatabase.drop()
    }

    def "empty array is returned when no invoices were created"() {
        given:
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
        when:
        def response = mockMvc.perform(get("/invoices/getAll"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        then:
        response == "[ ]"
    }

    def "should add single invoice"() {
        given:
        def invoice = originalInvoice
        def invoiceAsJson = objectMapper.writeValueAsString(invoice)

        when:
        invoiceId = mockMvc.perform(post("/invoices/add")
                .content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString as long

        then:
        invoiceId > 0

    }

    def "should return one invoice when getting all invoice"() {
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices/getAll"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = objectMapper.readValue(response, Invoice[])

        then:
        invoices.size() == 1
        invoices[0] == expectedInvoice

    }

    def "invoice is returned correctly when getting by id"() {
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices/getById/" + invoiceId))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = objectMapper.readValue(response, Invoice)

        then:
        invoices == expectedInvoice

    }

    def "invoice date can be modified"() {
        given:
        def modificationInvoice = originalInvoice
        modificationInvoice.id = invoiceId
        modificationInvoice.date = updateDate

        def invoiceAsJson = objectMapper.writeValueAsString(modificationInvoice)

        expect:
        mockMvc.perform(put("/invoices/update/" + invoiceId)
                .content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
    }


    def "updated invoice is returned correctly when getting by id"() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId
        expectedInvoice.date = updateDate

        when:
        def response = mockMvc.perform(get("/invoices/getById/" + invoiceId))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = objectMapper.readValue(response, Invoice)

        then:
        invoices == expectedInvoice
    }

    def "invoice can be deleted"() {
        expect:
        mockMvc.perform(delete("/invoices/" + invoiceId))
                .andExpect(status().isNoContent())

        and:
        mockMvc.perform(delete("/invoices/" + invoiceId))
                .andExpect(status().isNotFound())

        and:
        mockMvc.perform(get("/invoices/getById/" + invoiceId))
                .andExpect(status().isNotFound())
    }

    void deleteInvoice(long id) {
        mockMvc.perform(delete("/invoices/" + id))
                .andExpect(status().isNoContent())
    }
    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get("/invoices/getAll"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        objectMapper.readValue(response, Invoice[])
    }
}