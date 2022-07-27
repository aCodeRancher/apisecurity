package com.course2.apisecurity.server;

import com.course2.apisecurity.api.request.sqlinjection.JdbcCustomerPatchRequest;
import com.course2.apisecurity.entity.JdbcCustomer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

         mockMvc.perform(get("/api/sqlinjection/danger/v1/customer/"+ email))
                 .andExpect(jsonPath("$.errorMessage").value("Sorry, some error happened"));
    }

    @Test
     void findCustomersByInvalidGender() throws Exception {
               mockMvc.perform(get("/api/sqlinjection/danger/v1/customer?genderCode=M;create table"))
                       .andExpect(jsonPath("$.errorMessage").value("Sorry, constraint violation happened"));

   }

   @Test
    void createCustomerWithSQLInj() throws Exception{
       JdbcCustomer jdbcCustomer = new JdbcCustomer();
       jdbcCustomer.setFullName("Thanos Thanos");
       jdbcCustomer.setGender("M'); DROP table jdbc_merchant --");
       jdbcCustomer.setBirthDate(LocalDate.of(1980,2,16));
       jdbcCustomer.setEmail("thanos.thanos@gmail.com");

       String jsonInput = objectMapper.writeValueAsString(jdbcCustomer);
        mockMvc.perform(post("/api/sqlinjection/danger/v1/customer/")
                       .content(jsonInput)
                        .contentType(MediaType.APPLICATION_JSON))
                       .andExpect(jsonPath("$.errorMessage").value("Sorry, input is invalid"));

   }

   @Test
    void updateCustomerWithSQLInj() throws Exception{
       JdbcCustomerPatchRequest jdbcCustomerPatchRequest
               = new JdbcCustomerPatchRequest();
       jdbcCustomerPatchRequest.setNewFullName("Peter Parker; DROP table jdbc_merchant --");
       String jsonInput =  objectMapper.writeValueAsString(jdbcCustomerPatchRequest);
       mockMvc.perform(patch("/api/sqlinjection/danger/v1/customer/1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(jsonInput))
                       .andExpect(status().isOk());
   }

}