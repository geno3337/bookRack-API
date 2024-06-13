package com.example.bookrack.security;

import com.example.bookrack.entity.User;
import com.example.bookrack.entity.UserSession;
import com.example.bookrack.response.common.ApiResponse;
import com.example.bookrack.service.user.UserService;
import com.example.bookrack.service.user.UserSessionService;
import com.example.bookrack.utilities.JwtUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {


    private final ObjectMapper objectMapper;

    private final UserSessionService userSessionService;

    private final UserService userService;

    private final JwtUtility jwtUtil;

    public JwtFilter(ObjectMapper objectMapper, UserService userService, UserSessionService userSessionService, JwtUtility jwtUtil) {
        this.objectMapper = objectMapper;
        this.userSessionService = userSessionService;
        this.userService = userService;
        this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);
            if (token == null){
                throw new RuntimeException(" token is null");
            }
            if (request.getServletPath().startsWith("/user")){
                doUserAuthentication(token,request,response,filterChain);
            }else throw new  RuntimeException(request.getServletPath());

        }catch (io.jsonwebtoken.ExpiredJwtException expiredException) {
            try {
                sendError(response, "Invalid - Expired Token", HttpStatus.UNAUTHORIZED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (io.jsonwebtoken.SignatureException signatureException) {
            try {
                sendError(response, "Invalid - Broken Token", HttpStatus.UNAUTHORIZED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (AccessDeniedException accessDeniedException) {
            try {
                sendError(response, accessDeniedException.getMessage(), HttpStatus.UNAUTHORIZED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                sendError(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void doUserAuthentication(String token,HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessTokenIdentifier = jwtUtil.getJWTSubject(token);
        if (jwtUtil.isTokenExpired(token)){
            throw new RuntimeException("token expired");
        }
        UserSession userSession = userSessionService.getActiveSessionByAccessTokenIdentifier(accessTokenIdentifier);
        if(userSession == null || userSession.getAccessTokenExpiresAt().before(new Date())){
            throw new RuntimeException("invalid session id or expired session id");
        }
        User userInfo = userService.getActiveUserById(userSession.getUserId());
        if (userInfo == null){
            throw new RuntimeException("account Inactive");
        }
        letProceedFurther(userInfo,userSession,filterChain,request,response);
    }

    private void letProceedFurther(User userInfo, UserSession userSession, FilterChain filterChain,
                                   HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("user"));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userInfo,userSession,authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request,response);
    }

    public String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(jwtUtil.getTokenName());

        if (token != null && StringUtils.hasText(token) && token.startsWith(jwtUtil.getTokenPrefix())){
            return token.substring(7,token.length());
        }
        return null;
    }

    private void sendError(HttpServletResponse response, String message, HttpStatus webstate) throws Exception {
        ApiResponse opRes = new ApiResponse("failed", message, 401);
        response.setStatus(webstate.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(opRes));
    }
}
