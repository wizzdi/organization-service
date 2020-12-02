package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.interfaces.IOrganizationRepository;
import com.flexicore.organization.interfaces.ISiteRepository;
import com.flexicore.organization.model.Site;
import com.flexicore.organization.request.SiteFiltering;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.flexicore.organization.interfaces.ISiteRepository.addSitePredicates;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class SiteRepository extends AbstractRepositoryPlugin
		implements
			ISiteRepository {

	public List<Site> getAllSites(SecurityContext securityContext,
			SiteFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Site> q = cb.createQuery(Site.class);
		Root<Site> r = q.from(Site.class);
		List<Predicate> preds = new ArrayList<>();
		addSitePredicates(filtering, cb, r, preds);
		QueryInformationHolder<Site> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Site.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllSites(SecurityContext securityContext,
			SiteFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Site> r = q.from(Site.class);
		List<Predicate> preds = new ArrayList<>();
		addSitePredicates(filtering, cb, r, preds);
		QueryInformationHolder<Site> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Site.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

}