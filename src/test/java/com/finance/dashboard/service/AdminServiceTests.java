package com.finance.dashboard.service;

import com.finance.dashboard.dto.UpdateRequestDto;
import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import com.finance.dashboard.entity.model.User;
import com.finance.dashboard.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;

import com.finance.dashboard.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
public class AdminServiceTests {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;

    private User admin;
    private User user;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        // create admin
        admin = userRepository.save(User.builder()
                .email("admin@test.com")
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .passwordHash("pass")
                .build());


        user = userRepository.save(User.builder()
                .email("user@test.com")
                .role(Role.VIEWER)
                .status(Status.ACTIVE)
                .passwordHash("pass")
                .build());


        CustomUserDetails userDetails = new CustomUserDetails(admin);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testGetUsers() {

        var page = adminService.getUsers(0,10,null,null);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void testUpdateUserRole() {

        UpdateRequestDto dto = new UpdateRequestDto();
        dto.setRole(Role.ANALYST);

        var response = adminService.updateUser(user.getId(), dto);

        assertEquals(Role.ANALYST, response.getRole());
    }

    @Test
    void testUpdateUserStatus() {

        UpdateRequestDto dto = new UpdateRequestDto();
        dto.setStatus(Status.SUSPENDED);

        var response = adminService.updateUser(user.getId(), dto);

        assertEquals(Status.SUSPENDED, response.getStatus());
    }

    @Test
    void testDeleteUser() {

        adminService.deleteUser(user.getId());

        var deletedUser = userRepository.findById(user.getId()).orElseThrow();

        assertEquals(Status.DELETED, deletedUser.getStatus());
    }

    @Test
    void testDeleteSelf_shouldFail() {

        Exception ex = assertThrows(Exception.class, () -> {
            adminService.deleteUser(admin.getId());
        });

        assertTrue(ex.getMessage().contains("cannot delete yourself"));
    }
}
