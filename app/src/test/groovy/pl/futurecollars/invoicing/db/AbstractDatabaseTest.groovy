package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import java.time.LocalDate

import static pl.futurecollars.invoicing.TestInvoice.invoice
import static pl.futurecollars.invoicing.TestInvoice.resetIds

abstract class AbstractDatabaseTest extends Specification {

    Database<Invoice> database = initDatabase()

    List<Invoice> invoices = (1..12).collect { invoice(it) }

    abstract Database<Invoice> initDatabase()

    def setup(){
        database.getAll().forEach {invoices -> database.delete(invoices.getId())}

        assert database.getAll().isEmpty()
    }

    def "should save invoices returning sequential id"() {
        when:
        def ids = invoices.collect { it.id = database.save(it) }

        then:
        (1..invoices.size() - 1).forEach { assert ids[it] == ids[0] + it }
    }

    def "should save invoices returning id, should have correct id and get id return correct invoice "() {
        when:
        def ids = invoices.collect {database.save(it) }

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
            invoices.get(index).getBuyer().id = index + 11
            it.getBuyer().id = index +11
            invoices.get(index).getSeller().id = index + 1
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
    def "deleting not existing invoice returns optional empty"() {
        expect:
        database.delete(123) == Optional.empty()
    }

    def "it's possible to update the invoice, original invoice is returned"() {
        given:
        def originalInvoice = invoices.get(0)
        originalInvoice.id = database.save(originalInvoice)

        def expectedInvoice = invoices.get(1)
        expectedInvoice.id = originalInvoice.id

        when:
        def result = database.update(originalInvoice.id, expectedInvoice)

        then:
        def invoiceAfterUpdate = database.getById(originalInvoice.id).get()
        def invoiceAfterUpdateAsString = resetIds(invoiceAfterUpdate).toString()
        def expectedInvoiceAfterUpdateAsString = resetIds(expectedInvoice).toString()
        invoiceAfterUpdateAsString == expectedInvoiceAfterUpdateAsString

        and:
        def invoiceBeforeUpdateAsString = resetIds(result.get()).toString()
        def expectedInvoiceBeforeUpdateAsString = resetIds(originalInvoice).toString()
        invoiceBeforeUpdateAsString == expectedInvoiceBeforeUpdateAsString
    }

    def "updating not existing invoice returns Optional.empty()"() {
        expect:
        database.update(213, invoices.get(1)) == Optional.empty()
    }

}