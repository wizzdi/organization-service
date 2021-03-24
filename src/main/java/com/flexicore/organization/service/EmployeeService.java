package com.flexicore.organization.service;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.organization.data.EmployeeRepository;
import com.flexicore.organization.model.Employee;
import com.flexicore.organization.request.EmployeeCreate;
import com.flexicore.organization.request.EmployeeFiltering;
import com.flexicore.organization.request.EmployeeUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;


@Extension
@Component

public class EmployeeService implements Plugin {


	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private BasicService basicService;


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return repository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return repository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return repository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return repository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return repository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		repository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		repository.massMerge(toMerge);
	}

	
	public PaginationResponse<Employee> listAllEmployees(
			SecurityContextBase securityContextBase, EmployeeFiltering filtering) {

		List<Employee> endpoints = repository.listAllEmployees(securityContextBase,
				filtering);
		long count = repository.countAllEmployees(securityContextBase, filtering);
		return new PaginationResponse<>(endpoints, filtering, count);
	}

	public Employee createEmployee(EmployeeCreate creationContainer,
			SecurityContextBase securityContextBase) {

		Employee employee = createEmployeeNoMerge(creationContainer,
				securityContextBase);

		repository.merge(employee);
		return employee;
	}

	
	public Employee createEmployeeNoMerge(EmployeeCreate creationContainer,
			SecurityContextBase securityContextBase) {
		Employee employee = new Employee();
		updateEmployeeNoMerge(employee, creationContainer);
		Baseclass securityObjectNoMerge = BaseclassService.createSecurityObjectNoMerge(employee, securityContextBase);
		return employee;

	}

	public Employee updateEmployee(EmployeeUpdate creationContainer,
			SecurityContextBase securityContextBase) {
		Employee employee = creationContainer.getEmployee();
		if (updateEmployeeNoMerge(employee, creationContainer)) {
			repository.merge(employee);
		}
		return employee;
	}

	
	public boolean updateEmployeeNoMerge(Employee employee,
			EmployeeCreate employeeCreate) {
		boolean update = basicService.updateBasicNoMerge(employeeCreate, employee);
		return update;
	}

	public void validateFiltering(EmployeeFiltering filtering,
			SecurityContextBase securityContextBase) {
	}

}