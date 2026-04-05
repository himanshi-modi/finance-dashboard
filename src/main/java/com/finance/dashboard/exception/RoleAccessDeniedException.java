package com.finance.dashboard.exception;

import com.finance.dashboard.entity.enums.Role;
import org.modelmapper.internal.bytebuddy.asm.Advice;

public class RoleAccessDeniedException extends RuntimeException{
    private final Role requiredRole;


    public RoleAccessDeniedException(String message,Role requiredRole) {
        super(message);
        this.requiredRole = requiredRole;
    }

    public Role getRequiredRole(){
        return requiredRole;
    }
}
