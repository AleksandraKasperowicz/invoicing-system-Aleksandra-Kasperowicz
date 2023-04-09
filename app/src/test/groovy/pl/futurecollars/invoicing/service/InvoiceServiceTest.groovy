package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.memory.InMemoryDatabase
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestInvoice.invoice

class InvoiceServiceTest extends Specification {

    InvoiceService service
    List<Invoice> invoices

    def "setup"() {
        service = new InvoiceService(new InMemoryDatabase())
        invoices = (1..12).collect { invoice(it) }
    }

    def "should save invoices returning id, should have correct id and get id return correct invoice "() {
        when:
        def ids = invoices.collect({ service.save(it) })

        then:
        ids == (1..invoices.size()).collect()
        ids.forEach({ assert service.getById(it).isPresent() })
        ids.forEach({ assert service.getById(it).get().getId() == it })
        ids.forEach({ assert service.getById(it).get() == invoices.get(it -1 as int) })
    }

    def "should return empty optional"() {
        expect:
        !service.getById(1).isPresent()
    }

    def "should return empty collection"() {
        expect:
        service.getAll().isEmpty()
    }

    def "should return get all invoices"() {
        when:
        invoices.forEach({ service.save(it) })

        then:
        service.getAll().size() == invoices.size()
        service.getAll().forEach({ assert it == invoices.get(it.getId() -1 as int) })
    }

    def "should update invoice"() {
        given:
        long id = service.save(invoices.get(2))

        when:
        service.update(id, invoices.get(11))

        then:
        service.getById(id).get() == invoices.get(11)

    }

    def "should delete invoice"() {
        given:
        invoices.forEach({ service.save(it) })

        when:
        service.delete(1)

        then:
        service.getAll().size() == invoices.size() - 1
        service.getAll().forEach({ assert it.getId() != 1 })
    }

    def "should return exceptions"() {
        when:
        Optional<Invoice> invoice = service.update(555, invoices.get(1))

        then:
        invoice.isEmpty()
    }


    def "GetInvoicesByBuyer"() {
        when:
        invoices.forEach({ service.save(it) })

        then:
        !service.getInvoicesByBuyerId("11").isEmpty()

    }

    def "GetInvoicesBySeller"() {
        when:
        invoices.forEach({ service.save(it) })

        then:
        !service.getInvoicesBySellerId("11").isEmpty()

    }
}
