package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.territories.Address;

public class SiteCreate {

	private String name;
	private String description;
	private String addressId;
	@JsonIgnore
	private Address address;
	private String externalId;

	public String getName() {
		return name;
	}

	public SiteCreate setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public SiteCreate setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getAddressId() {
		return addressId;
	}

	public SiteCreate setAddressId(String addressId) {
		this.addressId = addressId;
		return this;
	}

	@JsonIgnore
	public Address getAddress() {
		return address;
	}

	public SiteCreate setAddress(Address address) {
		this.address = address;
		return this;
	}

	public String getExternalId() {
		return externalId;
	}

	public SiteCreate setExternalId(String externalId) {
		this.externalId = externalId;
		return this;
	}
}
