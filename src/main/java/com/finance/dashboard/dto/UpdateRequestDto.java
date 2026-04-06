package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRequestDto {
    @NotNull
    private Role role;

    @NotNull
    private Status status;

}
