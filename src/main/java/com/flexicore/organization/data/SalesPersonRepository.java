package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.Baselink_;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.interfaces.IEmployeeRepository;
import com.flexicore.organization.model.SalesPerson;
import com.flexicore.organization.model.SalesPersonToRegion;
import com.flexicore.organization.model.SalesPerson_;
import com.flexicore.organization.model.SalesRegion;
import com.flexicore.organization.request.SalesPersonFiltering;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class SalesPersonRepository extends AbstractRepositoryPlugin {

	public List<SalesPerson> listAllSalesPersons(
			SecurityContext securityContext, SalesPersonFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SalesPerson> q = cb.createQuery(SalesPerson.class);
		Root<SalesPerson> r = q.from(SalesPerson.class);
		List<Predicate> preds = new ArrayList<>();
		addSalesPersonPredicates(filtering, cb, r, preds);
		QueryInformationHolder<SalesPerson> queryInformationHolder = new QueryInformationHolder<>(
				filtering, SalesPerson.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addSalesPersonPredicates(SalesPersonFiltering filtering,
			CriteriaBuilder cb, Root<SalesPerson> r, List<Predicate> preds) {
		IEmployeeRepository.addEmployeePredicates(filtering, cb, r, preds);
		if (filtering.getSalesRegions() != null
				&& !filtering.getSalesRegions().isEmpty()) {
			Set<String> ids = filtering.getSalesRegions().parallelStream()
					.map(f -> f.getId()).collect(Collectors.toSet());
			Join<SalesPerson, SalesPersonToRegion> j1 = r
					.join(SalesPerson_.salesPersonToRegions);
			Join<SalesPersonToRegion, SalesRegion> join = cb.treat(
					j1.join(Baselink_.rightside), SalesRegion.class);
			preds.add(join.get(SalesPerson_.id).in(ids));
		}
	}

	public Long countAllSalesPersons(SecurityContext securityContext,
			SalesPersonFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SalesPerson> r = q.from(SalesPerson.class);
		List<Predicate> preds = new ArrayList<>();
		addSalesPersonPredicates(filtering, cb, r, preds);
		QueryInformationHolder<SalesPerson> queryInformationHolder = new QueryInformationHolder<>(
				filtering, SalesPerson.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

}