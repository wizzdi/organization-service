package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.territories.Address;
import com.flexicore.request.BaseclassCreate;

public class OrganizationCreate extends BaseclassCreate {

	private String mainAddressId;
	@JsonIgnore
	private Address mainAddress;

	public String getMainAddressId() {
		return mainAddressId;
	}

	public <T extends OrganizationCreate> T setMainAddressId(String mainAddressId) {
		this.mainAddressId = mainAddressId;
		return (T) this;
	}

	@JsonIgnore
	public Address getMainAddress() {
		return mainAddress;
	}

	public <T extends OrganizationCreate> T setMainAddress(Address mainAddress) {
		this.mainAddress = mainAddress;
		return (T) this;
	}
}
