package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.Site;
import com.flexicore.organization.request.SiteCreate;
import com.flexicore.organization.request.SiteFiltering;
import com.flexicore.organization.request.SiteUpdate;
import com.flexicore.organization.service.SiteService;
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
@Path("plugins/Site")
@Tag(name = "Site")
@Extension
@Component
public class SiteRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private SiteService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllSites", description = "Lists all Sites")
	@IOperation(Name = "getAllSites", Description = "Lists all Sites")
	@Path("getAllSites")
	public PaginationResponse<Site> getAllSites(
			@HeaderParam("authenticationKey") String authenticationKey,
			SiteFiltering filtering, @Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.getAllSites(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createSite")
	@Operation(summary = "createSite", description = "Creates Site")
	@IOperation(Name = "createSite", Description = "Creates Site")
	public Site createSite(
			@HeaderParam("authenticationKey") String authenticationKey,
			SiteCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);

		return service.createSite(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateSite")
	@Operation(summary = "updateSite", description = "Updates Site")
	@IOperation(Name = "updateSite", Description = "Updates Site")
	public Site updateSite(
			@HeaderParam("authenticationKey") String authenticationKey,
			SiteUpdate updateContainer, @Context SecurityContext securityContext) {
		service.validate(updateContainer, securityContext);
		Site site = service.getByIdOrNull(updateContainer.getId(), Site.class,
				null, securityContext);
		if (site == null) {
			throw new BadRequestException("no Site with id "
					+ updateContainer.getId());
		}
		updateContainer.setSite(site);

		return service.updateSite(updateContainer, securityContext);
	}
}