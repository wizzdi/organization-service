package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.organization.interfaces.ISiteRepository;
import com.flexicore.organization.model.*;
import com.flexicore.organization.request.CustomerDocumentFiltering;
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
public class CustomerDocumentRepository extends AbstractRepositoryPlugin {

	public List<CustomerDocument> getAllCustomerDocuments(SecurityContext securityContext,
			CustomerDocumentFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CustomerDocument> q = cb.createQuery(CustomerDocument.class);
		Root<CustomerDocument> r = q.from(CustomerDocument.class);
		List<Predicate> preds = new ArrayList<>();
		addCustomerDocumentPredicates(filtering, cb, r, preds);
		QueryInformationHolder<CustomerDocument> queryInformationHolder = new QueryInformationHolder<>(
				filtering, CustomerDocument.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public long countAllCustomerDocuments(SecurityContext securityContext,
			CustomerDocumentFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<CustomerDocument> r = q.from(CustomerDocument.class);
		List<Predicate> preds = new ArrayList<>();
		addCustomerDocumentPredicates(filtering, cb, r, preds);
		QueryInformationHolder<CustomerDocument> queryInformationHolder = new QueryInformationHolder<>(
				filtering, CustomerDocument.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addCustomerDocumentPredicates(CustomerDocumentFiltering filtering, CriteriaBuilder cb, Root<CustomerDocument> r, List<Predicate> preds) {
		if (filtering.getCustomers() != null && !filtering.getCustomers().isEmpty()) {
			Set<String> ids = filtering.getCustomers().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
			Join<CustomerDocument, Customer> join = r.join(CustomerDocument_.customer);
			preds.add(join.get(Customer_.id).in(ids));
		}
	}

}