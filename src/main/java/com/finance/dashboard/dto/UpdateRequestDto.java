package com.finance.dashboard.dto;

import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import lombok.Data;

@Data
public class UpdateRequestDto {
    private Role role;
    private Status status;

}
