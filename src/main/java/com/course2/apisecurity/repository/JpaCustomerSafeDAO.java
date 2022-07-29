package com.course2.apisecurity.repository;

import com.course2.apisecurity.entity.JpaCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class JpaCustomerSafeDAO {

   @Autowired
	private EntityManager entityManager;

	public List<JpaCustomer> findByGender(String gender) {
		// some complex business logic to build query
		var jpql = "SELECT customer FROM JpaCustomer customer WHERE customer.gender = :gender";
		var query = entityManager.createQuery(jpql, JpaCustomer.class).setParameter("gender", gender);

		return query.getResultList();
	}
}
