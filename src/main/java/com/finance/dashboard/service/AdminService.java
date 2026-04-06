package com.finance.dashboard.service;

import com.finance.dashboard.dto.UpdateRequestDto;
import com.finance.dashboard.dto.UpdateResponseDto;
import com.finance.dashboard.dto.UserResponseDto;
import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import com.finance.dashboard.entity.model.User;
import com.finance.dashboard.exception.InvalidOperationException;
import com.finance.dashboard.exception.ResourceNotFoundExcption;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.security.CustomUserDetails;
import com.finance.dashboard.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UpdateResponseDto updateUser(String userId, UpdateRequestDto updateRequestDto){
        User user=userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundExcption("User not Found with id: "+userId));

        if(updateRequestDto.getRole()!=null){
            user.setRole(updateRequestDto.getRole());
        }

        if (updateRequestDto.getStatus()!=null){
            user.setStatus(updateRequestDto.getStatus());
        }
        userRepository.save(user);

        return UpdateResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    public void deleteUser(String userId){
        SecurityUtils.requireRole(Role.ADMIN);
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundExcption("User not found with id: "+userId));
        Object principal=   SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserId;
        if(principal instanceof  CustomUserDetails customUserDetails){
            currentUserId=customUserDetails.getUser().getId();
        }else {
            throw new InvalidOperationException("Invalid authentication principal type: "+principal.getClass());
        }
        if(user.getId().equals(currentUserId)){
            throw new InvalidOperationException("You cannot delete yourself");
        }
        if(user.getStatus()==Status.DELETED){
            throw new InvalidOperationException("User already deleted");
        }
        user.setStatus(Status.DELETED);

        userRepository.save(user);

    }

    public Page<UserResponseDto> getUsers(int page, int size, String search, Status status) {
        Pageable pageable= PageRequest.of(page,size);

        Page<User> users=userRepository.findByStatusNot(Status.DELETED,pageable);
        if(search!=null && status!=null){
            users=userRepository.findByEmailContainingIgnoreCaseAndStatus(search,status,pageable);
        }else if(search!=null) {
            users = userRepository.findByEmailContainingIgnoreCase(search, pageable);
        }else if (status!=null){
            users=userRepository.findByStatus(status,pageable);
        }else {
            users=userRepository.findAll(pageable);
        }
        return  users.map(user -> UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build()
        );

    }


}
