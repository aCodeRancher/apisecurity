package com.course2.apisecurity.repository;

import com.course2.apisecurity.entity.BasicApikey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BasicApikeyRepository extends CrudRepository<BasicApikey, Integer> {
    Optional<BasicApikey> findByApikeyAndExpiredAtAfter(String apikey, LocalDateTime now);

}
