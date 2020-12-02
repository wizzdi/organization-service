package com.flexicore.organization.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.organization.interfaces.IOrganizationRepository;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class OrganizationRepository extends AbstractRepositoryPlugin
		implements
			IOrganizationRepository {

}