package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantToUser;
import com.flexicore.organization.data.EmployeeRepository;
import com.flexicore.organization.interfaces.IEmployeeService;
import com.flexicore.organization.model.Employee;
import com.flexicore.organization.request.EmployeeCreate;
import com.flexicore.organization.request.EmployeeFiltering;
import com.flexicore.organization.request.EmployeeUpdate;
import com.flexicore.request.TenantToUserCreate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class EmployeeService implements IEmployeeService {

	@PluginInfo(version = 1)
	@Autowired
	private EmployeeRepository repository;
	@Autowired
	private Logger logger;

	@Autowired
	private UserService userService;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	@Override
	public PaginationResponse<Employee> listAllEmployees(
			SecurityContext securityContext, EmployeeFiltering filtering) {

		List<Employee> endpoints = repository.listAllEmployees(securityContext,
				filtering);
		long count = repository.countAllEmployees(securityContext, filtering);
		return new PaginationResponse<>(endpoints, filtering, count);
	}

	public Employee createEmployee(EmployeeCreate creationContainer,
			SecurityContext securityContext) {

		Employee employee = createEmployeeNoMerge(creationContainer,
				securityContext);
		TenantToUser tenantToUser = userService.createTenantToUserNoMerge(
				new TenantToUserCreate()
						.setUser(employee)
						.setDefaultTenant(true)
						.setTenant(creationContainer.getTenant()),
				securityContext);
		repository.massMerge(Arrays.asList(employee, tenantToUser));
		return employee;
	}

	@Override
	public Employee createEmployeeNoMerge(EmployeeCreate creationContainer,
			SecurityContext securityContext) {
		Employee employee = new Employee(
				creationContainer.getName(), securityContext);
		updateEmployeeNoMerge(employee, creationContainer);
		return employee;

	}

	public Employee updateEmployee(EmployeeUpdate creationContainer,
			SecurityContext securityContext) {
		Employee employee = creationContainer.getEmployee();
		if (updateEmployeeNoMerge(employee, creationContainer)) {
			repository.merge(employee);
		}
		return employee;
	}

	@Override
	public boolean updateEmployeeNoMerge(Employee employee,
			EmployeeCreate employeeCreate) {
		boolean update = userService
				.updateUserNoMerge(employee, employeeCreate);
		return update;
	}

	public void validateFiltering(EmployeeFiltering filtering,
			SecurityContext securityContext) {
	}

}