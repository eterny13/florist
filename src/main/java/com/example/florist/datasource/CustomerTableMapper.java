package com.example.florist.datasource;

import com.example.florist.domain.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static com.example.florist.generated.db.Tables.CUSTOMER;


@Component
@RequiredArgsConstructor
public class CustomerTableMapper {
    private final DSLContext dsl;

    public void persist(Customer customer) {
        dsl.insertInto(CUSTOMER,
                        CUSTOMER.NAME, CUSTOMER.EMAIL)
                .values(customer.name(), customer.email())
                .execute();
    }
}
