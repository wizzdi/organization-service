package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.CustomerDocument;
import com.flexicore.organization.request.CustomerDocumentCreate;
import com.flexicore.organization.request.CustomerDocumentFiltering;
import com.flexicore.organization.request.CustomerDocumentUpdate;
import com.flexicore.organization.service.CustomerDocumentService;
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
@Path("plugins/CustomerDocument")
@RequestScoped
@Tag(name = "CustomerDocument")
@Extension
@Component
public class CustomerDocumentRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private CustomerDocumentService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllCustomerDocuments", description = "Lists all CustomerDocuments")
	@IOperation(Name = "getAllCustomerDocuments", Description = "Lists all CustomerDocuments")
	@Path("getAllCustomerDocuments")
	public PaginationResponse<CustomerDocument> getAllCustomerDocuments(
			@HeaderParam("authenticationKey") String authenticationKey,
			CustomerDocumentFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllCustomerDocuments(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createCustomerDocument")
	@Operation(summary = "createCustomerDocument", description = "Creates CustomerDocument")
	@IOperation(Name = "createCustomerDocument", Description = "Creates CustomerDocument")
	public CustomerDocument createCustomerDocument(
			@HeaderParam("authenticationKey") String authenticationKey,
			CustomerDocumentCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createCustomerDocument(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateCustomerDocument")
	@Operation(summary = "updateCustomerDocument", description = "Updates CustomerDocument")
	@IOperation(Name = "updateCustomerDocument", Description = "Updates CustomerDocument")
	public CustomerDocument updateCustomerDocument(
			@HeaderParam("authenticationKey") String authenticationKey,
			CustomerDocumentUpdate updateContainer,
			@Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		CustomerDocument customerDocument = service.getByIdOrNull(updateContainer.getId(),
				CustomerDocument.class, null, securityContext);
		if (customerDocument == null) {
			throw new BadRequestException("no CustomerDocument with id "
					+ updateContainer.getId());
		}
		updateContainer.setCustomerDocument(customerDocument);

		return service.updateCustomerDocument(updateContainer, securityContext);
	}
}