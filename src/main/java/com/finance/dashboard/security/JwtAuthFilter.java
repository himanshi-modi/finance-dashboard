package com.finance.dashboard.security;

import com.finance.dashboard.entity.enums.Status;
import com.finance.dashboard.entity.model.User;
import com.finance.dashboard.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header=request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        String token=header.substring(7);
        String email=authUtil.getUsernameFromToken(token);

        if(email!=null  && SecurityContextHolder.getContext().getAuthentication()==null){
            User user= userRepository.findByEmail(email).orElseThrow();
            if(user.getStatus()!= Status.ACTIVE){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                return;
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(user,null, List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().name())));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }

        filterChain.doFilter(request,response);
    }
}
