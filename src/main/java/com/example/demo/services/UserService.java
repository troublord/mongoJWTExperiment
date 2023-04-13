package com.example.demo.services;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.stereotype.Service;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }
    public Optional<User> getOneUser(String email){
        return userRepo.findUserByEmail(email);
    }
    public void addUser(User user){
        userRepo.insert(user);
    }


    public String verifyUser(String email,String userAcct , String userPasswd){
        Optional<User> user=userRepo.findUserByEmail(email);
        String result="9999";
        if(user.isPresent()){
            if(user.get().getUserAccount().equals(userAcct)){
                if(user.get().getUserPassword().equals(userPasswd)){
                    byte[] keyBtypes = Decoders.BASE64.decode("MySecretisthatIwanttohavelargerKeytoSuccedThispartofcode");
                    Key key = Keys.hmacShaKeyFor(keyBtypes);
                    Date expireDate = 
                    //設定30min過期
                    new Date(System.currentTimeMillis()+ 30 * 60 * 1000);
                    String jwtToken = Jwts.builder()
                    .setSubject(email) //我以email當subject
                    .setExpiration(expireDate)
                    .signWith(key)
                    .compact();
                    //直接將JWT傳回
                    result=jwtToken;
                }else{
                    result="wrong password";
                }
            }else{
                result="wrong username";
            }
        }else{
            result="wrong email";
        }
        return result;        
    }
}
