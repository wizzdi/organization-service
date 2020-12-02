package com.flexicore.organization.interfaces;

import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.organization.model.Supplier;
import com.flexicore.organization.request.SupplierCreate;
import com.flexicore.organization.request.SupplierFiltering;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface ISupplierService extends ServicePlugin {
	List<Supplier> getAllSuppliers(SecurityContext securityContext,
			SupplierFiltering filtering);

	Supplier createSupplierNoMerge(SupplierCreate creationContainer,
			SecurityContext securityContext);

	void massMerge(List<?> toMerge);

	boolean updateSupplierNoMerge(SupplierCreate create, Supplier Supplier);

}
