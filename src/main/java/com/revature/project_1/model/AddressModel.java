package com.revature.project_1.model;

public class AddressModel {
	private String streetAddress;
	private String city;
	private String zipcode;
	private String state;
	
	private AddressModel(String streetAddress, String city, String zipcode, String state) {
		this.streetAddress = streetAddress;
		this.city = city;
		this.zipcode = zipcode;
		this.state = state;
	}
	
	public static class Builder {
		private String streetAddress;
		private String city;
		private String zipcode;
		private String state;
		
		public Builder withStreetAdress(String streetAddress) {
			this.streetAddress = streetAddress;
			return this;
		}
		public Builder withCity(String city) {
			this.city = city;
			return this;
		}
		public Builder withZipCode(String zipcode) {
			this.zipcode = zipcode;
			return this;
		}
		public Builder withState(String state) {
			this.state = state;
			return this;
		}
		public AddressModel build() {
			return new AddressModel(streetAddress, city, zipcode, state);
		}
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
