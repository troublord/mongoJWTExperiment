package com.example.demo.filter;

import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;




public class AuthorizationCheckFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
         //如果不是登入就攔截
         if(!request.getServletPath().equals("/api/user/login")){
            //get 不到header搞什麼毛      --- header 沒有AUTHORIZATION need to check why0955
            String authorHeader =  request.getHeader(AUTHORIZATION);
            
            String bearer ="Bearer ";
            //以jjwt驗證token，只要驗證成功就放行
            //驗證失敗會拋exception，直接將錯誤訊息傳回
            if(authorHeader!= null && authorHeader.startsWith(bearer)){
                try{
                String token = authorHeader.substring(bearer.length());
               
                byte[] keyBtypes = Decoders.BASE64.decode("MySecretisthatIwanttohavelargerKeytoSuccedThispartofcode");
                Key key = Keys.hmacShaKeyFor(keyBtypes);
                
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                System.out.println("bellow jwt parserBuilder");

                filterChain.doFilter(request, response);
                
                }catch(Exception e){
                    System.err.println("Error : "+e);
                    response.setStatus(FORBIDDEN.value());
                    
                    Map<String, String> err = new HashMap<>();
                    err.put("jwt_err", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), err);
                }
            }else{
                System.out.println(authorHeader + " header is head");
                response.setStatus(UNAUTHORIZED.value());
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }

        
}
