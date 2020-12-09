package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.*;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.Customer_;
import com.flexicore.organization.request.CustomerFiltering;
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
		if(filtering.getUsers()!=null&&!filtering.getUsers().isEmpty()){
			Set<String> ids=filtering.getUsers().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Tenant> tenantJoin=r.join(Customer_.tenant);
			Join<Tenant, TenantToUser> tenantToUserJoin=tenantJoin.join(Tenant_.tenantToUser);
			Join<TenantToUser, User> join=cb.treat(tenantToUserJoin.join(Baselink_.rightside),User.class);
			preds.add(join.get(User_.id).in(ids));
		}

	}

}