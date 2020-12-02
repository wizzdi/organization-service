package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.SalesRegionRepository;
import com.flexicore.organization.model.SalesRegion;
import com.flexicore.organization.request.SalesRegionCreate;
import com.flexicore.organization.request.SalesRegionFiltering;
import com.flexicore.organization.request.SalesRegionUpdate;
import com.flexicore.security.SecurityContext;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class SalesRegionService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SalesRegionRepository repository;
	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private EmployeeService employeeService;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public PaginationResponse<SalesRegion> listAllSalesRegions(
			SecurityContext securityContext, SalesRegionFiltering filtering) {

		List<SalesRegion> endpoints = repository.listAllSalesRegions(
				securityContext, filtering);
		long count = repository
				.countAllSalesRegions(securityContext, filtering);
		return new PaginationResponse<>(endpoints, filtering, count);
	}

	public SalesRegion createSalesRegion(SalesRegionCreate creationContainer,
			SecurityContext securityContext) {

		SalesRegion salesRegion = createSalesRegionNoMerge(creationContainer,
				securityContext);
		repository.merge(salesRegion);
		return salesRegion;
	}

	public SalesRegion createSalesRegionNoMerge(
			SalesRegionCreate creationContainer, SecurityContext securityContext) {
		SalesRegion salesRegion = new SalesRegion(creationContainer.getName(),securityContext);
		updateSalesRegionNoMerge(salesRegion, creationContainer);
		return salesRegion;

	}

	public SalesRegion updateSalesRegion(SalesRegionUpdate creationContainer,
			SecurityContext securityContext) {
		SalesRegion salesRegion = creationContainer.getSalesRegion();
		if (updateSalesRegionNoMerge(salesRegion, creationContainer)) {
			repository.merge(salesRegion);
		}
		return salesRegion;
	}

	public boolean updateSalesRegionNoMerge(SalesRegion salesRegion,
			SalesRegionCreate salesRegionCreate) {
		boolean update = false;
		if (salesRegionCreate.getName() != null
				&& !salesRegionCreate.getName().equals(salesRegion.getName())) {
			salesRegion.setName(salesRegionCreate.getName());
			update = true;
		}

		if (salesRegionCreate.getDescription() != null
				&& !salesRegionCreate.getDescription().equals(
						salesRegion.getDescription())) {
			salesRegion.setDescription(salesRegionCreate.getDescription());
			update = true;
		}
		return update;
	}

	public void validateFiltering(SalesRegionFiltering filtering,
			SecurityContext securityContext) {
	}

}