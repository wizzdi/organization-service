package com.flexicore.organization.interfaces;

import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.organization.model.SupplierApi;
import com.flexicore.organization.request.SupplierApiCreate;
import com.flexicore.organization.request.SupplierApiFiltering;
import com.flexicore.organization.request.SupplierApiUpdate;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface ISupplierApiService extends ServicePlugin {
	List<SupplierApi> getAllSupplierApis(SecurityContext securityContext,
			SupplierApiFiltering filtering);

	SupplierApi createSupplierApi(SupplierApiCreate creationContainer,
			SecurityContext securityContext);

	SupplierApi createSupplierApiNoMerge(SupplierApiCreate creationContainer,
			SecurityContext securityContext);

	SupplierApi updateSupplierApi(SupplierApiUpdate creationContainer,
			SecurityContext securityContext);

	void massMerge(List<?> toMerge);

	boolean updateSupplierApiNoMerge(SupplierApiCreate create,
			SupplierApi SupplierApi);

	void validateCreate(SupplierApiCreate creationContainer,
			SecurityContext securityContext);

	void validateUpdate(SupplierApiUpdate creationContainer,
			SecurityContext securityContext);
}
