package com.example.backend.Controller;

import com.example.backend.Model.UserModel;
import com.example.backend.Repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    @GetMapping("group/all")
    List<UserModel> GetAllUser(){
        return userRepository.findAll();
    }

    //BY PATH: Create just a group with name and nothing more
    @PostMapping("user/post/{name}/{score}")
    UserModel PostGroupByPath(@PathVariable String name, @PathVariable int score){
        UserModel newUser = new UserModel();
        newUser.setName(name);
        newUser.setScore(score);
        userRepository.save(newUser);
        return newUser;
    }

    //BY BODY: Create just a group with name and nothing more
    @PostMapping("group/post")
    UserModel PostGroupByBody(@RequestBody UserModel newUser){
        userRepository.save(newUser);
        return newUser;
    }


    @DeleteMapping("group/delete/{id}")
    void DeleteGroupByPath(@PathVariable long id){
        userRepository.deleteById(id);
    }


}
