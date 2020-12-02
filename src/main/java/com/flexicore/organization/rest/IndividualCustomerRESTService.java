package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.IndividualCustomer;
import com.flexicore.organization.request.IndividualCustomerCreate;
import com.flexicore.organization.request.IndividualCustomerFiltering;
import com.flexicore.organization.request.IndividualCustomerUpdate;
import com.flexicore.organization.service.IndividualCustomerService;
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
@Path("plugins/IndividualCustomer")
@RequestScoped
@Tag(name = "IndividualCustomer")
@Extension
@Component
public class IndividualCustomerRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IndividualCustomerService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllIndividualCustomers", description = "Lists all IndividualCustomers")
	@IOperation(Name = "getAllIndividualCustomers", Description = "Lists all IndividualCustomers")
	@Path("getAllIndividualCustomers")
	public PaginationResponse<IndividualCustomer> getAllIndividualCustomers(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndividualCustomerFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllIndividualCustomers(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createIndividualCustomer")
	@Operation(summary = "createIndividualCustomer", description = "Creates IndividualCustomer")
	@IOperation(Name = "createIndividualCustomer", Description = "Creates IndividualCustomer")
	public IndividualCustomer createIndividualCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndividualCustomerCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createIndividualCustomer(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateIndividualCustomer")
	@Operation(summary = "updateIndividualCustomer", description = "Updates IndividualCustomer")
	@IOperation(Name = "updateIndividualCustomer", Description = "Updates IndividualCustomer")
	public IndividualCustomer updateIndividualCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndividualCustomerUpdate updateContainer,
			@Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		IndividualCustomer individualCustomer = service.getByIdOrNull(updateContainer.getId(),
				IndividualCustomer.class, null, securityContext);
		if (individualCustomer == null) {
			throw new BadRequestException("no IndividualCustomer with id "
					+ updateContainer.getId());
		}
		updateContainer.setIndividualCustomer(individualCustomer);

		return service.updateIndividualCustomer(updateContainer, securityContext);
	}
}