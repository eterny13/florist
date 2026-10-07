package com.example.florist.domain.customer

import com.example.florist.domain.shared.DomainError
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CustomerDomainSpec extends Specification {
    def 'customer validation accumulates invalid name and email: name=#name email=#email'() {
        when:
        def result = Customer.create(name, email)

        then:
        result.isInvalid()
        result.getError().size() == expectedErrors.size()
        result.getError().toJavaList() == expectedErrors

        where:
        name | email              || expectedErrors
        null | 'good@example.com' || [new DomainError.ValidationError('name', 'Validation Error: Invalid Name')]
        '  ' | 'good@example.com' || [new DomainError.ValidationError('name', 'Validation Error: Invalid Name')]
        'A'  | null               || [new DomainError.ValidationError('email', 'Validation Error: Invalid Email')]
        'A'  | 'invalid'          || [new DomainError.ValidationError('email', 'Validation Error: Invalid Email')]
        ''   | 'invalid'          || [new DomainError.ValidationError('name', 'Validation Error: Invalid Name'), new DomainError.ValidationError('email', 'Validation Error: Invalid Email')]
    }

    def 'customer create succeeds with generated short id and reconstruct and of preserve supplied values'() {
        when:
        def result = Customer.create('Ada', 'ada@example.com')

        then:
        result.isValid()
        result.get().name == new CustomerName('Ada')
        result.get().email == new EmailAddress('ada@example.com')
        result.get().id.value().size() == 8

        and:
        Customer.reconstruct(new CustomerId('fixed'), new CustomerName('Grace'), new EmailAddress('g@example.com')) ==
                Customer.of('fixed', 'Grace', 'g@example.com')
    }

    def 'required customer text rejects null and blank values: #input'() {
        when:
        new CustomerName(input)
        then:
        thrown(IllegalArgumentException)
        where:
        input << [null, '', '  ']
    }

    def 'customer id rejects null and blank values: #input'() {
        when:
        new CustomerId(input)
        then:
        thrown(IllegalArgumentException)
        where:
        input << [null, '', '  ']
    }

    def 'email address requires text on both sides of at sign: #input'() {
        when:
        new EmailAddress(input)
        then:
        thrown(IllegalArgumentException)
        where:
        input << [null, '', 'plain', '@domain.com', 'name@']
    }

    def 'customer id generation yields a UUID first segment'() {
        expect:
        CustomerId.generate().value() ==~ /[0-9a-f]{8}/
    }
}
