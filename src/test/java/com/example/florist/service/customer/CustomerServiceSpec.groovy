package com.example.florist.service.customer

import com.example.florist.domain.customer.Customer
import com.example.florist.domain.customer.CustomerId
import com.example.florist.domain.customer.CustomerName
import com.example.florist.domain.shared.DomainError
import io.vavr.control.Option
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CustomerServiceSpec extends Specification {
    def repository = Mock(CustomerRepository)
    def service = new CustomerService(repository)

    def "register persists a valid customer"() {
        when:
        def result = service.register("Ada Lovelace", "ada@example.com")

        then:
        result.isRight()
        result.get().name == new CustomerName("Ada Lovelace")
        1 * repository.persist({ Customer customer ->
            customer.name == new CustomerName("Ada Lovelace") && customer.email.value == "ada@example.com"
        })
    }

    def "register returns validation errors without persisting invalid customer"() {
        when:
        def result = service.register(" ", "invalid-email")

        then:
        result.isLeft()
        result.getLeft().size() == 2
        result.getLeft().every { it instanceof DomainError.ValidationError }
        0 * repository.persist(_)
    }

    def 'persist forwards the same customer to the repository'() {
        given:
        def customer = Customer.of('fixed', 'Ada', 'ada@example.com')

        when:
        service.persist(customer)

        then:
        1 * repository.persist(customer)
    }

    def 'findById returns a found customer unchanged'() {
        given:
        def id = new CustomerId('fixed')
        def customer = Customer.of('fixed', 'Ada', 'ada@example.com')

        when:
        def found = service.findById(id)

        then:
        1 * repository.findById(id) >> Option.of(customer)
        found == Option.of(customer)
    }

    def 'findById returns an empty option for an unknown customer'() {
        when:
        def missing = service.findById(new CustomerId('missing'))

        then:
        1 * repository.findById(new CustomerId('missing')) >> Option.none()
        missing.isEmpty()
    }
}
