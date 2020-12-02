package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.UnProtectedREST;
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
@UnProtectedREST
@Path("plugins/UnsecureIndustry")
@RequestScoped
@Tag(name = "Industry Unsecure")
@Extension
@Component
public class IndustryUnsecureRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IndustryService service;


	@POST
	@Produces("application/json")
	@Operation(summary = "getAllIndustries", description = "Lists all Industries")
	@IOperation(Name = "getAllIndustries", Description = "Lists all Industries")
	@Path("getAllIndustries")
	public PaginationResponse<Industry> getAllIndustries(
			IndustryFiltering filtering) {

		service.validateFiltering(filtering, null);
		return service.getAllIndustries(null, filtering);
	}


}