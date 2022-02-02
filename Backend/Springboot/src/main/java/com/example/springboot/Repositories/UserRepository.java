package com.example.springboot.Repositories;

import com.example.springboot.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    public UserModel findUserModelByUserName(String userName);
}
