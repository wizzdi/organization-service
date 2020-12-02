package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.*;
import com.flexicore.organization.request.IndustryFiltering;
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
public class IndustryRepository extends AbstractRepositoryPlugin {

	public List<Industry> getAllIndustries(SecurityContext securityContext,
			IndustryFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Industry> q = cb.createQuery(Industry.class);
		Root<Industry> r = q.from(Industry.class);
		List<Predicate> preds = new ArrayList<>();
		addIndustryPredicates(filtering, cb, r, preds);
		QueryInformationHolder<Industry> queryInformationHolder = new QueryInformationHolder<>(filtering, Industry.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllIndustries(SecurityContext securityContext,
			IndustryFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Industry> r = q.from(Industry.class);
		List<Predicate> preds = new ArrayList<>();
		addIndustryPredicates(filtering, cb, r, preds);
		QueryInformationHolder<Industry> queryInformationHolder = new QueryInformationHolder<>(filtering, Industry.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addIndustryPredicates(IndustryFiltering filtering, CriteriaBuilder cb, Root<Industry> r, List<Predicate> preds) {
		if (filtering.getCustomers() != null && !filtering.getCustomers().isEmpty()) {
			Set<String> ids = filtering.getCustomers().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
			Join<Industry, IndustryToCustomer> industryIndustryToCustomerJoin = r.join(Industry_.industryToCustomers);
			Join<IndustryToCustomer, Customer> join = industryIndustryToCustomerJoin.join(IndustryToCustomer_.customer);

			preds.add(join.get(Customer_.id).in(ids));
		}
	}

}