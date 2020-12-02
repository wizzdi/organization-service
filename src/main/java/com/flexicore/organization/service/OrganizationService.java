package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.OrganizationRepository;
import com.flexicore.organization.interfaces.IOrganizationService;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.request.OrganizationCreate;
import com.flexicore.organization.request.OrganizationFiltering;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;

import java.util.List;
import java.util.Set;

import org.pf4j.Extension;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class OrganizationService implements IOrganizationService {

	@PluginInfo(version = 1)
	@Autowired
	private OrganizationRepository repository;

	@Autowired
	private BaseclassNewService baseclassNewService;


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public Organization createOrganization(
			OrganizationCreate creationContainer,
			SecurityContext securityContext) {

		Organization employee = createOrganizationNoMerge(creationContainer,
				securityContext);
		repository.merge(employee);
		return employee;
	}

	@Override
	public Organization createOrganizationNoMerge(
			OrganizationCreate creationContainer,
			SecurityContext securityContext) {
		Organization organization = new Organization(creationContainer.getName(), securityContext);
		updateOrganizationNoMerge(organization, creationContainer);
		return organization;

	}

	@Override
	public boolean updateOrganizationNoMerge(Organization organization,
			OrganizationCreate organizationCreate) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(organizationCreate,organization);
		if(organizationCreate.getMainAddress()!=null&& (organization.getMainAddress()==null||!organizationCreate.getMainAddress().getId().equals(organization.getMainAddress().getId()))){
			organization.setMainAddress(organizationCreate.getMainAddress());
			update=true;
		}
		return update;
	}

	@Override
	public void validateFiltering(OrganizationFiltering filtering,
			SecurityContext securityContext) {
	}

}