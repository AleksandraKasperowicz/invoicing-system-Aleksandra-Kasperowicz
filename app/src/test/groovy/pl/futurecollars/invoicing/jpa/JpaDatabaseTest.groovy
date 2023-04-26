package pl.futurecollars.invoicing.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database

@DataJpaTest
@IfProfileValue(name = "spring.profiles.active", value = "jpa")
class JpaDatabaseTest extends AbstractDatabaseTest {

    @Autowired
    private InvoiceRepository invoiceRepository

    @Override
    Database initDatabase() {
        assert invoiceRepository != null
        new JpaDatabase(invoiceRepository)
    }
}
