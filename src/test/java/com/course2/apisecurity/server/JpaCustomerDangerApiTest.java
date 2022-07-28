package com.course2.apisecurity.server;

import com.course2.apisecurity.entity.JpaCustomer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JpaCustomerDangerApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void findCustomerByEmailSQLInj() throws Exception{
        String sql = "1' and 1 = 2 union SELECT null, version(), null, null, null FROM information_schema.tables WHERE table_schema='apisecurity' AND table_type='BASE TABLE' --";
        String response = mockMvc.perform(get("/api/sqlinjection/danger/v2/customer/"+sql))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(response.isEmpty());
    }

    @Test
    public void findCustomerByEmail() throws Exception{
        String emailToFind = "asturch1@bbb.org";
        String response = mockMvc.perform(get("/api/sqlinjection/danger/v2/customer/"+emailToFind))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(emailToFind));
    }

    @Test
    public void findCustomerByGenderM() throws Exception{
        String gender = "M";
        MvcResult result = mockMvc.perform(get("/api/sqlinjection/danger/v2/customer?genderCode="+gender))
                .andExpect(status().isOk())
                .andReturn();
        List<JpaCustomer> customers =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<JpaCustomer>>(){});
        assertTrue(customers.size()>0);
        customers.stream().forEach( c-> assertTrue(c.getGender().equals("M")));
    }

    @Test
    public void findCustomerByGenderSQLInj() throws Exception{
        String gender = "M' or 'x' ='x";
        MvcResult result = mockMvc.perform(get("/api/sqlinjection/danger/v2/customer?genderCode="+gender))
                .andExpect(status().isOk())
                .andReturn();
        List<JpaCustomer> customers =
                objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<JpaCustomer>>(){});
        assertTrue(customers.size()>0);
        customers.stream().forEach( c-> assertTrue(c.getGender().equals("M") || c.getGender().equals("F")));
    }


}