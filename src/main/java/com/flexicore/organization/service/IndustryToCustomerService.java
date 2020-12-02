package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.organization.data.IndustryToCustomerRepository;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.Industry;
import com.flexicore.organization.model.IndustryToCustomer;
import com.flexicore.organization.request.IndustryToCustomerCreate;
import com.flexicore.organization.request.IndustryToCustomerFiltering;
import com.flexicore.organization.request.IndustryToCustomerUpdate;
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
public class IndustryToCustomerService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IndustryToCustomerRepository repository;

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

	public void validateFiltering(IndustryToCustomerFiltering filtering,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(filtering, securityContext);
		Set<String> customerIds = filtering.getCustomerIds();
		Map<String, Customer> customers = customerIds.isEmpty() ? new HashMap<>() : listByIds(Customer.class, customerIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		customerIds.removeAll(customers.keySet());
		if (!customerIds.isEmpty()) { throw new BadRequestException("No Organization with ids " + customerIds);
		}
		filtering.setCustomers(new ArrayList<>(customers.values()));
	}

	public PaginationResponse<IndustryToCustomer> getAllIndustryToCustomers(SecurityContext securityContext, IndustryToCustomerFiltering filtering) {
		List<IndustryToCustomer> list = repository.getAllIndustryToCustomers(securityContext, filtering);
		long count = repository.countAllIndustryToCustomers(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public IndustryToCustomer createIndustryToCustomer(IndustryToCustomerCreate creationContainer,
			SecurityContext securityContext) {
		IndustryToCustomer industryToCustomer = createIndustryToCustomerNoMerge(creationContainer, securityContext);
		repository.merge(industryToCustomer);
		return industryToCustomer;
	}

	public IndustryToCustomer createIndustryToCustomerNoMerge(IndustryToCustomerCreate creationContainer,
			SecurityContext securityContext) {
		IndustryToCustomer industryToCustomer = new IndustryToCustomer(creationContainer.getName(),securityContext);
		updateIndustryToCustomerNoMerge(industryToCustomer, creationContainer);
		return industryToCustomer;
	}

	private boolean updateIndustryToCustomerNoMerge(IndustryToCustomer industryToCustomer,
			IndustryToCustomerCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer, industryToCustomer);

		if (creationContainer.getCustomer() != null && (industryToCustomer.getCustomer() == null || !creationContainer.getCustomer().getId().equals(industryToCustomer.getCustomer().getId()))) {
			industryToCustomer.setCustomer(creationContainer.getCustomer());
			update = true;
		}

		if (creationContainer.getIndustry() != null && (industryToCustomer.getIndustry() == null || !creationContainer.getIndustry().getId().equals(industryToCustomer.getIndustry().getId()))) {
			industryToCustomer.setIndustry(creationContainer.getIndustry());
			update = true;
		}
		return update;
	}

	public IndustryToCustomer updateIndustryToCustomer(IndustryToCustomerUpdate updateContainer,
			SecurityContext securityContext) {
		IndustryToCustomer industryToCustomer = updateContainer.getIndustryToCustomer();
		if (updateIndustryToCustomerNoMerge(industryToCustomer, updateContainer)) {
			repository.merge(industryToCustomer);
		}
		return industryToCustomer;
	}

	public void validate(IndustryToCustomerCreate creationContainer,
			SecurityContext securityContext) {
		baseclassNewService.validate(creationContainer, securityContext);
		String customerId = creationContainer.getCustomerId();
		Customer customer = customerId == null ? null : getByIdOrNull(customerId, Customer.class, null, securityContext);
		if (customer == null && customerId != null) {
			throw new BadRequestException("No Customer with id " + customerId);
		}
		creationContainer.setCustomer(customer);

		String industryId = creationContainer.getIndustryId();
		Industry industry = industryId == null ? null : getByIdOrNull(industryId, Industry.class, null, securityContext);
		if (industry == null && industryId != null) {
			throw new BadRequestException("No Industry with id " + industryId);
		}
		creationContainer.setIndustry(industry);
	}
}