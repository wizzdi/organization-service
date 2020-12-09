package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.IndividualCustomer;
import com.flexicore.organization.request.IndividualCustomerFiltering;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
@Extension
@Component
public class IndividualCustomerRepository extends AbstractRepositoryPlugin {

	public List<IndividualCustomer> getAllIndividualCustomers(SecurityContext securityContext,
			IndividualCustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<IndividualCustomer> q = cb.createQuery(IndividualCustomer.class);
		Root<IndividualCustomer> r = q.from(IndividualCustomer.class);
		List<Predicate> preds = new ArrayList<>();
		addIndividualCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<IndividualCustomer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, IndividualCustomer.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllIndividualCustomers(SecurityContext securityContext,
			IndividualCustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<IndividualCustomer> r = q.from(IndividualCustomer.class);
		List<Predicate> preds = new ArrayList<>();
		addIndividualCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<IndividualCustomer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, IndividualCustomer.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public static <T extends IndividualCustomer>  void addIndividualCustomerPredicates(IndividualCustomerFiltering filtering,
			CriteriaBuilder cb, Root<T> r, List<Predicate> preds) {
		CustomerRepository.addCustomerPredicates(filtering,cb,r,preds);

	}

}