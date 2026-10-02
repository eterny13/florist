package com.example.florist.service.customer

import com.example.generated.db.Tables
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class CustomerServiceIntegrationSpec extends Specification {
    @Autowired
    CustomerService customerService

    @Autowired
    DSLContext dsl

    def "register writes a customer that can be read back from MySQL"() {
        when:
        def result = customerService.register("Integration Customer", "integration@example.com")

        then:
        result.isRight()
        def savedCustomer = result.get()
        customerService.findById(savedCustomer.id).get() == savedCustomer

        cleanup:
        if (result?.isRight()) {
            dsl.deleteFrom(Tables.CUSTOMER)
                    .where(Tables.CUSTOMER.ID.eq(result.get().id.value()))
                    .execute()
        }
    }

    def "invalid registration does not create a customer row"() {
        when:
        def result = customerService.register(" ", "invalid-email")

        then:
        result.isLeft()
        result.getLeft().size() == 2
    }
}
