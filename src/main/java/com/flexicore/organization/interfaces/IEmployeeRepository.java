package com.flexicore.organization.interfaces;

import com.flexicore.model.*;
import com.flexicore.organization.model.Employee;
import com.flexicore.organization.request.EmployeeFiltering;
import com.flexicore.request.UserFiltering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface IEmployeeRepository {

	static <T extends Employee> void addEmployeePredicates(
			EmployeeFiltering filtering, CriteriaBuilder cb, Root<T> r,
			List<Predicate> preds) {
		addUserFiltering(filtering, cb, r, preds);
	}

	public static <T extends User> void addUserFiltering(
			UserFiltering userFiltering, CriteriaBuilder cb, Root<T> r,
			List<Predicate> preds) {

		if (userFiltering.getEmails() != null
				&& !userFiltering.getEmails().isEmpty()) {
			preds.add(r.get(User_.email).in(userFiltering.getEmails()));
		}
		if (userFiltering.getPhoneNumbers() != null
				&& !userFiltering.getPhoneNumbers().isEmpty()) {
			preds.add(r.get(User_.phoneNumber).in(
					userFiltering.getPhoneNumbers()));
		}

		if (userFiltering.getEmailVerificationToken() != null) {
			preds.add(cb.equal(r.get(User_.emailVerificationToken),
					userFiltering.getEmailVerificationToken()));
		}

		if (userFiltering.getForgotPasswordToken() != null) {
			preds.add(cb.equal(r.get(User_.forgotPasswordToken),
					userFiltering.getForgotPasswordToken()));
		}

		if (userFiltering.getLastNameLike() != null) {
			preds.add(cb.like(r.get(User_.surName),
					userFiltering.getLastNameLike()));
		}

		if (userFiltering.getUserIds() != null
				&& !userFiltering.getUserIds().isEmpty()) {
			preds.add(r.get(User_.id).in(userFiltering.getUserIds()));
		}
		if (userFiltering.getUserTenants() != null
				&& !userFiltering.getUserTenants().isEmpty()) {
			Set<String> ids = userFiltering.getUserTenants().parallelStream()
					.map(Baseclass::getId).collect(Collectors.toSet());
			Join<T, TenantToUser> join = r.join(User_.tenantToUsers);
			Join<TenantToUser, Tenant> tenantJoin = cb.treat(
					join.join(Baselink_.leftside), Tenant.class);
			preds.add(tenantJoin.get(Tenant_.id).in(ids));
		}

	}
}
