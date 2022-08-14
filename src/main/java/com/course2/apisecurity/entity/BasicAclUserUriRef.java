package com.course2.apisecurity.entity;

import org.springframework.data.relational.core.mapping.Column;

public class BasicAclUserUriRef {

    private int uriId;

  //  @Column("user_id")
   // private Long id;

    public int getUriId() {
        return uriId;
    }

    public void setUriId(int uriId) {
        this.uriId = uriId;
    }

  //  public Long getId(){
      //  return id;
   // }

  //  public void setId(Long id){
 //       this.id = id;
 //   }
}
