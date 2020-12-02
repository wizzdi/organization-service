package com.flexicore.organization.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.organization.model.Employee;
import com.flexicore.organization.request.EmployeeCreate;
import com.flexicore.organization.request.EmployeeFiltering;
import com.flexicore.organization.request.EmployeeUpdate;
import com.flexicore.organization.service.EmployeeService;
import com.flexicore.security.SecurityContext;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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
@Path("plugins/Employee")
@RequestScoped
@Tag(name = "Employee")
@Extension
@Component
public class EmployeeRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private EmployeeService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "listAllEmployees", description = "Lists all Employees")
	@IOperation(Name = "listAllEmployees", Description = "Lists all Employees")
	@Path("listAllEmployees")
	public PaginationResponse<Employee> listAllEmployees(
			@HeaderParam("authenticationKey") String authenticationKey,
			EmployeeFiltering filtering,
			@Context SecurityContext securityContext) {
		service.validateFiltering(filtering, securityContext);
		return service.listAllEmployees(securityContext, filtering);
	}

	@POST
	@Produces("application/json")
	@Path("/createEmployee")
	@Operation(summary = "createEmployee", description = "Creates Employee")
	@IOperation(Name = "createEmployee", Description = "Creates Employee")
	public Employee createEmployee(
			@HeaderParam("authenticationKey") String authenticationKey,
			EmployeeCreate creationContainer,
			@Context SecurityContext securityContext) {

		return service.createEmployee(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/updateEmployee")
	@Operation(summary = "updateEmployee", description = "Updates Employee")
	@IOperation(Name = "updateEmployee", Description = "Updates Employee")
	public Employee updateEmployee(
			@HeaderParam("authenticationKey") String authenticationKey,
			EmployeeUpdate updateContainer,
			@Context SecurityContext securityContext) {
		Employee employee = service.getByIdOrNull(updateContainer.getId(),
				Employee.class, null, securityContext);
		if (employee == null) {
			throw new BadRequestException("no Employee with id "
					+ updateContainer.getId());
		}
		updateContainer.setEmployee(employee);

		return service.updateEmployee(updateContainer, securityContext);
	}

}