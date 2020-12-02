package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.CustomerRepository;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.request.CustomerCreate;
import com.flexicore.organization.request.CustomerFiltering;
import com.flexicore.organization.request.CustomerUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class CustomerService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private CustomerRepository repository;

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

	public void validateFiltering(CustomerFiltering filtering,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(filtering, securityContext);

	}

	public PaginationResponse<Customer> getAllCustomers(
			SecurityContext securityContext, CustomerFiltering filtering) {
		List<Customer> list = listAllCustomers(securityContext, filtering);
		long count = repository.countAllCustomers(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public List<Customer> listAllCustomers(SecurityContext securityContext, CustomerFiltering filtering) {
		return repository.getAllCustomers(securityContext, filtering);
	}

	public Customer createCustomer(CustomerCreate creationContainer,
			SecurityContext securityContext) {
		Customer customer = createCustomerNoMerge(creationContainer, securityContext);
		repository.merge(customer);
		return customer;
	}

	public Customer createCustomerNoMerge(CustomerCreate creationContainer,
			SecurityContext securityContext) {
		Customer customer = new Customer(creationContainer.getName(),securityContext);
		updateCustomerNoMerge(customer, creationContainer);
		return customer;
	}

	public boolean updateCustomerNoMerge(Customer customer,
			CustomerCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer,
				customer);
		if(creationContainer.getExternalId()!=null && !creationContainer.getExternalId().equals(customer.getExternalId())){
			customer.setExternalId(creationContainer.getExternalId());
			update=true;
		}

		return update;
	}

	public Customer updateCustomer(CustomerUpdate updateContainer,
			SecurityContext securityContext) {
		Customer customer = updateContainer.getCustomer();
		if (updateCustomerNoMerge(customer, updateContainer)) {
			repository.merge(customer);
		}
		return customer;
	}

	public void validate(CustomerCreate creationContainer,
			SecurityContext securityContext) {
		baseclassNewService.validate(creationContainer, securityContext);

	}
}