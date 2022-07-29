package com.course2.apisecurity.server.sqlinjection;

import com.course2.apisecurity.entity.JpaCustomer;
import com.course2.apisecurity.repository.JpaCustomerCrudRepository;
import com.course2.apisecurity.repository.JpaCustomerSafeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@RestController
//@RequestMapping("/api/sqlinjection/safe/v2")
public class JpaCustomerSafeApi {
/*
    @Autowired
	private JpaCustomerCrudRepository repository;

	@Autowired
   private JpaCustomerSafeDAO dao;

	@GetMapping(value = "/customer/{email}")
	public JpaCustomer findCustomerByEmail(@PathVariable(required = true, name = "email") String email) {
		var queryResult = repository.findByEmail(email);

     	System.out.println(queryResult);

		if (queryResult != null && !queryResult.isEmpty()) {
			return queryResult.get(0);
		}

		return null;
	}

	@GetMapping(value = "/customer")
	public List<JpaCustomer> findCustomersByGender(
			@RequestParam(required = true, name = "genderCode") String genderCode) {
		return dao.findByGender(genderCode);
	}*/
}
