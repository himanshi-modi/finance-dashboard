package com.finance.dashboard.security;

import com.finance.dashboard.entity.enums.Role;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static CustomUserDetails getCurrentUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return (CustomUserDetails) principal;
    }

    public static Role getCurrentRole() {
        return getCurrentUser().getUser().getRole();
    }

    public static void requireRole(Role requiredRole) {

        Role currentRole = getCurrentRole();

        if (currentRole != requiredRole) {
            throw new com.finance.dashboard.exception.RoleAccessDeniedException(
                    requiredRole + " role required to perform this action",
                    requiredRole
            );
        }
    }
}
