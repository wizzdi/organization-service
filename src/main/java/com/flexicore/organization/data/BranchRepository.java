package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.interfaces.ISiteRepository;
import com.flexicore.organization.model.Branch;
import com.flexicore.organization.model.Branch_;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.model.Organization_;
import com.flexicore.organization.request.BranchFiltering;
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
public class BranchRepository extends AbstractRepositoryPlugin {

	public List<Branch> getAllBranches(SecurityContext securityContext,
			BranchFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Branch> q = cb.createQuery(Branch.class);
		Root<Branch> r = q.from(Branch.class);
		List<Predicate> preds = new ArrayList<>();
		addBranchPredicates(filtering, cb, r, preds);
		QueryInformationHolder<Branch> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Branch.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllBranches(SecurityContext securityContext,
			BranchFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Branch> r = q.from(Branch.class);
		List<Predicate> preds = new ArrayList<>();
		addBranchPredicates(filtering, cb, r, preds);
		QueryInformationHolder<Branch> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Branch.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addBranchPredicates(BranchFiltering filtering,
			CriteriaBuilder cb, Root<Branch> r, List<Predicate> preds) {
		ISiteRepository.addSitePredicates(filtering, cb, r, preds);
		if (filtering.getOrganizations() != null && !filtering.getOrganizations().isEmpty()) {
			Set<String> ids = filtering.getOrganizations().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
			Join<Branch, Organization> join = r.join(Branch_.organization);
			preds.add(join.get(Organization_.id).in(ids));
		}
	}

}