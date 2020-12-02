package com.flexicore.organization.interfaces;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.organization.model.Site;
import com.flexicore.organization.request.SiteCreate;
import com.flexicore.organization.request.SiteFiltering;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface ISiteService extends ServicePlugin {

	void validateFiltering(SiteFiltering filtering,
			SecurityContext securityContext);

	PaginationResponse<Site> getAllSites(SecurityContext securityContext,
			SiteFiltering filtering);

	List<Site> listAllSites(SecurityContext securityContext,
			SiteFiltering filtering);

	Site createSiteNoMerge(SiteCreate creationContainer,
			SecurityContext securityContext);

	boolean updateSiteNoMerge(Site site, SiteCreate creationContainer);

	void validate(SiteCreate creationContainer, SecurityContext securityContext);

	void massMerge(List<?> toMerge);
}
