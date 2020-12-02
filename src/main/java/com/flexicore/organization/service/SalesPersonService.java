package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantToUser;
import com.flexicore.organization.data.SalesPersonRepository;
import com.flexicore.organization.model.SalesPerson;
import com.flexicore.organization.model.SalesPersonToRegion;
import com.flexicore.organization.model.SalesRegion;
import com.flexicore.organization.request.SalesPersonCreate;
import com.flexicore.organization.request.SalesPersonFiltering;
import com.flexicore.organization.request.SalesPersonToRegionCreate;
import com.flexicore.organization.request.SalesPersonUpdate;
import com.flexicore.request.TenantToUserCreate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.UserService;

import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class SalesPersonService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SalesPersonRepository repository;
	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public PaginationResponse<SalesPerson> listAllSalesPersons(
			SecurityContext securityContext, SalesPersonFiltering filtering) {

		List<SalesPerson> endpoints = repository.listAllSalesPersons(
				securityContext, filtering);
		long count = repository
				.countAllSalesPersons(securityContext, filtering);
		return new PaginationResponse<>(endpoints, filtering, count);
	}

	public SalesPerson createSalesPerson(SalesPersonCreate creationContainer,
			SecurityContext securityContext) {

		SalesPerson salesPerson = createSalesPersonNoMerge(creationContainer,
				securityContext);
		TenantToUser tenantToUser = userService.createTenantToUserNoMerge(
				new TenantToUserCreate()
						.setUser(salesPerson)
						.setDefaultTenant(true)
						.setTenant(creationContainer.getTenant()),
				securityContext);
		repository.massMerge(Arrays.asList(salesPerson, tenantToUser));
		return salesPerson;
	}

	public SalesPerson createSalesPersonNoMerge(
			SalesPersonCreate creationContainer, SecurityContext securityContext) {
		SalesPerson salesPerson = new SalesPerson(creationContainer.getName(), securityContext);
		updateSalesPersonNoMerge(salesPerson, creationContainer);
		return salesPerson;

	}

	public SalesPerson updateSalesPerson(SalesPersonUpdate creationContainer,
			SecurityContext securityContext) {
		SalesPerson salesPerson = creationContainer.getSalesPerson();
		if (updateSalesPersonNoMerge(salesPerson, creationContainer)) {
			repository.merge(salesPerson);
		}
		return salesPerson;
	}

	public boolean updateSalesPersonNoMerge(SalesPerson salesPerson,
			SalesPersonCreate salesPersonCreate) {
		boolean update = employeeService.updateEmployeeNoMerge(salesPerson,
				salesPersonCreate);
		return update;
	}

	public void validateFiltering(SalesPersonFiltering filtering,
			SecurityContext securityContext) {
		Set<String> regionIds = filtering.getRegionIds();
		Map<String, SalesRegion> salesRegion = regionIds.isEmpty()
				? new HashMap<>()
				: listByIds(SalesRegion.class, regionIds, securityContext)
						.parallelStream().collect(
								Collectors.toMap(f -> f.getId(), f -> f));
		regionIds.removeAll(salesRegion.keySet());
		if (!salesRegion.isEmpty()) {
			throw new BadRequestException("No Sales Region with ids "
					+ regionIds);
		}
		filtering.setSalesRegions(new ArrayList<>(salesRegion.values()));
	}

	public void validate(SalesPersonToRegionCreate creationContainer,
			SecurityContext securityContext) {
		String salesPersonId = creationContainer.getSalesPersonId();
		SalesPerson salesPerson = salesPersonId == null ? null : getByIdOrNull(
				salesPersonId, SalesPerson.class, null, securityContext);
		if (salesPerson == null && salesPersonId != null) {
			throw new BadRequestException("No SalesPerson with id "
					+ salesPersonId);
		}
		creationContainer.setSalesPerson(salesPerson);

		String salesRegionId = creationContainer.getSalesRegionId();
		SalesRegion salesRegion = salesRegionId == null ? null : getByIdOrNull(
				salesRegionId, SalesRegion.class, null, securityContext);
		if (salesRegion == null && salesRegionId != null) {
			throw new BadRequestException("No SalesRegion with id "
					+ salesRegionId);
		}
		creationContainer.setSalesRegion(salesRegion);
	}

	public SalesPersonToRegion createSalesPersonToRegion(
			SalesPersonToRegionCreate creationContainer,
			SecurityContext securityContext) {
		SalesPersonToRegion salesPersonToRegion = createSalesPersonToRegionNoMerge(
				creationContainer, securityContext);
		if (creationContainer.getStartTime() == null) {
			creationContainer.setStartTime(LocalDateTime.now());
		}
		repository.merge(salesPersonToRegion);
		return salesPersonToRegion;
	}

	private SalesPersonToRegion createSalesPersonToRegionNoMerge(
			SalesPersonToRegionCreate creationContainer,
			SecurityContext securityContext) {
		SalesPersonToRegion salesPersonToRegion = new SalesPersonToRegion("link", securityContext);
		salesPersonToRegion.setLeftside(creationContainer.getSalesPerson());
		salesPersonToRegion.setRightside(creationContainer.getSalesRegion());
		updateSalesPersonToRegionNoMerge(creationContainer, salesPersonToRegion);
		return salesPersonToRegion;
	}

	private boolean updateSalesPersonToRegionNoMerge(
			SalesPersonToRegionCreate creationContainer,
			SalesPersonToRegion salesPersonToRegion) {
		boolean update = false;
		if (creationContainer.getStartTime() != null
				&& !creationContainer.getStartTime().equals(
						salesPersonToRegion.getStartTime())) {
			creationContainer.setStartTime(creationContainer.getStartTime());
			update = true;
		}

		if (creationContainer.getEndTime() != null
				&& !creationContainer.getEndTime().equals(
						salesPersonToRegion.getEndTime())) {
			creationContainer.setEndTime(creationContainer.getEndTime());
			update = true;
		}

		return update;
	}

}