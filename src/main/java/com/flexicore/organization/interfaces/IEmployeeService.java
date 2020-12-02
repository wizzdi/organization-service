package com.flexicore.organization.interfaces;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.territories.Address;
import com.flexicore.model.territories.Address_;
import com.flexicore.organization.model.Employee;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.model.Site;
import com.flexicore.organization.model.Site_;
import com.flexicore.organization.request.*;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface IEmployeeService extends ServicePlugin {
	PaginationResponse<Employee> listAllEmployees(
			SecurityContext securityContext, EmployeeFiltering filtering);

	Employee createEmployeeNoMerge(EmployeeCreate creationContainer,
			SecurityContext securityContext);

	boolean updateEmployeeNoMerge(Employee employee,
			EmployeeCreate employeeCreate);

}
