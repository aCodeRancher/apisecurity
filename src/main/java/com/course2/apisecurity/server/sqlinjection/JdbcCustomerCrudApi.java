package com.course2.apisecurity.server.sqlinjection;

import com.course2.apisecurity.entity.JdbcCustomer;
import com.course2.apisecurity.repository.JdbcCustomerCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

//@RestController
//@RequestMapping("/api/sqlinjection/crud/v1")
public class JdbcCustomerCrudApi {

    @Autowired
    private JdbcCustomerCrudRepository repository;

    @GetMapping(value = "/customer/{email}")
    public JdbcCustomer findCustomerByEmail(@PathVariable(required = true, name = "email") String email) {
        var queryResult = repository.findByEmail(email);

        if (queryResult != null && !queryResult.isEmpty()) {
            return queryResult.get(0);
        }

        return null;
    }

    @GetMapping(value = "/customer")
    public List<JdbcCustomer> findCustomersByGender(
            @Pattern(regexp = "^[MF]$", message = "Invalid gender") @RequestParam(required = true, name = "genderCode") String genderCode) {
        return repository.findByGender(genderCode);
    }

    @PostMapping(value = "/customer")
    public void createCustomer(@RequestBody(required = true) @Valid JdbcCustomer newCustomer) {
        repository.save(newCustomer);
    }

   /*@PatchMapping(value = "/customer/{customerId}")
     public void updateCustomerFullName(@PathVariable(required = true, name = "customerId") int customerId,
                                       @RequestBody(required = true) JdbcCustomer request) {
        Optional<JdbcCustomer> customerOptional = repository.findById(customerId);
        JdbcCustomer customerUpdate =customerOptional.get();

        customerUpdate.setFullName(request.getFullName());
        customerUpdate.setBirthDate(request.getBirthDate());
        customerUpdate.setEmail(request.getEmail());
        customerUpdate.setGender(request.getGender());
        repository.save(customerUpdate);
     }*/
}
