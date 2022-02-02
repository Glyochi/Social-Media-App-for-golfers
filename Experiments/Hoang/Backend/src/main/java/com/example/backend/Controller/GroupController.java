package com.example.backend.Controller;

import com.example.backend.Model.GroupModel;
import com.example.backend.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    GroupRepository groupRepository;

    @GetMapping("group/all")
    List<GroupModel> GetAllGroups(){
        return groupRepository.findAll();
    }

}
