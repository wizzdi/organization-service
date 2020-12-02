package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.interfaces.IEmployeeRepository;
import com.flexicore.organization.interfaces.IOrganizationRepository;
import com.flexicore.organization.model.Employee;
import com.flexicore.organization.model.Site;
import com.flexicore.organization.request.EmployeeFiltering;
import com.flexicore.organization.request.SiteFiltering;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class EmployeeRepository extends AbstractRepositoryPlugin
		implements
			IEmployeeRepository {

	public List<Employee> listAllEmployees(SecurityContext securityContext,
			EmployeeFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
		Root<Employee> r = q.from(Employee.class);
		List<Predicate> preds = new ArrayList<>();
		IEmployeeRepository.addEmployeePredicates(filtering, cb, r, preds);
		QueryInformationHolder<Employee> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Employee.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public Long countAllEmployees(SecurityContext securityContext,
			EmployeeFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Employee> r = q.from(Employee.class);
		List<Predicate> preds = new ArrayList<>();
		IEmployeeRepository.addEmployeePredicates(filtering, cb, r, preds);
		QueryInformationHolder<Employee> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Employee.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

}