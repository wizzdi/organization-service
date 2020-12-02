package com.flexicore.organization.interfaces;

import com.flexicore.model.territories.Address;
import com.flexicore.model.territories.Address_;
import com.flexicore.organization.model.Site;
import com.flexicore.organization.model.Site_;
import com.flexicore.organization.request.SiteFiltering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ISiteRepository {

	static <T extends Site> void addSitePredicates(SiteFiltering filtering,
			CriteriaBuilder cb, Root<T> r, List<Predicate> preds) {
		if (filtering.getAddresses() != null
				&& !filtering.getAddresses().isEmpty()) {
			Set<String> ids = filtering.getAddresses().parallelStream()
					.map(f -> f.getId()).collect(Collectors.toSet());
			Join<T, Address> join = r.join(Site_.address);
			preds.add(join.get(Address_.id).in(ids));
		}

		if (filtering.getExternalIds() != null
				&& !filtering.getExternalIds().isEmpty()) {
			preds.add(r.get(Site_.externalId).in(filtering.getExternalIds()));
		}

	}

}
