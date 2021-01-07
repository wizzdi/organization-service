package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.request.CustomerCreate;
import com.flexicore.organization.request.CustomerFiltering;
import com.flexicore.organization.request.CustomerUpdate;
import com.flexicore.organization.service.CustomerService;
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
@Path("plugins/organization/Customer")
@RequestScoped
@Tag(name = "Customer")
@Extension
@Component
public class CustomerRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private CustomerService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllCustomers", description = "Lists all Customers")
	@IOperation(Name = "getAllCustomers", Description = "Lists all Customers")
	@Path("getAllCustomers")
	public PaginationResponse<Customer> getAllCustomers(
			@HeaderParam("authenticationKey") String authenticationKey,
			CustomerFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllCustomers(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createCustomer")
	@Operation(summary = "createCustomer", description = "Creates Customer")
	@IOperation(Name = "createCustomer", Description = "Creates Customer")
	public Customer createCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			CustomerCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createCustomer(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateCustomer")
	@Operation(summary = "updateCustomer", description = "Updates Customer")
	@IOperation(Name = "updateCustomer", Description = "Updates Customer")
	public Customer updateCustomer(
			@HeaderParam("authenticationKey") String authenticationKey,
			CustomerUpdate updateContainer,
			@Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		Customer customer = service.getByIdOrNull(updateContainer.getId(),
				Customer.class, null, securityContext);
		if (customer == null) {
			throw new BadRequestException("no Customer with id "
					+ updateContainer.getId());
		}
		updateContainer.setCustomer(customer);

		return service.updateCustomer(updateContainer, securityContext);
	}
}