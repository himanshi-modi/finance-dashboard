package com.finance.dashboard.controller;

import com.finance.dashboard.dto.UpdateRequestDto;
import com.finance.dashboard.dto.UpdateResponseDto;
import com.finance.dashboard.dto.UserResponseDto;
import com.finance.dashboard.entity.enums.Status;
import com.finance.dashboard.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseDto>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10")int size,
                                                          @RequestParam(required = false) String search,
                                                          @RequestParam(required = false) Status status){

        return ResponseEntity.ok(adminService.getUsers(page, size, search, status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<UpdateResponseDto> updateUser(@PathVariable String id , @RequestBody UpdateRequestDto updateRequestDto){
        return ResponseEntity.ok(adminService.updateUser(id,updateRequestDto));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }



}

