package com.flexicore.organization.request;

public class SalesRegionCreate {
	private String name;
	private String description;

	public String getName() {
		return name;
	}

	public SalesRegionCreate setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public SalesRegionCreate setDescription(String description) {
		this.description = description;
		return this;
	}
}
