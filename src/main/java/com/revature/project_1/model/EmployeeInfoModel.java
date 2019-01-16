package com.revature.project_1.model;

import java.io.IOException;

import org.apache.tomcat.jni.Address;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeInfoModel {
	private int id;
	private String email;
	private String firstname;
	private String lastname;
	private AddressModel residentAddress;

	@JsonCreator
	private EmployeeInfoModel(@JsonProperty("id")int id, @JsonProperty("email")String email, 
			@JsonProperty("firstname")String firstname, @JsonProperty("lastname")String lastname, 
			@JsonProperty("residentAddress")AddressModel residentAddress) {
		this.id = id;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.residentAddress = residentAddress;
	}

	public static class Builder {
		private int id;
		private String email;
		private String firstname;
		private String lastname;
		private AddressModel residentAddress;
		
		public Builder withId(int id) {
			this.id = id;
			return this;
		}
		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}
		public Builder withFirstName(String firstname) {
			this.firstname = firstname;
			return this;
		}
		public Builder withLastName(String lastname) {
			this.lastname = lastname;
			return this;
		}
		public Builder withResidentAddress(AddressModel residentAddress) {
			this.residentAddress = residentAddress;
			return this;
		}
		public EmployeeInfoModel build() {
			return new EmployeeInfoModel(id, email, firstname, lastname, residentAddress);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public AddressModel getResidentAddress() {
		return residentAddress;
	}

	public void setResidentAddress(AddressModel residentAddress) {
		this.residentAddress = residentAddress;
	}
}
