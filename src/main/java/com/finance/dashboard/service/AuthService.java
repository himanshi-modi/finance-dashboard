package com.finance.dashboard.service;

import com.finance.dashboard.dto.LoginRequestDto;
import com.finance.dashboard.dto.LoginResponseDto;
import com.finance.dashboard.dto.SignupRequestDto;
import com.finance.dashboard.dto.SignupResponseDto;
import com.finance.dashboard.entity.enums.Role;
import com.finance.dashboard.entity.enums.Status;
import com.finance.dashboard.entity.model.User;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.security.AuthUtil;
import com.finance.dashboard.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        User user= userRepository.findByEmail(signupRequestDto.getEmail()).orElse(null);
        if(user!=null){
            throw new RuntimeException("User already exists! ");
        }
        user =userRepository.save(User.builder()
                .email(signupRequestDto.getEmail())
                .role(Role.VIEWER)
                .status(Status.ACTIVE)
                .passwordHash(passwordEncoder.encode(signupRequestDto.getPassword()))
                .accountLockedUntil(null)
                .failedLoginAttempts(0)

                .build()
        );
        return modelMapper.map(user,SignupResponseDto.class);




    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        String email=loginRequestDto.getEmail();
        User user=userRepository.findByEmail(email).orElseThrow(()->new BadCredentialsException("Invalid email or password"));
        if(user.getAccountLockedUntil()!=null && user.getAccountLockedUntil().isAfter(LocalDateTime.now())){
            throw new RuntimeException("Account is Locked.Try later!");
        }

        try{
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword()));
            user.setFailedLoginAttempts(0);
            user.setAccountLockedUntil(null);
            CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
            if(user.getStatus()!=Status.ACTIVE){
                throw new RuntimeException("User account is "+user.getStatus());
            }
            String accessToken=authUtil.generateAccessToken(user);
            String refreshToken=authUtil.generateRefreshToken(user);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
            return new LoginResponseDto(user.getId(),accessToken,refreshToken);

        }catch (Exception ex){
            int attempts=user.getFailedLoginAttempts()==null ? 0:user.getFailedLoginAttempts();
            attempts++;
            user.setFailedLoginAttempts(attempts);
            if (attempts>=5){
                user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(5));
            }
            throw new BadCredentialsException("Invalid email or password");
        }
    }
    public LoginResponseDto refreshToken(String refreshToken){
        String email=authUtil.getUsernameFromToken(refreshToken);

        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found !"));
        if(user.getStatus() != Status.ACTIVE){
            throw new RuntimeException("User account is " + user.getStatus());
        }
        System.out.println("DB Token: "+user.getRefreshToken());
        System.out.println("Incoming Token: "+refreshToken);
        if(user.getRefreshToken()==null || !user.getRefreshToken().equals(refreshToken)){
            throw new RuntimeException("Invalid Refresh Token");
        }
        if(authUtil.isTokenExpired(refreshToken)){
            throw new RuntimeException("Refresh token expired. Please login again");
        }
        String newAccessToken =authUtil.generateAccessToken(user);
        String newRefreshToken=authUtil.generateRefreshToken(user);

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        return new LoginResponseDto(user.getId(),newAccessToken,newRefreshToken);
    }
}
