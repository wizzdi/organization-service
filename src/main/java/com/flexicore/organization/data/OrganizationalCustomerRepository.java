package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.model.Tenant;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.model.Organization_;
import com.flexicore.organization.model.OrganizationalCustomer;
import com.flexicore.organization.model.OrganizationalCustomer_;
import com.flexicore.organization.request.OrganizationalCustomerFiltering;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	public static <T extends OrganizationalCustomer> void addOrganizationalCustomerPredicates(OrganizationalCustomerFiltering filtering,
			CriteriaBuilder cb, Root<T> r, List<Predicate> preds) {
		CustomerRepository.addCustomerPredicates(filtering,cb,r,preds);
		if(filtering.getOrganizations()!=null &&!filtering.getOrganizations().isEmpty()){
			Set<String> ids=filtering.getOrganizations().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Organization> join= r.join(OrganizationalCustomer_.organization);
			preds.add(join.get(Organization_.id).in(ids));
		}


	}

}