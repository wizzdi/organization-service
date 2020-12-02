package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.territories.Address;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SiteFiltering extends FilteringInformationHolder {

	private Set<String> addressIds = new HashSet<>();
	@JsonIgnore
	private List<Address> addresses;

	private Set<String> externalIds = new HashSet<>();

	public Set<String> getAddressIds() {
		return addressIds;
	}

	public SiteFiltering setAddressIds(Set<String> addressIds) {
		this.addressIds = addressIds;
		return this;
	}

	@JsonIgnore
	public List<Address> getAddresses() {
		return addresses;
	}

	public SiteFiltering setAddresses(List<Address> addresses) {
		this.addresses = addresses;
		return this;
	}

	public Set<String> getExternalIds() {
		return externalIds;
	}

	public SiteFiltering setExternalIds(Set<String> externalIds) {
		this.externalIds = externalIds;
		return this;
	}
}
