package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.IndustryRepository;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.Industry;
import com.flexicore.organization.request.IndustryCreate;
import com.flexicore.organization.request.IndustryFiltering;
import com.flexicore.organization.request.IndustryUpdate;
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
public class IndustryService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IndustryRepository repository;

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

	public void validateFiltering(IndustryFiltering filtering,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(filtering, securityContext);
		Set<String> customerIds = filtering.getCustomerIds();
		Map<String, Customer> customers = customerIds.isEmpty() ? new HashMap<>() : listByIds(Customer.class, customerIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		customerIds.removeAll(customers.keySet());
		if (!customerIds.isEmpty()) { throw new BadRequestException("No Organization with ids " + customerIds);
		}
		filtering.setCustomers(new ArrayList<>(customers.values()));
	}

	public PaginationResponse<Industry> getAllIndustries(SecurityContext securityContext, IndustryFiltering filtering) {
		List<Industry> list = repository.getAllIndustries(securityContext, filtering);
		long count = repository.countAllIndustries(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public Industry createIndustry(IndustryCreate creationContainer,
			SecurityContext securityContext) {
		Industry industry = createIndustryNoMerge(creationContainer, securityContext);
		repository.merge(industry);
		return industry;
	}

	private Industry createIndustryNoMerge(IndustryCreate creationContainer,
			SecurityContext securityContext) {
		Industry industry = new Industry(creationContainer.getName(),securityContext);
		updateIndustryNoMerge(industry, creationContainer);
		return industry;
	}

	private boolean updateIndustryNoMerge(Industry industry,
			IndustryCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer, industry);

		return update;
	}

	public Industry updateIndustry(IndustryUpdate updateContainer,
			SecurityContext securityContext) {
		Industry industry = updateContainer.getIndustry();
		if (updateIndustryNoMerge(industry, updateContainer)) {
			repository.merge(industry);
		}
		return industry;
	}

	public void validate(IndustryCreate creationContainer,
			SecurityContext securityContext) {
		baseclassNewService.validate(creationContainer, securityContext);
	}
}