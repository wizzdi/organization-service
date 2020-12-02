package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.SupplierRepository;
import com.flexicore.organization.interfaces.ISupplierService;
import com.flexicore.organization.model.Supplier;
import com.flexicore.organization.model.SupplierApi;
import com.flexicore.organization.request.*;
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
public class SupplierService implements ISupplierService {

	@PluginInfo(version = 1)
	@Autowired
	private SupplierRepository repository;
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

	public PaginationResponse<Supplier> listAllSuppliers(
			SecurityContext securityContext, SupplierFiltering filtering) {

		List<Supplier> endpoints = repository.getAllSuppliers(securityContext,
				filtering);
		long count = repository.countAllSuppliers(securityContext, filtering);
		return new PaginationResponse<>(endpoints, filtering, count);
	}

	@Override
	public List<Supplier> getAllSuppliers(SecurityContext securityContext,
			SupplierFiltering filtering) {
		return repository.getAllSuppliers(securityContext, filtering);
	}

	@Override
	public Supplier createSupplierNoMerge(SupplierCreate creationContainer,
			SecurityContext securityContext) {
		Supplier supplier = new Supplier(creationContainer.getName(), securityContext);
		updateSupplierNoMerge(creationContainer, supplier);
		return supplier;

	}

	public Supplier updateSupplier(SupplierUpdate creationContainer,
			SecurityContext securityContext) {
		Supplier Supplier = creationContainer.getSupplier();
		if (updateSupplierNoMerge(creationContainer, Supplier)) {
			repository.merge(Supplier);
		}
		return Supplier;
	}

	@Override
	public void massMerge(List<?> toMerge) {
		repository.massMerge(toMerge);
	}

	@Override
	public boolean updateSupplierNoMerge(SupplierCreate create,
			Supplier Supplier) {
		boolean update = false;
		if (create.getName() != null
				&& !create.getName().equals(Supplier.getName())) {
			Supplier.setName(create.getName());
			update = true;
		}
		if (create.getDescription() != null
				&& !create.getDescription().equals(Supplier.getDescription())) {
			Supplier.setDescription(create.getDescription());
			update = true;
		}

		if (create.getSupplierApi() != null
				&& !(Supplier.getSupplierApi() == null || create
						.getSupplierApi().getId()
						.equals(Supplier.getSupplierApi().getId()))) {
			Supplier.setSupplierApi(create.getSupplierApi());
			update = true;
		}

		return update;
	}

	public void validateFiltering(SupplierFiltering filtering,
			SecurityContext securityContext) {


	}


	public void validateCreate(SupplierCreate creationContainer,
			SecurityContext securityContext) {
		if (creationContainer.getName() == null
				|| creationContainer.getName().isEmpty()) {
			throw new BadRequestException(
					"Supplier name must be non empty and non null");
		}
		SupplierFiltering supplierFiltering = new SupplierFiltering();
		supplierFiltering.setNameLike(creationContainer.getName());
		List<Supplier> suppliers = getAllSuppliers(securityContext,
				supplierFiltering);
		if (!suppliers.isEmpty()) {
			throw new BadRequestException("Supplier with name "
					+ creationContainer.getName() + " already exists");
		}
		String supplierApiId = creationContainer.getSupplierApiId();
		SupplierApi supplierApi = supplierApiId != null ? getByIdOrNull(
				supplierApiId, SupplierApi.class, null, securityContext) : null;
		if (supplierApi == null && supplierApiId != null) {
			throw new BadRequestException("No Supplier api id " + supplierApiId);
		}
		creationContainer.setSupplierApi(supplierApi);

	}

	public void validateUpdate(SupplierUpdate creationContainer,
			SecurityContext securityContext) {

		if (creationContainer.getName() != null) {
			SupplierFiltering supplierFiltering = new SupplierFiltering();
			supplierFiltering.setNameLike(creationContainer.getName());
			List<Supplier> suppliers = getAllSuppliers(securityContext,
					supplierFiltering)
					.parallelStream()
					.filter(f -> !f.getId().equals(
							creationContainer.getSupplier().getId()))
					.collect(Collectors.toList());
			if (!suppliers.isEmpty()) {
				throw new BadRequestException("Supplier with name "
						+ creationContainer.getName() + " already exists");
			}
		}
		String supplierApiId = creationContainer.getSupplierApiId();
		SupplierApi supplierApi = supplierApiId != null ? getByIdOrNull(
				supplierApiId, SupplierApi.class, null, securityContext) : null;
		if (supplierApi == null && supplierApiId != null) {
			throw new BadRequestException("No Supplier api id " + supplierApiId);
		}
		creationContainer.setSupplierApi(supplierApi);

	}

	public Supplier createSupplier(SupplierCreate creationContainer,
								   SecurityContext securityContext) {
		Supplier Supplier = createSupplierNoMerge(creationContainer,
				securityContext);
		repository.merge(Supplier);
		return Supplier;
	}



}