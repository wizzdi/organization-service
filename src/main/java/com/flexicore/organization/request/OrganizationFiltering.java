package com.flexicore.organization.request;

import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import java.util.Set;

public class OrganizationFiltering extends PaginationFilter {

	private BasicPropertiesFilter basicPropertiesFilter;
	private Set<String> externalIds;
	private String organizationNameLike;
	private String employeeNameLike;
	public BasicPropertiesFilter getBasicPropertiesFilter() {
		return basicPropertiesFilter;
	}

	public <T extends OrganizationFiltering> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
		this.basicPropertiesFilter = basicPropertiesFilter;
		return (T) this;
	}

	public Set<String> getExternalIds() {
		return externalIds;
	}

	public OrganizationFiltering setExternalIds(Set<String> externalIds) {
		this.externalIds = externalIds;
		return this;
	}



	public String getOrganizationNameLike() {
		return organizationNameLike;
	}

	public OrganizationFiltering setOrganizationNameLike(String organizationNameLike) {
		this.organizationNameLike = organizationNameLike;
		return this;
	}

	public String getEmployeeNameLike() {
		return employeeNameLike;
	}

	public OrganizationFiltering setEmployeeNameLike(String employeeNameLike) {
		this.employeeNameLike = employeeNameLike;
		return this;
	}
}
