package com.flexicore.organization.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Baseclass;
import com.flexicore.model.territories.Address;
import com.flexicore.organization.data.SiteRepository;
import com.flexicore.organization.interfaces.ISiteService;
import com.flexicore.organization.model.Site;
import com.flexicore.organization.request.SiteCreate;
import com.flexicore.organization.request.SiteFiltering;
import com.flexicore.organization.request.SiteUpdate;
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
public class SiteService implements ISiteService {

	@PluginInfo(version = 1)
	@Autowired
	private SiteRepository repository;

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batch, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batch, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	@Override
	public void validateFiltering(SiteFiltering filtering,
			SecurityContext securityContext) {
		Set<String> addressIds = filtering.getAddressIds();
		Map<String, Address> address = addressIds.isEmpty()
				? new HashMap<>()
				: listByIds(Address.class, addressIds, securityContext)
						.parallelStream().collect(
								Collectors.toMap(f -> f.getId(), f -> f));
		addressIds.removeAll(address.keySet());
		if (!addressIds.isEmpty()) {
			throw new BadRequestException("No Address with ids " + addressIds);
		}
		filtering.setAddresses(new ArrayList<>(address.values()));
	}

	@Override
	public PaginationResponse<Site> getAllSites(
			SecurityContext securityContext, SiteFiltering filtering) {
		List<Site> list = listAllSites(securityContext, filtering);
		long count = repository.countAllSites(securityContext, filtering);
		return new PaginationResponse<>(list, filtering, count);
	}

	@Override
	public List<Site> listAllSites(SecurityContext securityContext,
			SiteFiltering filtering) {
		return repository.getAllSites(securityContext, filtering);
	}

	public Site createSite(SiteCreate creationContainer,
			SecurityContext securityContext) {
		Site site = createSiteNoMerge(creationContainer, securityContext);
		repository.merge(site);
		return site;
	}

	@Override
	public Site createSiteNoMerge(SiteCreate creationContainer,
			SecurityContext securityContext) {
		Site site = new Site(creationContainer.getName(),securityContext);
		updateSiteNoMerge(site, creationContainer);
		return site;
	}

	@Override
	public boolean updateSiteNoMerge(Site site, SiteCreate creationContainer) {
		boolean update = false;
		if (creationContainer.getName() != null
				&& !creationContainer.getName().equals(site.getName())) {
			site.setName(creationContainer.getName());
			update = true;
		}

		if (creationContainer.getDescription() != null
				&& !creationContainer.getDescription().equals(
						site.getDescription())) {
			site.setDescription(creationContainer.getDescription());
			update = true;
		}

		if (creationContainer.getAddress() != null
				&& (site.getAddress() == null || !creationContainer
						.getAddress().getId().equals(site.getAddress().getId()))) {
			site.setAddress(creationContainer.getAddress());
			update = true;
		}

		if (creationContainer.getExternalId() != null
				&& !creationContainer.getExternalId().equals(
						site.getExternalId())) {
			site.setExternalId(creationContainer.getExternalId());
			update = true;
		}
		return update;
	}

	public Site updateSite(SiteUpdate updateContainer,
			SecurityContext securityContext) {
		Site site = updateContainer.getSite();
		if (updateSiteNoMerge(site, updateContainer)) {
			repository.merge(site);
		}
		return site;
	}

	@Override
	public void validate(SiteCreate creationContainer,
			SecurityContext securityContext) {
		String addressId = creationContainer.getAddressId();
		Address address = addressId == null ? null : getByIdOrNull(addressId,
				Address.class, null, securityContext);
		if (address == null && addressId != null) {
			throw new BadRequestException("No Address with id " + addressId);
		}
		creationContainer.setAddress(address);
	}

	@Override
	public void massMerge(List<?> toMerge) {
		repository.massMerge(toMerge);
	}
}