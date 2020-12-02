package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.SupplierApiRepository;
import com.flexicore.organization.interfaces.ISupplierApiService;
import com.flexicore.organization.model.SupplierApi;

import com.flexicore.organization.request.SupplierApiCreate;
import com.flexicore.organization.request.SupplierApiFiltering;
import com.flexicore.organization.request.SupplierApiUpdate;
import com.flexicore.security.SecurityContext;

import javax.ws.rs.BadRequestException;
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
public class SupplierApiService implements ISupplierApiService {

	@PluginInfo(version = 1)
	@Autowired
	private SupplierApiRepository repository;
	@Autowired
	private Logger logger;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public PaginationResponse<SupplierApi> listAllSupplierApis(
			SecurityContext securityContext, SupplierApiFiltering filtering) {

		List<SupplierApi> endpoints = repository.getAllSupplierApis(
				securityContext, filtering);
		long count = repository
				.countAllSupplierApis(securityContext, filtering);
		return new PaginationResponse<>(endpoints, filtering, count);
	}

	@Override
	public List<SupplierApi> getAllSupplierApis(
			SecurityContext securityContext, SupplierApiFiltering filtering) {
		return repository.getAllSupplierApis(securityContext, filtering);
	}

	@Override
	public SupplierApi createSupplierApi(SupplierApiCreate creationContainer,
			SecurityContext securityContext) {
		SupplierApi SupplierApi = createSupplierApiNoMerge(creationContainer,
				securityContext);
		repository.merge(SupplierApi);
		return SupplierApi;
	}

	@Override
	public SupplierApi createSupplierApiNoMerge(
			SupplierApiCreate creationContainer, SecurityContext securityContext) {
		SupplierApi supplierApi = new SupplierApi(
				creationContainer.getName(), securityContext);
		updateSupplierApiNoMerge(creationContainer, supplierApi);
		return supplierApi;

	}

	@Override
	public SupplierApi updateSupplierApi(SupplierApiUpdate creationContainer,
			SecurityContext securityContext) {
		SupplierApi SupplierApi = creationContainer.getSupplierApi();
		if (updateSupplierApiNoMerge(creationContainer, SupplierApi)) {
			repository.merge(SupplierApi);
		}
		return SupplierApi;
	}

	@Override
	public void massMerge(List<?> toMerge) {
		repository.massMerge(toMerge);
	}

	@Override
	public boolean updateSupplierApiNoMerge(SupplierApiCreate create,
			SupplierApi SupplierApi) {
		boolean update = false;
		if (create.getName() != null
				&& !create.getName().equals(SupplierApi.getName())) {
			SupplierApi.setName(create.getName());
			update = true;
		}
		if (create.getDescription() != null
				&& !create.getDescription()
						.equals(SupplierApi.getDescription())) {
			SupplierApi.setDescription(create.getDescription());
			update = true;
		}

		if (create.getImplementorCanonicalName() != null
				&& !create.getImplementorCanonicalName().equals(
						SupplierApi.getImplementorCanonicalName())) {
			SupplierApi.setImplementorCanonicalName(create
					.getImplementorCanonicalName());
			update = true;
		}

		return update;
	}

	public void validateFiltering(SupplierApiFiltering filtering,
			SecurityContext securityContext) {

	}

	@Override
	public void validateCreate(SupplierApiCreate creationContainer,
			SecurityContext securityContext) {
		if (creationContainer.getName() == null
				|| creationContainer.getName().isEmpty()) {
			throw new BadRequestException(
					"SupplierApi name must be non empty and non null");
		}
		SupplierApiFiltering supplierApiFiltering = new SupplierApiFiltering();
		supplierApiFiltering.setNameLike(creationContainer.getName());
		List<SupplierApi> supplierApis = getAllSupplierApis(securityContext,
				supplierApiFiltering);
		if (!supplierApis.isEmpty()) {
			throw new BadRequestException("SupplierApi with name "
					+ creationContainer.getName() + " already exists");
		}

	}

	@Override
	public void validateUpdate(SupplierApiUpdate creationContainer,
			SecurityContext securityContext) {

		if (creationContainer.getName() != null) {
			SupplierApiFiltering supplierApiFiltering = new SupplierApiFiltering();
			supplierApiFiltering.setNameLike(creationContainer.getName());
			List<SupplierApi> supplierApis = getAllSupplierApis(
					securityContext, supplierApiFiltering)
					.parallelStream()
					.filter(f -> !f.getId().equals(
							creationContainer.getSupplierApi().getId()))
					.collect(Collectors.toList());
			if (!supplierApis.isEmpty()) {
				throw new BadRequestException("SupplierApi with name "
						+ creationContainer.getName() + " already exists");
			}
		}

	}

}