package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.organization.model.SalesPersonToRegion;
import com.flexicore.organization.request.SalesPersonToRegionCreate;
import com.flexicore.organization.service.SalesPersonToRegionService;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@OperationsInside
@RequestMapping("plugins/SalesPersonToRegion")
@Tag(name = "SalesPersonToRegion")
@Extension
@Component
public class SalesPersonToRegionController implements Plugin {


	@Autowired
	private SalesPersonToRegionService service;


	@PostMapping("/createSalesPersonToRegion")
	@Operation(summary = "createSalesPersonToRegion", description = "Creates SalesPersonToRegion")
	@IOperation(Name = "createSalesPersonToRegion", Description = "Creates SalesPersonToRegion")
	public SalesPersonToRegion createSalesPersonToRegion(

			@RequestBody  SalesPersonToRegionCreate creationContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {

		service.validate(creationContainer, securityContextBase);

		return service.createSalesPersonToRegion(creationContainer,
				securityContextBase);
	}

}