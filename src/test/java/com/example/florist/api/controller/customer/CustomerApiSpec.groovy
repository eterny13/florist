package com.example.florist.api.controller.customer

import com.example.florist.api.controller.customer.request.FixtureCustomerRequest
import com.example.florist.domain.customer.FixtureCustomer
import com.example.florist.domain.shared.DomainError
import com.example.florist.service.customer.CustomerService
import io.vavr.collection.Vector
import io.vavr.control.Either
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CustomerApi)
@Unroll
class CustomerApiSpec extends Specification {
    @MockitoBean
    CustomerService customerService
    @Autowired
    MockMvc mockMvc

    def "CustomerApi post #label"() {
        given:
        if (label == "Normal") {
            Mockito.when(customerService.register(Mockito.anyString(), Mockito.anyString()))
                    .thenReturn(Either.right(FixtureCustomer.of("abcd1234", "Steve Gatt", "abc@example.com")))
        } else if (label == "Empty Name") {
            Mockito.when(customerService.register(Mockito.eq(""), Mockito.anyString()))
                    .thenReturn(Either.left(Vector.of(new DomainError.ValidationError("name", "Name must not be blank"))))
        } else if (label == "Illegal Email") {
            Mockito.when(customerService.register(Mockito.anyString(), Mockito.eq("example.com")))
                    .thenReturn(Either.left(Vector.of(new DomainError.ValidationError("email", "Invalid email format: example.com"))))
        }

        when:
        ResultActions response = mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        )

        then:
        response.andExpect(status().is(httpStatus.value()))
                .andExpect(content().contentTypeCompatibleWith(mediaType))

        where:
        label           | request                                    || httpStatus             | mediaType
        "Normal"        | FixtureCustomerRequest.asStringNormal()    || HttpStatus.CREATED     | MediaType.TEXT_PLAIN
        "Empty Name"    | FixtureCustomerRequest.asStringEmptyName() || HttpStatus.BAD_REQUEST | MediaType.APPLICATION_PROBLEM_JSON
        "Illegal Email" | FixtureCustomerRequest.asStringIllegal()   || HttpStatus.BAD_REQUEST | MediaType.APPLICATION_PROBLEM_JSON
    }
}
