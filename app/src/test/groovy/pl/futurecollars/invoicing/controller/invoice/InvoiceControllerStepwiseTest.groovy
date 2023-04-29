package pl.futurecollars.invoicing.controller.invoice


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestInvoice
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.ulits.JsonService
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestInvoice.resetIds

@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class InvoiceControllerStepwiseTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Shared
    private long invoiceId

    private Invoice originalInvoice = TestInvoice.invoice(1)

    private LocalDate updateDate = LocalDate.now()

    @Autowired
    private Database<Invoice> database

    @Requires({ System.getProperty('spring.profiles.active', 'memory').contains("mongo") })
    def "database is dropped to ensure clean state"() {
        expect:
        database != null

        when:
        database.reset()

        then:
        database.getAll().size() == 0
    }

    def "empty array is returned when no invoices were created"() {

        when:
        def response = mockMvc.perform(get("/invoices/"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        then:
        response == "[]"
    }

    def "should add single invoice"() {
        given:
        def invoice = originalInvoice
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        invoiceId = Integer.valueOf(mockMvc.perform(post("/invoices/")
                .content(invoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        )

        then:
        invoiceId > 0

    }

    def "should return one invoice when getting all invoice"() {
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices/"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.convertToObject(response, Invoice[])

        then:
        invoices.size() == 1
        resetIds(invoices[0]) == resetIds(expectedInvoice)

    }

    def "invoice is returned correctly when getting by id"() {
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices/" + invoiceId))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.convertToObject(response, Invoice)

        then:
        resetIds(invoices) == resetIds(expectedInvoice)

    }

    def "invoice date can be modified"() {
        given:
        def modificationInvoice = originalInvoice
        modificationInvoice.date = updateDate

        def invoiceAsJson = jsonService.convertToJson(modificationInvoice)

        expect:
        mockMvc.perform(put("/invoices/" + invoiceId)
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
        def response = mockMvc.perform(get("/invoices/$invoiceId"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.convertToObject(response, Invoice)

        then:
        resetIds(invoices) == resetIds(expectedInvoice)
    }

    def "invoice can be deleted"() {
        expect:
        mockMvc.perform(delete("/invoices/" + invoiceId))
                .andExpect(status().isNoContent())

        and:
        mockMvc.perform(delete("/invoices/" + invoiceId))
                .andExpect(status().isNotFound())

        and:
        mockMvc.perform(get("/invoices/" + invoiceId))
                .andExpect(status().isNotFound())
    }

    void deleteInvoice(long id) {
        mockMvc.perform(delete("/invoices/" + id))
                .andExpect(status().isNoContent())
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get("/invoices/"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        jsonService.convertToObject(response, Invoice[])
    }
}