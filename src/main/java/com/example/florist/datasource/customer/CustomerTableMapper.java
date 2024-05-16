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
        dsl.insertInto(CUSTOMER,
                        CUSTOMER.NAME, CUSTOMER.EMAIL)
                .values(customer.getName(), customer.getEmail())
                .execute();
    }
}
