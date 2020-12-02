package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.organization.data.BranchRepository;
import com.flexicore.organization.model.Branch;
import com.flexicore.organization.model.Organization;
import com.flexicore.organization.request.BranchCreate;
import com.flexicore.organization.request.BranchFiltering;
import com.flexicore.organization.request.BranchUpdate;
import com.flexicore.security.SecurityContext;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
@Primary
public class BranchService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private BranchRepository repository;

	@PluginInfo(version = 1)
	@Autowired
	private SiteService siteService;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public void validateFiltering(BranchFiltering filtering,
			SecurityContext securityContext) {
		siteService.validateFiltering(filtering, securityContext);
		Set<String> organizationIds = filtering.getOrganizationIds();
		Map<String, Organization> organizations = organizationIds.isEmpty() ? new HashMap<>() : listByIds(Organization.class, organizationIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		organizationIds.removeAll(organizations.keySet());
		if (!organizationIds.isEmpty()) {
			throw new BadRequestException("No Organization with ids " + organizationIds);
		}
		filtering.setOrganizations(new ArrayList<>(organizations.values()));
	}

	public PaginationResponse<Branch> getAllBranches(
			SecurityContext securityContext, BranchFiltering filtering) {
		List<Branch> list = repository.getAllBranches(securityContext,
				filtering);
		long count = repository.countAllBranches(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	public Branch createBranch(BranchCreate creationContainer,
			SecurityContext securityContext) {
		Branch branch = createBranchNoMerge(creationContainer, securityContext);
		repository.merge(branch);
		return branch;
	}

	private Branch createBranchNoMerge(BranchCreate creationContainer,
			SecurityContext securityContext) {
		Branch branch = new Branch(creationContainer.getName(),securityContext);
		updateBranchNoMerge(branch, creationContainer);
		return branch;
	}

	private boolean updateBranchNoMerge(Branch branch,
			BranchCreate creationContainer) {
		boolean update = siteService.updateSiteNoMerge(branch,
				creationContainer);

		if (creationContainer.getOrganization() != null && (branch.getOrganization() == null || !creationContainer.getOrganization().getId().equals(branch.getOrganization().getId()))) {
			branch.setOrganization(creationContainer.getOrganization());
			update = true;
		}
		return update;
	}

	public Branch updateBranch(BranchUpdate updateContainer,
			SecurityContext securityContext) {
		Branch branch = updateContainer.getBranch();
		if (updateBranchNoMerge(branch, updateContainer)) {
			repository.merge(branch);
		}
		return branch;
	}

	public void validate(BranchCreate creationContainer,
			SecurityContext securityContext) {
		siteService.validate(creationContainer, securityContext);
		String organizationId = creationContainer.getOrganizationId();
		Organization organization = organizationId == null ? null : getByIdOrNull(organizationId, Organization.class, null, securityContext);
		if (organization == null && organizationId != null) {
			throw new BadRequestException("No Organization with id " + organizationId);
		}
		creationContainer.setOrganization(organization);
	}
}