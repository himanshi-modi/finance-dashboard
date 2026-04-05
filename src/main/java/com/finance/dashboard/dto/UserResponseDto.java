package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private String id;
    private String email;
    private Role role;
    private Status status;
}
