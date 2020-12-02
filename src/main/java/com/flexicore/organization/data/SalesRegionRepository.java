package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.SalesRegion;
import com.flexicore.organization.request.SalesRegionFiltering;
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
public class SalesRegionRepository extends AbstractRepositoryPlugin {

	public List<SalesRegion> listAllSalesRegions(
			SecurityContext securityContext, SalesRegionFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SalesRegion> q = cb.createQuery(SalesRegion.class);
		Root<SalesRegion> r = q.from(SalesRegion.class);
		List<Predicate> preds = new ArrayList<>();
		addSalesRegionPredicates(filtering, cb, r, preds);
		QueryInformationHolder<SalesRegion> queryInformationHolder = new QueryInformationHolder<>(
				filtering, SalesRegion.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addSalesRegionPredicates(SalesRegionFiltering filtering,
			CriteriaBuilder cb, Root<SalesRegion> r, List<Predicate> preds) {

	}

	public Long countAllSalesRegions(SecurityContext securityContext,
			SalesRegionFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SalesRegion> r = q.from(SalesRegion.class);
		List<Predicate> preds = new ArrayList<>();
		addSalesRegionPredicates(filtering, cb, r, preds);
		QueryInformationHolder<SalesRegion> queryInformationHolder = new QueryInformationHolder<>(
				filtering, SalesRegion.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

}