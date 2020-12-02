package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.SalesPerson;
import com.flexicore.organization.model.SalesPersonToRegion;
import com.flexicore.organization.request.SalesPersonCreate;
import com.flexicore.organization.request.SalesPersonFiltering;
import com.flexicore.organization.request.SalesPersonToRegionCreate;
import com.flexicore.organization.request.SalesPersonUpdate;
import com.flexicore.organization.service.SalesPersonService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/SalesPerson")
@Tag(name = "SalesPerson")
@Extension
@Component
public class SalesPersonRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SalesPersonService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "listAllSalesPersons", description = "Lists all SalesPersons")
	@IOperation(Name = "listAllSalesPersons", Description = "Lists all SalesPersons")
	@Path("listAllSalesPersons")
	public PaginationResponse<SalesPerson> listAllSalesPersons(
			@HeaderParam("authenticationKey") String authenticationKey,
			SalesPersonFiltering filtering,
			@Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.listAllSalesPersons(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createSalesPerson")
	@Operation(summary = "createSalesPerson", description = "Creates SalesPerson")
	@IOperation(Name = "createSalesPerson", Description = "Creates SalesPerson")
	public SalesPerson createSalesPerson(
			@HeaderParam("authenticationKey") String authenticationKey,
			SalesPersonCreate creationContainer,
			@Context SecurityContext securityContext) {

		return service.createSalesPerson(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateSalesPerson")
	@Operation(summary = "updateSalesPerson", description = "Updates SalesPerson")
	@IOperation(Name = "updateSalesPerson", Description = "Updates SalesPerson")
	public SalesPerson updateSalesPerson(
			@HeaderParam("authenticationKey") String authenticationKey,
			SalesPersonUpdate updateContainer,
			@Context SecurityContext securityContext) {
		SalesPerson salesPerson = service.getByIdOrNull(
				updateContainer.getId(), SalesPerson.class, null,
				securityContext);
		if (salesPerson == null) {
			throw new BadRequestException("no SalesPerson with id "
					+ updateContainer.getId());
		}
		updateContainer.setSalesPerson(salesPerson);

		return service.updateSalesPerson(updateContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/createSalesPersonToRegion")
	@Operation(summary = "createSalesPersonToRegion", description = "Creates SalesPersonToRegion")
	@IOperation(Name = "createSalesPersonToRegion", Description = "Creates SalesPersonToRegion")
	public SalesPersonToRegion createSalesPersonToRegion(
			@HeaderParam("authenticationKey") String authenticationKey,
			SalesPersonToRegionCreate creationContainer,
			@Context SecurityContext securityContext) {

		service.validate(creationContainer, securityContext);

		return service.createSalesPersonToRegion(creationContainer,
				securityContext);
	}

}