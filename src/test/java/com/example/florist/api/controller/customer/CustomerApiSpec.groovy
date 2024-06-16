package com.example.florist.api.controller.customer

import com.example.florist.api.controller.customer.request.FixtureCustomerRequest
import com.example.florist.service.customer.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
@Unroll
class CustomerApiSpec extends Specification {
    @MockBean
    CustomerService customerService
    @Autowired
    MockMvc mockMvc

    def "CustomerApi post #label"() {
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
        "Normal"        | FixtureCustomerRequest.asStringNormal()    || HttpStatus.OK          | MediaType.TEXT_PLAIN
        "Empty Name"    | FixtureCustomerRequest.asStringEmptyName() || HttpStatus.BAD_REQUEST | MediaType.APPLICATION_PROBLEM_JSON
        "Illegal Email" | FixtureCustomerRequest.asStringIllegal()   || HttpStatus.BAD_REQUEST | MediaType.APPLICATION_PROBLEM_JSON
    }
}
