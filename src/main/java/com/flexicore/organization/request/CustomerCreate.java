package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.organization.model.Organization;
import com.flexicore.request.BaseclassCreate;

public class CustomerCreate extends BaseclassCreate {

    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends CustomerCreate> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }
}
