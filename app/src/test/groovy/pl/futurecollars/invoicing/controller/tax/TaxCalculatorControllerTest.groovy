package pl.futurecollars.invoicing.controller.tax

import pl.futurecollars.invoicing.controller.AbstractControllerTest
import spock.lang.Unroll

@Unroll
class TaxCalculatorControllerTest extends AbstractControllerTest {

    def "zeros are returned when there are no invoices in the system"() {
        when:
        def taxCalculatorResponse = calculateTax("0")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "zeros are returned when tax id is not matching"() {
        given:
        addMultipleInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax("no_match")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "sum of all products is returned when tax id is matching"() {
        given:
        addMultipleInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax("5")

        then:
        taxCalculatorResponse.income == 15000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 15000
        taxCalculatorResponse.incomingVat == 1200.0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 1200.0

        when:
        taxCalculatorResponse = calculateTax("10")

        then:
        taxCalculatorResponse.income == 55000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 55000
        taxCalculatorResponse.incomingVat == 4400.0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 4400.0

        when:
        taxCalculatorResponse = calculateTax("15")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 15000
        taxCalculatorResponse.earnings == -15000
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 1200.0
        taxCalculatorResponse.vatToReturn == -1200.0
    }

    def "correct values are returned when company was buyer and seller"() {
        given:
        addMultipleInvoices(15)
        when:
        def taxCalculatorResponse = calculateTax("12")

        then:
        taxCalculatorResponse.income == 78000
        taxCalculatorResponse.costs == 3000
        taxCalculatorResponse.earnings == 75000
        taxCalculatorResponse.incomingVat == 6240.0
        taxCalculatorResponse.outgoingVat == 240.0
        taxCalculatorResponse.vatToReturn == 6000.0
    }

}