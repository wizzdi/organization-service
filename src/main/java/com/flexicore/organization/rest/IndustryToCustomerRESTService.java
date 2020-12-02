package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.IndustryToCustomer;
import com.flexicore.organization.request.IndustryToCustomerCreate;
import com.flexicore.organization.request.IndustryToCustomerFiltering;
import com.flexicore.organization.request.IndustryToCustomerUpdate;
import com.flexicore.organization.service.IndustryToCustomerService;
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
@Path("plugins/IndustryToCustomer")
@RequestScoped
@Tag(name = "IndustryToCustomer")
@Extension
@Component
public class IndustryToCustomerRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IndustryToCustomerService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllIndustryToCustomers", description = "Lists all IndustryToCustomers")
	@IOperation(Name = "getAllIndustryToCustomers", Description = "Lists all IndustryToCustomers")
	@Path("getAllIndustryToCustomers")
	public PaginationResponse<IndustryToCustomer> getAllIndustryToCustomers(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndustryToCustomerFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllIndustryToCustomers(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createIndustryToCustomer")
	@Operation(summary = "createIndustryToCustomer", description = "Creates IndustryToCustomer")
	@IOperation(Name = "createIndustryToCustomer", Description = "Creates IndustryToCustomer")
	public IndustryToCustomer createIndustryToCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndustryToCustomerCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createIndustryToCustomer(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateIndustryToCustomer")
	@Operation(summary = "updateIndustryToCustomer", description = "Updates IndustryToCustomer")
	@IOperation(Name = "updateIndustryToCustomer", Description = "Updates IndustryToCustomer")
	public IndustryToCustomer updateIndustryToCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndustryToCustomerUpdate updateContainer,
			@Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		IndustryToCustomer industryToCustomer = service.getByIdOrNull(updateContainer.getId(),
				IndustryToCustomer.class, null, securityContext);
		if (industryToCustomer == null) {
			throw new BadRequestException("no IndustryToCustomer with id "
					+ updateContainer.getId());
		}
		updateContainer.setIndustryToCustomer(industryToCustomer);

		return service.updateIndustryToCustomer(updateContainer, securityContext);
	}
}