package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.Industry;
import com.flexicore.organization.request.IndustryCreate;
import com.flexicore.organization.request.IndustryFiltering;
import com.flexicore.organization.request.IndustryUpdate;
import com.flexicore.organization.service.IndustryService;
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
@Path("plugins/Industry")
@RequestScoped
@Tag(name = "Industry")
@Extension
@Component
public class IndustryRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IndustryService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllIndustries", description = "Lists all Industries")
	@IOperation(Name = "getAllIndustries", Description = "Lists all Industries")
	@Path("getAllIndustries")
	public PaginationResponse<Industry> getAllIndustries(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndustryFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllIndustries(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createIndustry")
	@Operation(summary = "createIndustry", description = "Creates Industry")
	@IOperation(Name = "createIndustry", Description = "Creates Industry")
	public Industry createIndustry(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndustryCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createIndustry(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateIndustry")
	@Operation(summary = "updateIndustry", description = "Updates Industry")
	@IOperation(Name = "updateIndustry", Description = "Updates Industry")
	public Industry updateIndustry(
			@HeaderParam("authenticationKey") String authenticationKey,
			IndustryUpdate updateContainer,
			@Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		Industry industry = service.getByIdOrNull(updateContainer.getId(),
				Industry.class, null, securityContext);
		if (industry == null) {
			throw new BadRequestException("no Industry with id "
					+ updateContainer.getId());
		}
		updateContainer.setIndustry(industry);

		return service.updateIndustry(updateContainer, securityContext);
	}
}