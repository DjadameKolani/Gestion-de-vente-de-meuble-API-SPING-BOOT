package com.example.projet_pro.filter;

import com.example.projet_pro.configuration.JwtUtils;
import com.example.projet_pro.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import  jakarta.servlet.ServleException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.Servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils JwtUtils;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
final String authHeader = request.getHeader("Authorization");
 String username = null;
 String jwt = null;
 if (authHeader !=null && authHeader.startsWith("Bearer")){
     jwt = authHeader.substring(7);
     username = jwtUtils.extractUsername(jwt);
 }

 if(username !=null && SercurityContextHolder.getContext().getAuthentication() == null){
   UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

     if(jwtUtils.validateToken(jwt, userDetails)){
         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null.userDetails:getAuthorities());
         authenticationToken.setDetails(new webAutenticationDetailsSource().buildDetails(request));
         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
     }
 }

 filterChain.doFilter(request, response);


    }
}
