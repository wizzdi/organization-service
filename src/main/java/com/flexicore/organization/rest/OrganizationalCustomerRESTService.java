package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.OrganizationalCustomer;
import com.flexicore.organization.request.OrganizationalCustomerCreate;
import com.flexicore.organization.request.OrganizationalCustomerFiltering;
import com.flexicore.organization.request.OrganizationalCustomerUpdate;
import com.flexicore.organization.service.OrganizationalCustomerService;
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
@Path("plugins/OrganizationalCustomer")
@RequestScoped
@Tag(name = "OrganizationalCustomer")
@Extension
@Component
public class OrganizationalCustomerRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private OrganizationalCustomerService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllOrganizationalCustomers", description = "Lists all OrganizationalCustomers")
	@IOperation(Name = "getAllOrganizationalCustomers", Description = "Lists all OrganizationalCustomers")
	@Path("getAllOrganizationalCustomers")
	public PaginationResponse<OrganizationalCustomer> getAllOrganizationalCustomers(
			@HeaderParam("authenticationKey") String authenticationKey,
			OrganizationalCustomerFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllOrganizationalCustomers(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createOrganizationalCustomer")
	@Operation(summary = "createOrganizationalCustomer", description = "Creates OrganizationalCustomer")
	@IOperation(Name = "createOrganizationalCustomer", Description = "Creates OrganizationalCustomer")
	public OrganizationalCustomer createOrganizationalCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			OrganizationalCustomerCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createOrganizationalCustomer(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateOrganizationalCustomer")
	@Operation(summary = "updateOrganizationalCustomer", description = "Updates OrganizationalCustomer")
	@IOperation(Name = "updateOrganizationalCustomer", Description = "Updates OrganizationalCustomer")
	public OrganizationalCustomer updateOrganizationalCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			OrganizationalCustomerUpdate updateContainer,
			@Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		OrganizationalCustomer organizationalCustomer = service.getByIdOrNull(updateContainer.getId(),
				OrganizationalCustomer.class, null, securityContext);
		if (organizationalCustomer == null) {
			throw new BadRequestException("no OrganizationalCustomer with id "
					+ updateContainer.getId());
		}
		updateContainer.setOrganizationalCustomer(organizationalCustomer);

		return service.updateOrganizationalCustomer(updateContainer, securityContext);
	}
}