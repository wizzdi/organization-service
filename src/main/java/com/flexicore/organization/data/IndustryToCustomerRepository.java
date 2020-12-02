package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.IndustryToCustomer;
import com.flexicore.organization.model.IndustryToCustomer_;
import com.flexicore.organization.model.Customer_;
import com.flexicore.organization.request.IndustryToCustomerFiltering;
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
public class IndustryToCustomerRepository extends AbstractRepositoryPlugin {

	public List<IndustryToCustomer> getAllIndustryToCustomers(SecurityContext securityContext,
			IndustryToCustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<IndustryToCustomer> q = cb.createQuery(IndustryToCustomer.class);
		Root<IndustryToCustomer> r = q.from(IndustryToCustomer.class);
		List<Predicate> preds = new ArrayList<>();
		addIndustryToCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<IndustryToCustomer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, IndustryToCustomer.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllIndustryToCustomers(SecurityContext securityContext,
			IndustryToCustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<IndustryToCustomer> r = q.from(IndustryToCustomer.class);
		List<Predicate> preds = new ArrayList<>();
		addIndustryToCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<IndustryToCustomer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, IndustryToCustomer.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addIndustryToCustomerPredicates(IndustryToCustomerFiltering filtering, CriteriaBuilder cb, Root<IndustryToCustomer> r, List<Predicate> preds) {
		if (filtering.getCustomers() != null && !filtering.getCustomers().isEmpty()) {
			Set<String> ids = filtering.getCustomers().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
			Join<IndustryToCustomer, Customer> join = r.join(IndustryToCustomer_.customer);
			preds.add(join.get(Customer_.id).in(ids));
		}
	}

}