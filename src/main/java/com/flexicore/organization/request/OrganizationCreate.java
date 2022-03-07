package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.territories.Address;
import com.wizzdi.flexicore.security.request.BasicCreate;
import com.wizzdi.flexicore.security.request.BasicCreate;

public class OrganizationCreate extends BasicCreate {

	private String mainAddressId;
	@JsonIgnore
	private Address mainAddress;
	private String externalId;
	private Boolean createPermissionGroup;
	private String permissionGroupId;
	@JsonIgnore
	private PermissionGroup permissionGroup;
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

	public String getExternalId() {
		return externalId;
	}

	public OrganizationCreate setExternalId(String externalId) {
		this.externalId = externalId;
		return this;
	}

	public Boolean getCreatePermissionGroup() {
		return createPermissionGroup;
	}

	public OrganizationCreate setCreatePermissionGroup(Boolean createPermissionGroup) {
		this.createPermissionGroup = createPermissionGroup;
		return this;
	}

	public String getPermissionGroupId() {
		return permissionGroupId;
	}

	public OrganizationCreate setPermissionGroupId(String permissionGroupId) {
		this.permissionGroupId = permissionGroupId;
		return this;
	}

	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public OrganizationCreate setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
		return this;
	}
}
