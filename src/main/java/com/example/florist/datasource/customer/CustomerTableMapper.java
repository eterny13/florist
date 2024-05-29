package com.example.florist.datasource.customer;

import com.example.florist.domain.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static com.example.generated.db.Tables.CUSTOMER;


@Component
@RequiredArgsConstructor
public class CustomerTableMapper {
    private final DSLContext dsl;

    public void persist(Customer customer) {
        dsl.insertInto(CUSTOMER)
                .set(CUSTOMER.ID, customer.getId())
                .set(CUSTOMER.NAME, customer.getName())
                .set(CUSTOMER.EMAIL, customer.getEmail())
                .execute();
    }

    public Customer get(String customerId) {
        var customerRecord = dsl.select(CUSTOMER.ID, CUSTOMER.NAME, CUSTOMER.EMAIL)
                .from(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetchOptional().get();

        return new Customer(customerRecord.value1(), customerRecord.value2(), customerRecord.value3());
    }
}
