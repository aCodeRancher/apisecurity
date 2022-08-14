package com.course2.apisecurity.entity;

//import org.springframework.data.annotation.Id;

//import javax.persistence.Entity;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

//@Entity
public class JdbcCustomer {
  //  @Id
  //  @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int customerId;

    private String fullName;

    @Email
    private String email;

    private LocalDate birthDate;

    @Pattern(regexp = "^[MF]$", message = "Invalid gender")
    private String gender;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
