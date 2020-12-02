package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.organization.model.SupplierApi;

public class SupplierCreate extends OrganizationCreate {

	private String supplierApiId;
	@JsonIgnore
	private SupplierApi supplierApi;

	@Override
	public SupplierCreate setName(String name) {
		return (SupplierCreate) super.setName(name);
	}

	@Override
	public SupplierCreate setDescription(String description) {
		return (SupplierCreate) super.setDescription(description);
	}

	public String getSupplierApiId() {
		return supplierApiId;
	}

	public <T extends SupplierCreate> T setSupplierApiId(String supplierApiId) {
		this.supplierApiId = supplierApiId;
		return (T) this;
	}

	@JsonIgnore
	public SupplierApi getSupplierApi() {
		return supplierApi;
	}

	public <T extends SupplierCreate> T setSupplierApi(SupplierApi supplierApi) {
		this.supplierApi = supplierApi;
		return (T) this;
	}
}
