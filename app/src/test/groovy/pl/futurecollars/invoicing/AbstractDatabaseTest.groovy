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
        def ids = invoices.collect({ database.save(it) })

        then:
        ids.size() == (1..invoices.size()).collect().size()
        ids.forEach({ assert database.getById(it).isPresent() })
        ids.forEach({ assert database.getById(it).get().getId() == it })
    }

    def "should return empty optional"() {
        expect:
        !database.getById(0).isPresent()
    }
    def "should update invoice"() {
        given:
        //int id = database.save(invoices.get(2)) as int
        def invoice = database.getById(3)
        when:
        invoice.get().setData(LocalDate.now())
        database.update(3, invoice.get())

        then:
        database.getById(3).get().getData() == LocalDate.now()
    }
    def "get by id "(){

        when:
        def invoice = database.getById(2)

        then:
        invoice.isPresent()

    }

    def "should return get all invoices"() {
        when:
        def allInvoices =  database.getAll()

        then:
        allInvoices.size() == invoices.size()
        allInvoices.forEach({ assert it == invoices.get(it.getId() - 1 as int) })
    }


    def "should return exceptions"() {
        when:
        database.update(555, invoices.get(1))

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == "Faktura o numerze: 555 nie istnieje"
    }
    def "should delete invoice"() {

        when:
        def sizeBeforeDelete = database.getAll().size()
        database.delete(1)

        then:
        database.getAll().size() == sizeBeforeDelete - 1
        database.getAll().forEach({ assert it.getId() != 1 })
    }
}
