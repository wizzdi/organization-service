package com.flexicore.organization.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.organization.model.Employee;
import com.flexicore.organization.model.Employee_;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.model.Organization_;
import com.flexicore.organization.request.EmployeeFiltering;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Extension
@Component
public class EmployeeRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;

	public List<Employee> listAllEmployees(SecurityContextBase securityContext,
										   EmployeeFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
		Root<Employee> r = q.from(Employee.class);
		List<Predicate> preds = new ArrayList<>();
		addEmployeePredicates(filtering, cb,q, r, preds,securityContext);
		q.select(r).where(preds.toArray(Predicate[]::new));
		TypedQuery<Employee> query = em.createQuery(q);
		BasicRepository.addPagination(filtering, query);
		return query.getResultList();
	}

	public <T extends Employee> void addEmployeePredicates(EmployeeFiltering filtering, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> preds, SecurityContextBase securityContext) {

		securedBasicRepository.addSecuredBasicPredicates(filtering.getBasicPropertiesFilter(),cb,q,r,preds,securityContext);
		if(filtering.getOrganizations()!=null&&!filtering.getOrganizations().isEmpty()){
			Set<String> ids=filtering.getOrganizations().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Organization> join=r.join(Employee_.organization);
			preds.add(join.get(Organization_.id).in(ids));
		}
		if (filtering.getExternalIds()!=null && !filtering.getExternalIds().isEmpty()) {
			preds.add(r.get(Employee_.externalId).in(filtering.getExternalIds()));
		}
		if (filtering.getExternalId2s()!=null && !filtering.getExternalId2s().isEmpty()) {
			preds.add(r.get(Employee_.externalId2).in(filtering.getExternalId2s()));
		}
		if (filtering.getOrganizationAdmin()!=null) {
			preds.add(cb.equal(cb.literal(filtering.getOrganizationAdmin()),r.get(Employee_.organizationAdmin)));
		}
	}

	public Long countAllEmployees(SecurityContextBase securityContext,
								  EmployeeFiltering filtering) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Employee> r = q.from(Employee.class);
		List<Predicate> preds = new ArrayList<>();
		addEmployeePredicates(filtering, cb,q, r, preds,securityContext);
		q.select(cb.count(r)).where(preds.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();
	}



	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securedBasicRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securedBasicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securedBasicRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		securedBasicRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		securedBasicRepository.massMerge(toMerge);
	}
}
