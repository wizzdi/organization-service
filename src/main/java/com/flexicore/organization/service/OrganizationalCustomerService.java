package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.OrganizationalCustomerRepository;
import com.flexicore.organization.model.OrganizationalCustomer;
import com.flexicore.organization.request.OrganizationalCustomerCreate;
import com.flexicore.organization.request.OrganizationalCustomerFiltering;
import com.flexicore.organization.request.OrganizationalCustomerUpdate;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class OrganizationalCustomerService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private OrganizationalCustomerRepository repository;

	@PluginInfo(version = 1)
	@Autowired
	private CustomerService customerService;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public void validateFiltering(OrganizationalCustomerFiltering filtering,
			SecurityContext securityContext) {
		customerService.validateFiltering(filtering, securityContext);

	}

	public PaginationResponse<OrganizationalCustomer> getAllOrganizationalCustomers(
			SecurityContext securityContext, OrganizationalCustomerFiltering filtering) {
		List<OrganizationalCustomer> list = repository.getAllOrganizationalCustomers(securityContext, filtering);
		long count = repository.countAllOrganizationalCustomers(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public OrganizationalCustomer createOrganizationalCustomer(OrganizationalCustomerCreate creationContainer,
			SecurityContext securityContext) {
		OrganizationalCustomer organizationalCustomer = createOrganizationalCustomerNoMerge(creationContainer, securityContext);
		repository.merge(organizationalCustomer);
		return organizationalCustomer;
	}

	public OrganizationalCustomer createOrganizationalCustomerNoMerge(OrganizationalCustomerCreate creationContainer,
			SecurityContext securityContext) {
		OrganizationalCustomer organizationalCustomer = new OrganizationalCustomer(creationContainer.getName(),securityContext);
		updateOrganizationalCustomerNoMerge(organizationalCustomer, creationContainer);
		return organizationalCustomer;
	}

	private boolean updateOrganizationalCustomerNoMerge(OrganizationalCustomer organizationalCustomer,
			OrganizationalCustomerCreate creationContainer) {
		boolean update = customerService.updateCustomerNoMerge(organizationalCustomer,
				creationContainer);

		if(creationContainer.getOrganization()!=null && (organizationalCustomer.getOrganization()==null||!creationContainer.getOrganization().getId().equals(organizationalCustomer.getOrganization().getId()))){
			organizationalCustomer.setOrganization(creationContainer.getOrganization());
			update=true;
		}


		return update;
	}

	public OrganizationalCustomer updateOrganizationalCustomer(OrganizationalCustomerUpdate updateContainer,
			SecurityContext securityContext) {
		OrganizationalCustomer organizationalCustomer = updateContainer.getOrganizationalCustomer();
		if (updateOrganizationalCustomerNoMerge(organizationalCustomer, updateContainer)) {
			repository.merge(organizationalCustomer);
		}
		return organizationalCustomer;
	}

	public void validate(OrganizationalCustomerCreate creationContainer,
			SecurityContext securityContext) {
		customerService.validate(creationContainer, securityContext);

	}
}