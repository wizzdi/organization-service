package com.flexicore.organization.service;


import com.flexicore.model.Basic;
import com.flexicore.organization.request.BranchFiltering;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.IndividualCustomerRepository;
import com.flexicore.organization.model.IndividualCustomer;
import com.flexicore.organization.request.IndividualCustomerCreate;
import com.flexicore.organization.request.IndividualCustomerFiltering;
import com.flexicore.organization.request.IndividualCustomerUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.service.BaseclassService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;


@Extension
@Component

public class IndividualCustomerService implements Plugin {


	@Autowired
	private IndividualCustomerRepository repository;


	@Autowired
	private CustomerService customerService;



	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return repository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return repository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return repository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return repository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return repository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		repository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		repository.massMerge(toMerge);
	}

	public void validateFiltering(IndividualCustomerFiltering filtering,
			SecurityContextBase securityContextBase) {
		customerService.validateFiltering(filtering, securityContextBase);

	}

	public PaginationResponse<IndividualCustomer> getAllIndividualCustomers(
			SecurityContextBase securityContextBase, IndividualCustomerFiltering filtering) {
		List<IndividualCustomer> list = repository.getAllIndividualCustomers(securityContextBase, filtering);
		long count = repository.countAllIndividualCustomers(securityContextBase, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public IndividualCustomer createIndividualCustomer(IndividualCustomerCreate creationContainer,
			SecurityContextBase securityContextBase) {
		IndividualCustomer individualCustomer = createIndividualCustomerNoMerge(creationContainer, securityContextBase);
		repository.merge(individualCustomer);
		return individualCustomer;
	}

	public IndividualCustomer createIndividualCustomerNoMerge(IndividualCustomerCreate creationContainer,
			SecurityContextBase securityContextBase) {
		IndividualCustomer individualCustomer = new IndividualCustomer();
		updateIndividualCustomerNoMerge(individualCustomer, creationContainer);
		BaseclassService.createSecurityObjectNoMerge(individualCustomer, securityContextBase);
		return individualCustomer;
	}

	public boolean updateIndividualCustomerNoMerge(IndividualCustomer individualCustomer,
			IndividualCustomerCreate creationContainer) {
		boolean update = customerService.updateCustomerNoMerge(individualCustomer,
				creationContainer);


		return update;
	}

	public IndividualCustomer updateIndividualCustomer(IndividualCustomerUpdate updateContainer,
			SecurityContextBase securityContextBase) {
		IndividualCustomer individualCustomer = updateContainer.getIndividualCustomer();
		if (updateIndividualCustomerNoMerge(individualCustomer, updateContainer)) {
			repository.merge(individualCustomer);
		}
		return individualCustomer;
	}

	public void validate(IndividualCustomerCreate creationContainer,
			SecurityContextBase securityContextBase) {
		customerService.validate(creationContainer, securityContextBase);

	}
}