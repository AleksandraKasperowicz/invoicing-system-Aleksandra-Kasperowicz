package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database

class InMemoryDatabaseTest extends AbstractDatabaseTest {

    def setup() {
        database = initDatabase()
        invoices.collect({database.save(it)})
    }

    @Override
    Database initDatabase() {
        return new InMemoryDatabase()
    }



}