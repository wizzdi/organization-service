package com.flexicore.organization.service;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.organization.data.CustomerDocumentRepository;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.CustomerDocument;
import com.flexicore.organization.request.CustomerDocumentCreate;
import com.flexicore.organization.request.CustomerDocumentFiltering;
import com.flexicore.organization.request.CustomerDocumentUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.metamodel.SingularAttribute;
import java.util.*;
import java.util.stream.Collectors;


@Extension
@Component

public class CustomerDocumentService implements Plugin {


	@Autowired
	private CustomerDocumentRepository repository;

	@Autowired
	private BasicService basicService;


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

	public void validateFiltering(CustomerDocumentFiltering filtering,
			SecurityContextBase securityContextBase) {
		basicService.validate(filtering, securityContextBase);
		Set<String> customerIds = filtering.getCustomerIds();
		Map<String, Customer> customers = customerIds.isEmpty() ? new HashMap<>() : listByIds(Customer.class, customerIds, SecuredBasic_.security, securityContextBase).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		customerIds.removeAll(customers.keySet());
		if (!customerIds.isEmpty()) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Organization with ids " + customerIds);
		}
		filtering.setCustomers(new ArrayList<>(customers.values()));
	}

	public PaginationResponse<CustomerDocument> getAllCustomerDocuments(SecurityContextBase securityContextBase, CustomerDocumentFiltering filtering) {
		List<CustomerDocument> list = repository.getAllCustomerDocuments(securityContextBase, filtering);
		long count = repository.countAllCustomerDocuments(securityContextBase, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public CustomerDocument createCustomerDocument(CustomerDocumentCreate creationContainer,
			SecurityContextBase securityContextBase) {
		CustomerDocument customerDocument = createCustomerDocumentNoMerge(creationContainer, securityContextBase);
		repository.merge(customerDocument);
		return customerDocument;
	}

	private CustomerDocument createCustomerDocumentNoMerge(CustomerDocumentCreate creationContainer,
			SecurityContextBase securityContextBase) {
		CustomerDocument customerDocument = new CustomerDocument();
		Baseclass securityObjectNoMerge = BaseclassService.createSecurityObjectNoMerge(customerDocument, securityContextBase);
		updateCustomerDocumentNoMerge(customerDocument, creationContainer);
		return customerDocument;
	}

	private boolean updateCustomerDocumentNoMerge(CustomerDocument customerDocument,
			CustomerDocumentCreate creationContainer) {
		boolean update = basicService.updateBasicNoMerge(creationContainer, customerDocument);

		if (creationContainer.getCustomer() != null && (customerDocument.getCustomer() == null || !creationContainer.getCustomer().getId().equals(customerDocument.getCustomer().getId()))) {
			customerDocument.setCustomer(creationContainer.getCustomer());
			update = true;
		}

		if (creationContainer.getFileResource() != null && (customerDocument.getFileResource() == null || !creationContainer.getFileResource().getId().equals(customerDocument.getFileResource().getId()))) {
			customerDocument.setFileResource(creationContainer.getFileResource());
			update = true;
		}
		return update;
	}

	public CustomerDocument updateCustomerDocument(CustomerDocumentUpdate updateContainer,
			SecurityContextBase securityContextBase) {
		CustomerDocument customerDocument = updateContainer.getCustomerDocument();
		if (updateCustomerDocumentNoMerge(customerDocument, updateContainer)) {
			repository.merge(customerDocument);
		}
		return customerDocument;
	}

	public void validate(CustomerDocumentCreate creationContainer,
			SecurityContextBase securityContextBase) {
		basicService.validate(creationContainer, securityContextBase);
		String customerId = creationContainer.getCustomerId();
		Customer customer = customerId == null ? null : getByIdOrNull(customerId, Customer.class, null, securityContextBase);
		if (customer == null && customerId != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Customer with id " + customerId);
		}
		creationContainer.setCustomer(customer);

		String fileResourceId = creationContainer.getFileResourceId();
		FileResource fileResource = fileResourceId == null ? null : getByIdOrNull(fileResourceId, FileResource.class, null, securityContextBase);
		if (fileResource == null && fileResourceId != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No FileResource with id " + fileResourceId);
		}
		creationContainer.setFileResource(fileResource);
	}
}