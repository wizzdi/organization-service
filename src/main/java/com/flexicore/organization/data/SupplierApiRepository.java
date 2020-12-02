package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.SupplierApi;
import com.flexicore.organization.request.SupplierApiFiltering;
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
public class SupplierApiRepository extends AbstractRepositoryPlugin {

	public List<SupplierApi> getAllSupplierApis(
			SecurityContext securityContext, SupplierApiFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SupplierApi> q = cb.createQuery(SupplierApi.class);
		Root<SupplierApi> r = q.from(SupplierApi.class);
		List<Predicate> preds = new ArrayList<>();
		addSupplierApiPredicate(filtering, cb, r, preds);
		QueryInformationHolder<SupplierApi> queryInformationHolder = new QueryInformationHolder<>(
				filtering, SupplierApi.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public Long countAllSupplierApis(SecurityContext securityContext,
			SupplierApiFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SupplierApi> r = q.from(SupplierApi.class);
		List<Predicate> preds = new ArrayList<>();
		addSupplierApiPredicate(filtering, cb, r, preds);
		QueryInformationHolder<SupplierApi> queryInformationHolder = new QueryInformationHolder<>(
				filtering, SupplierApi.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addSupplierApiPredicate(SupplierApiFiltering filtering,
			CriteriaBuilder cb, Root<SupplierApi> r, List<Predicate> preds) {

	}

}