package com.flexicore.organization.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.organization.model.Customer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerDocumentFiltering extends FilteringInformationHolder {

    private Set<String> customerIds=new HashSet<>();
    @JsonIgnore
    private List<Customer> customers;


    public Set<String> getCustomerIds() {
        return customerIds;
    }

    public <T extends CustomerDocumentFiltering> T setCustomerIds(Set<String> customerIds) {
        this.customerIds = customerIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Customer> getCustomers() {
        return customers;
    }

    public <T extends CustomerDocumentFiltering> T setCustomers(List<Customer> customers) {
        this.customers = customers;
        return (T) this;
    }
}
