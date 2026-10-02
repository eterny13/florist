package com.example.florist.domain.shared

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ValueTypesSpec extends Specification {
    def 'quantity supports arithmetic, predicates and comparisons: #left'() {
        expect:
        new Quantity(left).add(new Quantity(right)) == new Quantity(sum)
        new Quantity(left).subtract(new Quantity(right)) == new Quantity(difference)
        new Quantity(left).isGreaterThanOrEqual(new Quantity(right)) == (left >= right)
        new Quantity(left).isPositive() == (left > 0)
        new Quantity(left).compareTo(new Quantity(right)) == expectedComparison
        new Quantity(3).compareTo(new Quantity(5)) == -1

        where:
        left | right || sum | difference || expectedComparison
        0    | 0     || 0   | 0          || 0
        5    | 3     || 8   | 2          || 1
        3    | 3     || 6   | 0          || 0
    }

    def 'negative quantity #value is rejected'() {
        when:
        new Quantity(value)
        then:
        thrown(IllegalArgumentException)
        where:
        value << [-1, -10]
    }

    def 'quantity subtraction rejects underflow'() {
        when:
        new Quantity(2).subtract(new Quantity(3))
        then:
        def error = thrown(IllegalArgumentException)
        error.message == 'Cannot subtract 3 from 2'
    }

    def 'days allows zero and orders values'() {
        expect:
        Days.ZERO == new Days(0)
        new Days(2).compareTo(new Days(5)) == -1
        new Days(5).compareTo(new Days(5)) == 0
        new Days(6).compareTo(new Days(5)) == 1
    }

    def 'negative days #value are rejected'() {
        when:
        new Days(value)
        then:
        thrown(IllegalArgumentException)
        where:
        value << [-1, -20]
    }
}
