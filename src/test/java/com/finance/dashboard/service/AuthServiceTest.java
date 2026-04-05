package com.finance.dashboard.service;

import com.finance.dashboard.dto.LoginRequestDto;
import com.finance.dashboard.dto.LoginResponseDto;
import com.finance.dashboard.dto.SignupRequestDto;
import com.finance.dashboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void tstSignupSuccess(){
        SignupRequestDto requestDto=new SignupRequestDto();
        requestDto.setEmail("test1@gmail.com");
        requestDto.setPassword("password123");
        var response=authService.signup(requestDto);
        assertNotNull(response);
        assertEquals("test1@gmail.com",response.getEmail());
    }

    @Test
    void testSignupDuplicateUser(){
        SignupRequestDto request = new SignupRequestDto();
        request.setEmail("test2@gmail.com");
        request.setPassword("password123");

        authService.signup(request);
        Exception ex = assertThrows(RuntimeException.class,()->{

                authService.signup(request);
    });
        assertTrue(ex.getMessage().contains("User already exists"));
    }

    @Test
    void testLoginSuccess(){
        SignupRequestDto signup=new SignupRequestDto();
        signup.setEmail("login@gmail.com");
        signup.setPassword("password123");

        authService.signup(signup);

        LoginRequestDto login=new LoginRequestDto();
        login.setEmail("login@gmail.com");
        login.setPassword("password123");

        LoginResponseDto response=authService.login(login);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());

    }

    @Test
    void testLoginFailure(){
        SignupRequestDto signup= new SignupRequestDto();
        signup.setEmail("fail@gmail.com");
        signup.setPassword("password123");
        authService.signup(signup);

        LoginRequestDto login=new LoginRequestDto();
        login.setEmail("fail@gmail.com");
        login.setPassword("wrongpass");

        assertThrows(Exception.class,()->{
            authService.login(login);
        });

    }
    @Test
    void testRefreshToken(){
        SignupRequestDto signup=new SignupRequestDto();
        signup.setEmail("refresh@gmail.com");
        signup.setPassword("password123");
        authService.signup(signup);

        LoginRequestDto login= new LoginRequestDto();
        login.setEmail("refresh@gmail.com");
        login.setPassword("password123");

        LoginResponseDto loginResponse=authService.login(login);

        LoginResponseDto refreshResponse=authService.refreshToken(loginResponse.getRefreshToken());
        assertNotNull(refreshResponse.getAccessToken());
        assertNotNull(refreshResponse.getRefreshToken());

    }


}