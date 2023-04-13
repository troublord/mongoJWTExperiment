package com.example.demo.controllers;

import java.security.Key;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody Map<String,String> map){
        String email= map.get("email");
        String userAccount= map.get("userAccount");
        String userPassword= map.get("userPassword");
        String result="null data";
        if(StringUtils.isBlank(email)||StringUtils.isBlank(userAccount)||StringUtils.isBlank(userPassword)){
            System.out.println("god damn");
            return ResponseEntity.ok(result);
        }else{
            System.out.println("you good");
            result=userService.verifyUser(email, userAccount, userPassword);
            
            return ResponseEntity.ok(result);  
        }
    }  
    
    @GetMapping("/login/data")
    public ResponseEntity<String> userData(){
        Optional<User> userOption =  userService.getOneUser("test456@456.ccc");
        if(userOption.isPresent()){
            return ResponseEntity.ok(userOption.get().toString()); 
        }
        return  ResponseEntity.ok("eat westNorth_wind");
        
    } 
}
