package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;

import com.wizzdi.flexicore.security.response.PaginationResponse;


import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.organization.model.SalesRegion;
import com.flexicore.organization.request.SalesRegionCreate;
import com.flexicore.organization.request.SalesRegionFiltering;
import com.flexicore.organization.request.SalesRegionUpdate;
import com.flexicore.organization.service.SalesRegionService;
import com.flexicore.security.SecurityContextBase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import org.pf4j.Extension;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@OperationsInside


@RequestMapping("plugins/SalesRegion")
@Tag(name = "SalesRegion")
@Extension
@Component
public class SalesRegionController implements Plugin {


	@Autowired
	private SalesRegionService service;


	@Operation(summary = "listAllSalesRegions", description = "Lists all SalesRegions")
	@IOperation(Name = "listAllSalesRegions", Description = "Lists all SalesRegions")
	@PostMapping("listAllSalesRegions")
	public PaginationResponse<SalesRegion> listAllSalesRegions(

			@RequestBody SalesRegionFiltering filtering,
			@RequestAttribute SecurityContextBase securityContextBase) {
		service.validateFiltering(filtering, securityContextBase);
		return service.listAllSalesRegions(securityContextBase, filtering);
	}


	@PostMapping("/createSalesRegion")
	@Operation(summary = "createSalesRegion", description = "Creates SalesRegion")
	@IOperation(Name = "createSalesRegion", Description = "Creates SalesRegion")
	public SalesRegion createSalesRegion(

			@RequestBody SalesRegionCreate creationContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {

		return service.createSalesRegion(creationContainer, securityContextBase);
	}


	@PutMapping("/updateSalesRegion")
	@Operation(summary = "updateSalesRegion", description = "Updates SalesRegion")
	@IOperation(Name = "updateSalesRegion", Description = "Updates SalesRegion")
	public SalesRegion updateSalesRegion(

			@RequestBody SalesRegionUpdate updateContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {
		SalesRegion salesRegion = service.getByIdOrNull(
				updateContainer.getId(), SalesRegion.class, null,
				securityContextBase);
		if (salesRegion == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no SalesRegion with id "
					+ updateContainer.getId());
		}
		updateContainer.setSalesRegion(salesRegion);

		return service.updateSalesRegion(updateContainer, securityContextBase);
	}

}