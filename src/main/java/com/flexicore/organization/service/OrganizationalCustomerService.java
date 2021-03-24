package com.flexicore.organization.service;


import com.flexicore.model.Basic;
import com.flexicore.organization.request.BranchFiltering;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.OrganizationalCustomerRepository;
import com.flexicore.organization.model.OrganizationalCustomer;
import com.flexicore.organization.request.OrganizationalCustomerCreate;
import com.flexicore.organization.request.OrganizationalCustomerFiltering;
import com.flexicore.organization.request.OrganizationalCustomerUpdate;
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

public class OrganizationalCustomerService implements Plugin {


	@Autowired
	private OrganizationalCustomerRepository repository;


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

	public void validateFiltering(OrganizationalCustomerFiltering filtering,
			SecurityContextBase securityContextBase) {
		customerService.validateFiltering(filtering, securityContextBase);

	}

	public PaginationResponse<OrganizationalCustomer> getAllOrganizationalCustomers(
			SecurityContextBase securityContextBase, OrganizationalCustomerFiltering filtering) {
		List<OrganizationalCustomer> list = listAllOrganizationalCustomers(securityContextBase, filtering);
		long count = repository.countAllOrganizationalCustomers(securityContextBase, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public List<OrganizationalCustomer> listAllOrganizationalCustomers(SecurityContextBase securityContextBase, OrganizationalCustomerFiltering filtering) {
		return repository.getAllOrganizationalCustomers(securityContextBase, filtering);
	}

	public OrganizationalCustomer createOrganizationalCustomer(OrganizationalCustomerCreate creationContainer,
			SecurityContextBase securityContextBase) {
		OrganizationalCustomer organizationalCustomer = createOrganizationalCustomerNoMerge(creationContainer, securityContextBase);
		repository.merge(organizationalCustomer);
		return organizationalCustomer;
	}

	public OrganizationalCustomer createOrganizationalCustomerNoMerge(OrganizationalCustomerCreate creationContainer,
			SecurityContextBase securityContextBase) {
		OrganizationalCustomer organizationalCustomer = new OrganizationalCustomer();
		updateOrganizationalCustomerNoMerge(organizationalCustomer, creationContainer);
		BaseclassService.createSecurityObjectNoMerge(organizationalCustomer, securityContextBase);
		return organizationalCustomer;
	}

	public boolean updateOrganizationalCustomerNoMerge(OrganizationalCustomer organizationalCustomer,
			OrganizationalCustomerCreate creationContainer) {
		boolean update = customerService.updateCustomerNoMerge(organizationalCustomer,
				creationContainer);

		if(creationContainer.getOrganization()!=null && (organizationalCustomer.getOrganization()==null||!creationContainer.getOrganization().getId().equals(organizationalCustomer.getOrganization().getId()))){
			organizationalCustomer.setOrganization(creationContainer.getOrganization());
			update=true;
		}


		return update;
	}

	public OrganizationalCustomer updateOrganizationalCustomer(OrganizationalCustomerUpdate updateContainer,
			SecurityContextBase securityContextBase) {
		OrganizationalCustomer organizationalCustomer = updateContainer.getOrganizationalCustomer();
		if (updateOrganizationalCustomerNoMerge(organizationalCustomer, updateContainer)) {
			repository.merge(organizationalCustomer);
		}
		return organizationalCustomer;
	}

	public void validate(OrganizationalCustomerCreate creationContainer,
			SecurityContextBase securityContextBase) {
		customerService.validate(creationContainer, securityContextBase);

	}
}