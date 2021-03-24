package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;


import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.organization.model.CustomerDocument;
import com.flexicore.organization.request.CustomerDocumentCreate;
import com.flexicore.organization.request.CustomerDocumentFiltering;
import com.flexicore.organization.request.CustomerDocumentUpdate;
import com.flexicore.organization.service.CustomerDocumentService;
import com.flexicore.security.SecurityContextBase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;



@OperationsInside

@RequestMapping("plugins/CustomerDocument")

@Tag(name = "CustomerDocument")
@Extension
@Component
public class CustomerDocumentController implements Plugin {


	@Autowired
	private CustomerDocumentService service;


	@Operation(summary = "getAllCustomerDocuments", description = "Lists all CustomerDocuments")
	@IOperation(Name = "getAllCustomerDocuments", Description = "Lists all CustomerDocuments")
	@PostMapping("getAllCustomerDocuments")
	public PaginationResponse<CustomerDocument> getAllCustomerDocuments(

			@RequestBody CustomerDocumentFiltering filtering, @RequestAttribute SecurityContextBase securityContextBase) {
		service.validateFiltering(filtering, securityContextBase);
		return service.getAllCustomerDocuments(securityContextBase, filtering);
	}


	@PostMapping("/createCustomerDocument")
	@Operation(summary = "createCustomerDocument", description = "Creates CustomerDocument")
	@IOperation(Name = "createCustomerDocument", Description = "Creates CustomerDocument")
	public CustomerDocument createCustomerDocument(

			@RequestBody CustomerDocumentCreate creationContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {
		service.validate(creationContainer, securityContextBase);

		return service.createCustomerDocument(creationContainer, securityContextBase);
	}


	@PutMapping("/updateCustomerDocument")
	@Operation(summary = "updateCustomerDocument", description = "Updates CustomerDocument")
	@IOperation(Name = "updateCustomerDocument", Description = "Updates CustomerDocument")
	public CustomerDocument updateCustomerDocument(

			@RequestBody CustomerDocumentUpdate updateContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {
		service.validate(updateContainer, securityContextBase);
		CustomerDocument customerDocument = service.getByIdOrNull(updateContainer.getId(),
				CustomerDocument.class, null, securityContextBase);
		if (customerDocument == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no CustomerDocument with id "
					+ updateContainer.getId());
		}
		updateContainer.setCustomerDocument(customerDocument);

		return service.updateCustomerDocument(updateContainer, securityContextBase);
	}
}