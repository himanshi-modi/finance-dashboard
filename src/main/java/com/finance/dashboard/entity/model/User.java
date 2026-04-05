package com.finance.dashboard.entity.model;


import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;


import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Email is required! ")
    @Email(message = "Invalid Email Format")
    private String email;

    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "Password is required! ")
    @Size(min = 8, message = "Password must be at last 8 characters! ")
    private String passwordHash;

    @Indexed
    @NotBlank(message = "Role is required! ")
    private Role role;

    @Indexed
    @NotNull
    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    private LocalDateTime lastLoginAt;

    private String refreshToken;

    private Integer tokenVersion;

    private LocalDateTime accountLockedUntil;

    private Integer failedLoginAttempts;




}
