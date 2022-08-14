package com.course2.apisecurity.entity;

import java.time.LocalDate;

//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

//@Entity
//@Table(name ="jdbc_customer")
public class JpaCustomer {

  // 	@Id
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
