package com.course2.apisecurity.entity;


import org.springframework.data.relational.core.mapping.Column;

public class BasicAclUserUriRef {
    private int uriId;

     @Column("user_id")
     private Long id;


}
