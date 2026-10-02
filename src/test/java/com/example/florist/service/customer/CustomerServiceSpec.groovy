package com.example.florist.service.customer

import com.example.florist.domain.customer.Customer
import com.example.florist.domain.customer.CustomerName
import com.example.florist.domain.shared.DomainError
import spock.lang.Specification

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
}
