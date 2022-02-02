package com.example.springboot.Controllers;

import com.example.springboot.Models.MessageModel;
import com.example.springboot.Repositories.MessageRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class MessageController {
    @Autowired
    MessageRepository messageRepository;

//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------
//GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST
//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------

    @GetMapping("message/getAllMessage")
    @ApiOperation(value = "Get all messages by all users in a certain group",
            notes = "This function will return all messages from the messageRepository",
            response = ArrayList.class)
    List<MessageModel> GetAllMessage(){
        return messageRepository.findAll();
    }


}
