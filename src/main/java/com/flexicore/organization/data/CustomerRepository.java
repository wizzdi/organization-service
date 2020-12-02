package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.Customer_;
import com.flexicore.organization.request.CustomerFiltering;
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
public class CustomerRepository extends AbstractRepositoryPlugin {

	public List<Customer> getAllCustomers(SecurityContext securityContext,
			CustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> q = cb.createQuery(Customer.class);
		Root<Customer> r = q.from(Customer.class);
		List<Predicate> preds = new ArrayList<>();
		addCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<Customer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Customer.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllCustomers(SecurityContext securityContext,
			CustomerFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Customer> r = q.from(Customer.class);
		List<Predicate> preds = new ArrayList<>();
		addCustomerPredicates(filtering, cb, r, preds);
		QueryInformationHolder<Customer> queryInformationHolder = new QueryInformationHolder<>(
				filtering, Customer.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public  static <T extends Customer> void addCustomerPredicates(CustomerFiltering filtering,
			CriteriaBuilder cb, Root<T> r, List<Predicate> preds) {
		if(filtering.getExternalIds()!=null && !filtering.getExternalIds().isEmpty()){
			preds.add(r.get(Customer_.externalId).in(filtering.getExternalIds()));
		}

	}

}