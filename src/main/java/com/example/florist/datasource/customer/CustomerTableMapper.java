package com.example.florist.datasource.customer;

import com.example.florist.domain.customer.Customer;
import com.example.florist.domain.customer.CustomerId;
import com.example.florist.domain.customer.CustomerName;
import com.example.florist.domain.customer.EmailAddress;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static com.example.generated.db.Tables.CUSTOMER;

@Component
@RequiredArgsConstructor
public class CustomerTableMapper {
    private final DSLContext dsl;

    public void insert(Customer customer) {
        dsl.insertInto(CUSTOMER)
                .set(CUSTOMER.ID, customer.getId().value())
                .set(CUSTOMER.NAME, customer.getName().value())
                .set(CUSTOMER.EMAIL, customer.getEmail().value())
                .execute();
    }

    public Option<Customer> findById(String customerId) {
        var customerRecord = dsl.select(CUSTOMER.ID, CUSTOMER.NAME, CUSTOMER.EMAIL)
                .from(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetchOptional();

        return Option.ofOptional(customerRecord)
                .map(r -> Customer.reconstruct(
                        new CustomerId(r.value1()),
                        new CustomerName(r.value2()),
                        new EmailAddress(r.value3())
                ));
    }

    // 互換用メソッド
    public void persist(Customer customer) {
        insert(customer);
    }

    public Customer get(String customerId) {
        return findById(customerId).getOrElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
    }
}
