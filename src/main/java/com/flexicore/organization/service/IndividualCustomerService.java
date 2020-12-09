package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.IndividualCustomerRepository;
import com.flexicore.organization.model.IndividualCustomer;
import com.flexicore.organization.request.IndividualCustomerCreate;
import com.flexicore.organization.request.IndividualCustomerFiltering;
import com.flexicore.organization.request.IndividualCustomerUpdate;
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
public class IndividualCustomerService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IndividualCustomerRepository repository;

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

	public void validateFiltering(IndividualCustomerFiltering filtering,
			SecurityContext securityContext) {
		customerService.validateFiltering(filtering, securityContext);

	}

	public PaginationResponse<IndividualCustomer> getAllIndividualCustomers(
			SecurityContext securityContext, IndividualCustomerFiltering filtering) {
		List<IndividualCustomer> list = repository.getAllIndividualCustomers(securityContext, filtering);
		long count = repository.countAllIndividualCustomers(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public IndividualCustomer createIndividualCustomer(IndividualCustomerCreate creationContainer,
			SecurityContext securityContext) {
		IndividualCustomer individualCustomer = createIndividualCustomerNoMerge(creationContainer, securityContext);
		repository.merge(individualCustomer);
		return individualCustomer;
	}

	public IndividualCustomer createIndividualCustomerNoMerge(IndividualCustomerCreate creationContainer,
			SecurityContext securityContext) {
		IndividualCustomer individualCustomer = new IndividualCustomer(creationContainer.getName(),securityContext);
		updateIndividualCustomerNoMerge(individualCustomer, creationContainer);
		return individualCustomer;
	}

	public boolean updateIndividualCustomerNoMerge(IndividualCustomer individualCustomer,
			IndividualCustomerCreate creationContainer) {
		boolean update = customerService.updateCustomerNoMerge(individualCustomer,
				creationContainer);


		return update;
	}

	public IndividualCustomer updateIndividualCustomer(IndividualCustomerUpdate updateContainer,
			SecurityContext securityContext) {
		IndividualCustomer individualCustomer = updateContainer.getIndividualCustomer();
		if (updateIndividualCustomerNoMerge(individualCustomer, updateContainer)) {
			repository.merge(individualCustomer);
		}
		return individualCustomer;
	}

	public void validate(IndividualCustomerCreate creationContainer,
			SecurityContext securityContext) {
		customerService.validate(creationContainer, securityContext);

	}
}