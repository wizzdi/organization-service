package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;


import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.organization.model.Supplier;
import com.flexicore.organization.request.SupplierCreate;
import com.flexicore.organization.request.SupplierFiltering;
import com.flexicore.organization.request.SupplierUpdate;
import com.flexicore.organization.service.SupplierService;
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


@RequestMapping("plugins/Supplier")
@Tag(name = "Supplier")
@Extension
@Component
public class SupplierController implements Plugin {


	@Autowired
	private SupplierService service;


	@Operation(summary = "getAllSuppliers", description = "Lists all Suppliers")
	@IOperation(Name = "getAllSuppliers", Description = "Lists all Suppliers")
	@PostMapping("getAllSuppliers")
	public PaginationResponse<Supplier> listAllSuppliers(

			@RequestBody SupplierFiltering filtering,
			@RequestAttribute SecurityContextBase securityContextBase) {
		service.validateFiltering(filtering, securityContextBase);
		return service.listAllSuppliers(securityContextBase, filtering);
	}



	@PostMapping("/createSupplier")
	@Operation(summary = "createSupplier", description = "Creates Supplier")
	@IOperation(Name = "createSupplier", Description = "Creates Supplier")
	public Supplier createSupplier(

			@RequestBody SupplierCreate creationContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {

		service.validateCreate(creationContainer, securityContextBase);

		return service.createSupplier(creationContainer, securityContextBase);
	}


	@PutMapping("/updateSupplier")
	@Operation(summary = "updateSupplier", description = "Updates Supplier")
	@IOperation(Name = "updateSupplier", Description = "Updates Supplier")
	public Supplier updateSupplier(

			@RequestBody SupplierUpdate updateContainer,
			@RequestAttribute SecurityContextBase securityContextBase) {
		Supplier Supplier = service.getByIdOrNull(updateContainer.getId(),
				Supplier.class, null, securityContextBase);
		if (Supplier == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Supplier with id "
					+ updateContainer.getId());
		}
		updateContainer.setSupplier(Supplier);
		service.validateUpdate(updateContainer, securityContextBase);

		return service.updateSupplier(updateContainer, securityContextBase);
	}


}