package com.course2.apisecurity.server;

import com.course2.apisecurity.entity.JpaCustomer;
import com.course2.apisecurity.repository.JpaCustomerCrudRepository;
import com.course2.apisecurity.repository.JpaCustomerDangerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/sqlinjection/danger/v2")
public class JpaCustomerDangerApi {
  /*
    @Autowired
	private JpaCustomerCrudRepository repository;

	@Autowired
	private JpaCustomerDangerDAO dao;

	@GetMapping(value = "/customer/{email}")
	public JpaCustomer findCustomerByEmail(@PathVariable(required = true, name = "email") String email) {
		var queryResult = repository.findByEmail(email);

		if (queryResult != null && !queryResult.isEmpty()) {
			return queryResult.get(0);
		}

		return null;
	}

	@GetMapping(value = "/customer")
	public List<JpaCustomer> findCustomersByGender(
			@RequestParam(required = true, name = "genderCode") String genderCode) {
		return dao.findByGender(genderCode);
	}

   */
}
