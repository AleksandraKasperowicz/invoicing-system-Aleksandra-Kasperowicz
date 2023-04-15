package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import java.time.LocalDate

import static pl.futurecollars.invoicing.TestInvoice.invoice

abstract class AbstractDatabaseTest extends Specification {

    Database database = initDatabase()
    List<Invoice> invoices = (1..12).collect { invoice(it) }

    abstract Database initDatabase()

    def "should save invoices returning id, should have correct id and get id return correct invoice "() {
        when:
        def ids = invoices.collect { database.save(it) }

        then:
        ids.size() == (1..invoices.size()).collect().size()
        ids.forEach { assert database.getById(it).isPresent() }
        ids.forEach { assert database.getById(it).get().getId() == it }
    }

    def "should return empty optional"() {
        expect:
        !database.getById(0).isPresent()
    }

    def "should update invoice"() {

        given:
        def invoice = database.getById(1)
        when:
        invoice.get().setDate(LocalDate.now())
        database.update(1, invoice.get())

        then:
        database.getById(3).get().getDate() == LocalDate.now()
    }

    def "should get by id "() {
        when:
        def invoice = database.getById(2)

        then:
        invoice.isPresent()
    }

    def "should return get all invoices"() {
        when:
        def allInvoices = database.getAll()

        then:
        allInvoices.size() == invoices.size()
        allInvoices.forEach {
            def index = it.getId() -1 as int
// resetting is necessary because database query returns ids while we don't know ids in original invoice
            invoices.get(index).id = index + 1
            it.getBuyer().id = index + 11
            it.getSeller().id = index + 1
            assert it == invoices.get(index)
        }
    }

    def "should return exceptions"() {
        when:
        Optional<Invoice> invoice = database.update(555, invoices.get(1))

        then:
        invoice.isEmpty()
    }

    def "should delete invoice"() {

        when:
        def sizeBeforeDelete = database.getAll().size()
        database.delete(1)

        then:
        database.getAll().size() == sizeBeforeDelete - 1
        database.getAll().forEach { assert it.getId() != 1 }
    }
}