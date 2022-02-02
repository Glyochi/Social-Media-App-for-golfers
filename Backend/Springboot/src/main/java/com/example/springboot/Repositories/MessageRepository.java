package com.example.springboot.Repositories;

import com.example.springboot.Models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface MessageRepository extends JpaRepository<MessageModel, Long>{
}
