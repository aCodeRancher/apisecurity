package com.course2.apisecurity.repository;

import com.course2.apisecurity.entity.JpaCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface JpaCustomerCrudRepository {//extends CrudRepository<JpaCustomer, Integer> {

    List<JpaCustomer> findByEmail(String email);


}
