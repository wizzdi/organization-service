package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.organization.model.Organization;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerFiltering extends FilteringInformationHolder {

    private Set<String> externalIds;


    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends CustomerFiltering> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }
}
