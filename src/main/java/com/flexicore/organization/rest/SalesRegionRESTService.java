package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.SalesRegion;
import com.flexicore.organization.request.SalesRegionCreate;
import com.flexicore.organization.request.SalesRegionFiltering;
import com.flexicore.organization.request.SalesRegionUpdate;
import com.flexicore.organization.service.SalesRegionService;
import com.flexicore.security.SecurityContext;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@RequestScoped
@Path("plugins/SalesRegion")
@Tag(name = "SalesRegion")
@Extension
@Component
public class SalesRegionRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SalesRegionService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "listAllSalesRegions", description = "Lists all SalesRegions")
	@IOperation(Name = "listAllSalesRegions", Description = "Lists all SalesRegions")
	@Path("listAllSalesRegions")
	public PaginationResponse<SalesRegion> listAllSalesRegions(
			@HeaderParam("authenticationKey") String authenticationKey,
			SalesRegionFiltering filtering,
			@Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.listAllSalesRegions(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createSalesRegion")
	@Operation(summary = "createSalesRegion", description = "Creates SalesRegion")
	@IOperation(Name = "createSalesRegion", Description = "Creates SalesRegion")
	public SalesRegion createSalesRegion(
			@HeaderParam("authenticationKey") String authenticationKey,
			SalesRegionCreate creationContainer,
			@Context SecurityContext securityContext) {

		return service.createSalesRegion(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateSalesRegion")
	@Operation(summary = "updateSalesRegion", description = "Updates SalesRegion")
	@IOperation(Name = "updateSalesRegion", Description = "Updates SalesRegion")
	public SalesRegion updateSalesRegion(
			@HeaderParam("authenticationKey") String authenticationKey,
			SalesRegionUpdate updateContainer,
			@Context SecurityContext securityContext) {
		SalesRegion salesRegion = service.getByIdOrNull(
				updateContainer.getId(), SalesRegion.class, null,
				securityContext);
		if (salesRegion == null) {
			throw new BadRequestException("no SalesRegion with id "
					+ updateContainer.getId());
		}
		updateContainer.setSalesRegion(salesRegion);

		return service.updateSalesRegion(updateContainer, securityContext);
	}

}