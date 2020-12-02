package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.Supplier;
import com.flexicore.organization.request.SupplierCreate;
import com.flexicore.organization.request.SupplierFiltering;
import com.flexicore.organization.request.SupplierUpdate;
import com.flexicore.organization.service.SupplierService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@RequestScoped
@Path("plugins/Supplier")
@Tag(name = "Supplier")
@Extension
@Component
public class SupplierRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SupplierService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllSuppliers", description = "Lists all Suppliers")
	@IOperation(Name = "getAllSuppliers", Description = "Lists all Suppliers")
	@Path("getAllSuppliers")
	public PaginationResponse<Supplier> listAllSuppliers(
			@HeaderParam("authenticationKey") String authenticationKey,
			SupplierFiltering filtering,
			@Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.listAllSuppliers(securityContext, filtering);
	}


	@POST
	@Produces("application/json")
	@Path("/createSupplier")
	@Operation(summary = "createSupplier", description = "Creates Supplier")
	@IOperation(Name = "createSupplier", Description = "Creates Supplier")
	public Supplier createSupplier(
			@HeaderParam("authenticationKey") String authenticationKey,
			SupplierCreate creationContainer,
			@Context SecurityContext securityContext) {

		service.validateCreate(creationContainer, securityContext);

		return service.createSupplier(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateSupplier")
	@Operation(summary = "updateSupplier", description = "Updates Supplier")
	@IOperation(Name = "updateSupplier", Description = "Updates Supplier")
	public Supplier updateSupplier(
			@HeaderParam("authenticationKey") String authenticationKey,
			SupplierUpdate updateContainer,
			@Context SecurityContext securityContext) {
		Supplier Supplier = service.getByIdOrNull(updateContainer.getId(),
				Supplier.class, null, securityContext);
		if (Supplier == null) {
			throw new BadRequestException("no Supplier with id "
					+ updateContainer.getId());
		}
		updateContainer.setSupplier(Supplier);
		service.validateUpdate(updateContainer, securityContext);

		return service.updateSupplier(updateContainer, securityContext);
	}


}