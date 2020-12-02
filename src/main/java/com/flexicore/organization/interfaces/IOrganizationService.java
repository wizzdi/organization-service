package com.flexicore.organization.interfaces;

import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.request.OrganizationCreate;
import com.flexicore.organization.request.OrganizationFiltering;
import com.flexicore.security.SecurityContext;

public interface IOrganizationService extends ServicePlugin {

	Organization createOrganizationNoMerge(
			OrganizationCreate creationContainer,
			SecurityContext securityContext);

	boolean updateOrganizationNoMerge(Organization organization,
			OrganizationCreate organizationCreate);

	void validateFiltering(OrganizationFiltering filtering,
			SecurityContext securityContext);

}
