package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.OrganizationalCustomer;
import com.flexicore.organization.request.OrganizationalCustomerFiltering;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
@Extension
@Component
public class OrganizationalCustomerRepository extends AbstractRepositoryPlugin {

	public List<OrganizationalCustomer> getAllOrganizationalCustomers(SecurityContext securityContext,
			OrganizationalCustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<OrganizationalCustomer> q = cb.createQuery(OrganizationalCustomer.class);
		Root<OrganizationalCustomer> r = q.from(OrganizationalCustomer.class);
		List<Predicate> preds = new ArrayList<>();
		addOrganizationalCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<OrganizationalCustomer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, OrganizationalCustomer.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllOrganizationalCustomers(SecurityContext securityContext,
			OrganizationalCustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<OrganizationalCustomer> r = q.from(OrganizationalCustomer.class);
		List<Predicate> preds = new ArrayList<>();
		addOrganizationalCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<OrganizationalCustomer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, OrganizationalCustomer.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addOrganizationalCustomerPredicates(OrganizationalCustomerFiltering filtering,
			CriteriaBuilder cb, Root<OrganizationalCustomer> r, List<Predicate> preds) {
		CustomerRepository.addCustomerPredicates(filtering,cb,r,preds);

	}

}