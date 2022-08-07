package com.course2.apisecurity.repository;

import com.course2.apisecurity.entity.BasicAuthUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasicAuthUserRepository extends CrudRepository<BasicAuthUser, Integer> {
    Optional<BasicAuthUser> findByUsername(String encryptedUsername);
}
