package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepo;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
    CommandLineRunner runner(UserRepo userDao){
        return args -> {
            String email ="test456@456.ccc";
            //建立一個user
            User user = new User(email,"test1","test1",0123456,1);
            //查這個email在db有沒有重複
            userDao.findUserByEmail(email)
                .ifPresentOrElse(users ->{
                    //有的話就印出來
                    System.out.println("already exists");
                    System.out.println(users);
        
                }, ()->{
                    //沒有的話就新增
                    userDao.insert(user);   
                    System.out.println("add user");
                });
        };
    }

}
