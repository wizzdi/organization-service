package com.flexicore.organization.request;

public class SupplierApiCreate {

	private String name;
	private String description;
	private String implementorCanonicalName;

	public String getName() {
		return name;
	}

	public <T extends SupplierApiCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends SupplierApiCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	public String getImplementorCanonicalName() {
		return implementorCanonicalName;
	}

	public <T extends SupplierApiCreate> T setImplementorCanonicalName(
			String implementorCanonicalName) {
		this.implementorCanonicalName = implementorCanonicalName;
		return (T) this;
	}
}
