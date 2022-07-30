package com.course2.apisecurity.repository;

import com.course2.apisecurity.entity.XssArticle;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface XssArticleRepository extends CrudRepository<XssArticle, Integer> {

    public List<XssArticle> findByArticleContainsIgnoreCase(String article);
}
