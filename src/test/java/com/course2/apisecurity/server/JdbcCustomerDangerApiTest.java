package com.course2.apisecurity.server;

import com.course2.apisecurity.api.request.sqlinjection.JdbcCustomerPatchRequest;
import com.course2.apisecurity.entity.JdbcCustomer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class JdbcCustomerDangerApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findCustomerByEmailSQL() throws Exception{
        String email = "1' and 1 = 2 union SELECT null, string_agg(table_name, ','), null, null, null FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE' --";

        MvcResult result = mockMvc.perform(get("/api/sqlinjection/danger/v1/customer/"+ email))
                .andReturn();
        String output = result.getResponse().getContentAsString();
        assertTrue(output.equals("Bad request, SQL injection detected"));
    }

    @Test
     void findCustomersByInvalidGender() throws Exception{
       assertThrows(NestedServletException.class,
               ()->mockMvc.perform(get("/api/sqlinjection/danger/v1/customer?genderCode=M;create table")));

   }

   @Test
    void createCustomerWithSQLInj() throws Exception{
       JdbcCustomer jdbcCustomer = new JdbcCustomer();
       jdbcCustomer.setFullName("Thanos Thanos");
       jdbcCustomer.setGender("M'); DROP table jdbc_merchant --");
       jdbcCustomer.setBirthDate(LocalDate.of(1980,2,16));
       jdbcCustomer.setEmail("thanos.thanos@gmail.com");

       String jsonInput = objectMapper.writeValueAsString(jdbcCustomer);
       MvcResult result = mockMvc.perform(post("/api/sqlinjection/danger/v1/customer/")
                       .content(jsonInput))
                       .andReturn();
       String output = result.getResponse().getContentAsString();
       assertTrue(output.equals("Bad request, SQL injection detected"));
   }

   @Test
    void updateCustomerWithSQLInj() throws Exception{
       JdbcCustomerPatchRequest jdbcCustomerPatchRequest
               = new JdbcCustomerPatchRequest();
       jdbcCustomerPatchRequest.setNewFullName("Peter Parker; DROP table jdbc_merchant --");
       String jsonInput =  objectMapper.writeValueAsString(jdbcCustomerPatchRequest);
       MvcResult result = mockMvc.perform(patch("/api/sqlinjection/danger/v1/customer/1")
                       .content(jsonInput))
                       .andReturn();
       String output = result.getResponse().getContentAsString();
       assertTrue(output.equals("Bad request, SQL injection detected"));
   }

}