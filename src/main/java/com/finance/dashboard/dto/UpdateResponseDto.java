package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateResponseDto {

    private String id;
    private String email;
    private Role role;
    private Status status;
    private LocalDateTime updatedAt;
}
