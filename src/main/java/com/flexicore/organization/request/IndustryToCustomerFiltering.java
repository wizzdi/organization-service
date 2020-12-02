package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.organization.model.Customer;
import com.flexicore.organization.model.Industry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IndustryToCustomerFiltering extends FilteringInformationHolder {

    private Set<String> industriesIds=new HashSet<>();
    @JsonIgnore
    private List<Industry> industries;

    private Set<String> customerIds=new HashSet<>();
    @JsonIgnore
    private List<Customer> customers;

    public Set<String> getIndustriesIds() {
        return industriesIds;
    }

    public <T extends IndustryToCustomerFiltering> T setIndustriesIds(Set<String> industriesIds) {
        this.industriesIds = industriesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Industry> getIndustries() {
        return industries;
    }

    public <T extends IndustryToCustomerFiltering> T setIndustries(List<Industry> industries) {
        this.industries = industries;
        return (T) this;
    }

    public Set<String> getCustomerIds() {
        return customerIds;
    }

    public <T extends IndustryToCustomerFiltering> T setCustomerIds(Set<String> customerIds) {
        this.customerIds = customerIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Customer> getCustomers() {
        return customers;
    }

    public <T extends IndustryToCustomerFiltering> T setCustomers(List<Customer> customers) {
        this.customers = customers;
        return (T) this;
    }
}
