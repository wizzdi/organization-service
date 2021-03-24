package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;


import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.organization.model.IndustryToCustomer;
import com.flexicore.organization.request.IndustryToCustomerCreate;
import com.flexicore.organization.request.IndustryToCustomerFiltering;
import com.flexicore.organization.request.IndustryToCustomerUpdate;
import com.flexicore.organization.service.IndustryToCustomerService;
import com.flexicore.security.SecurityContextBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;



@OperationsInside

@RequestMapping("plugins/IndustryToCustomer")

@Tag(name = "IndustryToCustomer")
@Extension
@Component
public class IndustryToCustomerController implements Plugin {


	@Autowired
	private IndustryToCustomerService service;


	@Operation(summary = "getAllIndustryToCustomers", description = "Lists all IndustryToCustomers")
	@IOperation(Name = "getAllIndustryToCustomers", Description = "Lists all IndustryToCustomers")
	@PostMapping("getAllIndustryToCustomers")
	public PaginationResponse<IndustryToCustomer> getAllIndustryToCustomers(

			@RequestBody IndustryToCustomerFiltering filtering, @RequestAttribute SecurityContextBase securityContextBase) {
		service.validateFiltering(filtering, securityContextBase);
		return service.getAllIndustryToCustomers(securityContextBase, filtering);
	}


	@PostMapping("/createIndustryToCustomer")
	@Operation(summary = "createIndustryToCustomer", description = "Creates IndustryToCustomer")
	@IOperation(Name = "createIndustryToCustomer", Description = "Creates IndustryToCustomer")
	public IndustryToCustomer createIndustryToCustomer(

			@RequestBody IndustryToCustomerCreate creationContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {
		service.validate(creationContainer, securityContextBase);

		return service.createIndustryToCustomer(creationContainer, securityContextBase);
	}


	@PutMapping("/updateIndustryToCustomer")
	@Operation(summary = "updateIndustryToCustomer", description = "Updates IndustryToCustomer")
	@IOperation(Name = "updateIndustryToCustomer", Description = "Updates IndustryToCustomer")
	public IndustryToCustomer updateIndustryToCustomer(

			@RequestBody IndustryToCustomerUpdate updateContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {
		service.validate(updateContainer, securityContextBase);
		IndustryToCustomer industryToCustomer = service.getByIdOrNull(updateContainer.getId(),
				IndustryToCustomer.class, null, securityContextBase);
		if (industryToCustomer == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no IndustryToCustomer with id "
					+ updateContainer.getId());
		}
		updateContainer.setIndustryToCustomer(industryToCustomer);

		return service.updateIndustryToCustomer(updateContainer, securityContextBase);
	}
}