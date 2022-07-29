package com.course2.apisecurity.server.sqlinjection;

import com.course2.apisecurity.api.request.sqlinjection.JdbcCustomerPatchRequest;
import com.course2.apisecurity.entity.JdbcCustomer;
import com.course2.apisecurity.repository.JdbcCustomerDangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/api/sqlinjection/danger/v1")
@Validated
public class JdbcCustomerDangerApi {
    @Autowired
    private JdbcCustomerDangerRepository repository;

    @GetMapping(value = "/customer/{email}")
    public JdbcCustomer findCustomerByEmail(@PathVariable(required = true, name = "email") String email) {
        return repository.findCustomerByEmail(email);
    }

    @GetMapping(value = "/customer")
    public List<JdbcCustomer> findCustomersByGender(
            @Pattern(regexp = "^[MF]$", message = "Invalid gender") @RequestParam(required = true, name = "genderCode") String genderCode) {
      return  repository.findCustomersByGender(genderCode);

    }

    @PostMapping(value = "/customer")
    public void createCustomer(@RequestBody(required = true) @Valid JdbcCustomer newCustomer) {
        repository.createCustomer(newCustomer);
    }

    @PatchMapping(value = "/customer/{customerId}")
    public void updateCustomerFullName(@PathVariable(required = true, name = "customerId") int customerId,
                                       @RequestBody(required = true) JdbcCustomerPatchRequest request) {
        repository.updateCustomerFullName(customerId, request.getNewFullName());
    }
}
