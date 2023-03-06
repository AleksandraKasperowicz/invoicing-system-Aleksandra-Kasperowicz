package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestInvoice.invoice

class InMemoryDatabaseTest extends Specification {

    Database database
    List<Invoice> invoices

    def "setup"(){
        database = new InMemoryDatabase()
        invoices = (1..12).collect({invoice(it)})
    }

    def "should save invoices returning id, should have correct id and get id return correct invoice "() {
        when:
        def ids = invoices.collect({database.save(it)})

        then:
        ids == (1..invoices.size()).collect()
        ids.forEach({assert database.getById(it).isPresent()})
        ids.forEach({ assert database.getById(it).get().getId() == it })
        ids.forEach({ assert database.getById(it).get() == invoices.get(it - 1) })
    }

    def "should return empty optional"() {
        expect:
        !database.getById(1).isPresent()
    }

    def "should return empty collection"() {
        expect:
        database.getAll().isEmpty()
    }
    def"should return get all invoices"(){
        when:
        invoices.forEach({ database.save(it) })

        then:
        database.getAll().size() == invoices.size()
        database.getAll().forEach({ assert it == invoices.get(it.getId() - 1) })
    }

    def "should update invoice"() {
        given:
        int id = database.save(invoices.get(2))

        when:
        database.update(id, invoices.get(11))

        then:
        database.getById(id).get() == invoices.get(11)

    }

    def "should delete invoice"() {
        given:
        invoices.forEach({ database.save(it) })

        when:
        database.delete(1)

        then:
        database.getAll().size() == invoices.size() -1
        database.getAll().forEach({ assert it.getId() != 1 })
    }
    def "should return exceptions"(){
        when:
        database.update(555, invoices.get(1))

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == "Faktura o numerze: 555 nie istnieje"
    }
}
