package com.example.springboot.Repositories;

import com.example.springboot.Models.PostModel;
import com.example.springboot.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostModel, Long> {
}
