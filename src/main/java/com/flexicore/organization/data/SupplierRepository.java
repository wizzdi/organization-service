package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.Baselink_;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.Supplier;
import com.flexicore.organization.model.Supplier_;
import com.flexicore.organization.request.SupplierFiltering;
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
public class SupplierRepository extends AbstractRepositoryPlugin {

	public List<Supplier> getAllSuppliers(SecurityContext securityContext,
			SupplierFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Supplier> q = cb.createQuery(Supplier.class);
		Root<Supplier> r = q.from(Supplier.class);
		List<Predicate> preds = new ArrayList<>();
		addSupplierPredicate(filtering, cb, r, preds);
		QueryInformationHolder<Supplier> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Supplier.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}


	public Long countAllSuppliers(SecurityContext securityContext,
			SupplierFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Supplier> r = q.from(Supplier.class);
		List<Predicate> preds = new ArrayList<>();
		addSupplierPredicate(filtering, cb, r, preds);
		QueryInformationHolder<Supplier> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Supplier.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addSupplierPredicate(SupplierFiltering filtering,
			CriteriaBuilder cb, Root<Supplier> r, List<Predicate> preds) {


	}



}