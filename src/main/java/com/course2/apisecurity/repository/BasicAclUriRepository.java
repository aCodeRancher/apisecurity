package com.course2.apisecurity.repository;

import com.course2.apisecurity.entity.BasicAclUri;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicAclUriRepository extends CrudRepository<BasicAclUri, Integer> {
}
