package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.organization.data.CustomerDocumentRepository;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.CustomerDocument;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.request.CustomerDocumentCreate;
import com.flexicore.organization.request.CustomerDocumentFiltering;
import com.flexicore.organization.request.CustomerDocumentUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class CustomerDocumentService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private CustomerDocumentRepository repository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public void validateFiltering(CustomerDocumentFiltering filtering,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(filtering, securityContext);
		Set<String> customerIds = filtering.getCustomerIds();
		Map<String, Customer> customers = customerIds.isEmpty() ? new HashMap<>() : listByIds(Customer.class, customerIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		customerIds.removeAll(customers.keySet());
		if (!customerIds.isEmpty()) { throw new BadRequestException("No Organization with ids " + customerIds);
		}
		filtering.setCustomers(new ArrayList<>(customers.values()));
	}

	public PaginationResponse<CustomerDocument> getAllCustomerDocuments(SecurityContext securityContext, CustomerDocumentFiltering filtering) {
		List<CustomerDocument> list = repository.getAllCustomerDocuments(securityContext, filtering);
		long count = repository.countAllCustomerDocuments(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public CustomerDocument createCustomerDocument(CustomerDocumentCreate creationContainer,
			SecurityContext securityContext) {
		CustomerDocument customerDocument = createCustomerDocumentNoMerge(creationContainer, securityContext);
		repository.merge(customerDocument);
		return customerDocument;
	}

	private CustomerDocument createCustomerDocumentNoMerge(CustomerDocumentCreate creationContainer,
			SecurityContext securityContext) {
		CustomerDocument customerDocument = new CustomerDocument(creationContainer.getName(),securityContext);
		updateCustomerDocumentNoMerge(customerDocument, creationContainer);
		return customerDocument;
	}

	private boolean updateCustomerDocumentNoMerge(CustomerDocument customerDocument,
			CustomerDocumentCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer, customerDocument);

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
			SecurityContext securityContext) {
		CustomerDocument customerDocument = updateContainer.getCustomerDocument();
		if (updateCustomerDocumentNoMerge(customerDocument, updateContainer)) {
			repository.merge(customerDocument);
		}
		return customerDocument;
	}

	public void validate(CustomerDocumentCreate creationContainer,
			SecurityContext securityContext) {
		baseclassNewService.validate(creationContainer, securityContext);
		String customerId = creationContainer.getCustomerId();
		Customer customer = customerId == null ? null : getByIdOrNull(customerId, Customer.class, null, securityContext);
		if (customer == null && customerId != null) {
			throw new BadRequestException("No Customer with id " + customerId);
		}
		creationContainer.setCustomer(customer);

		String fileResourceId = creationContainer.getFileResourceId();
		FileResource fileResource = fileResourceId == null ? null : getByIdOrNull(fileResourceId, FileResource.class, null, securityContext);
		if (fileResource == null && fileResourceId != null) {
			throw new BadRequestException("No FileResource with id " + fileResourceId);
		}
		creationContainer.setFileResource(fileResource);
	}
}