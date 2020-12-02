package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.organization.model.SalesPerson;
import com.flexicore.organization.model.SalesRegion;

import java.time.LocalDateTime;

public class SalesPersonToRegionCreate {

	private String salesPersonId;
	@JsonIgnore
	private SalesPerson salesPerson;
	private String salesRegionId;
	@JsonIgnore
	private SalesRegion salesRegion;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	public String getSalesPersonId() {
		return salesPersonId;
	}

	public SalesPersonToRegionCreate setSalesPersonId(String salesPersonId) {
		this.salesPersonId = salesPersonId;
		return this;
	}

	@JsonIgnore
	public SalesPerson getSalesPerson() {
		return salesPerson;
	}

	public SalesPersonToRegionCreate setSalesPerson(SalesPerson salesPerson) {
		this.salesPerson = salesPerson;
		return this;
	}

	public String getSalesRegionId() {
		return salesRegionId;
	}

	public SalesPersonToRegionCreate setSalesRegionId(String salesRegionId) {
		this.salesRegionId = salesRegionId;
		return this;
	}

	@JsonIgnore
	public SalesRegion getSalesRegion() {
		return salesRegion;
	}

	public SalesPersonToRegionCreate setSalesRegion(SalesRegion salesRegion) {
		this.salesRegion = salesRegion;
		return this;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public SalesPersonToRegionCreate setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
		return this;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public SalesPersonToRegionCreate setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
		return this;
	}
}
