package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.Branch;
import com.flexicore.organization.request.BranchCreate;
import com.flexicore.organization.request.BranchFiltering;
import com.flexicore.organization.request.BranchUpdate;
import com.flexicore.organization.service.BranchService;
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
@Path("plugins/Branch")
@RequestScoped
@Tag(name = "Branch")
@Extension
@Component
public class BranchRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private BranchService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllBranches", description = "Lists all Branchs")
	@IOperation(Name = "getAllBranches", Description = "Lists all Branchs")
	@Path("getAllBranches")
	public PaginationResponse<Branch> getAllBranchs(
			@HeaderParam("authenticationKey") String authenticationKey,
			BranchFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllBranches(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createBranch")
	@Operation(summary = "createBranch", description = "Creates Branch")
	@IOperation(Name = "createBranch", Description = "Creates Branch")
	public Branch createBranch(
			@HeaderParam("authenticationKey") String authenticationKey,
			BranchCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createBranch(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateBranch")
	@Operation(summary = "updateBranch", description = "Updates Branch")
	@IOperation(Name = "updateBranch", Description = "Updates Branch")
	public Branch updateBranch(
			@HeaderParam("authenticationKey") String authenticationKey,
			BranchUpdate updateContainer,
			@Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		Branch branch = service.getByIdOrNull(updateContainer.getId(),
				Branch.class, null, securityContext);
		if (branch == null) {
			throw new BadRequestException("no Branch with id "
					+ updateContainer.getId());
		}
		updateContainer.setBranch(branch);

		return service.updateBranch(updateContainer, securityContext);
	}
}