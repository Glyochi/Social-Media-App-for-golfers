package com.example.backend.Controller;

import com.example.backend.Model.GroupModel;
import com.example.backend.Repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {

    @Autowired
    GroupRepository groupRepository;


    @GetMapping("group/all")
    List<GroupModel> GetAllGroup(){
        return groupRepository.findAll();
    }

    //BY PATH: Create just a group with name and nothing more
    @PostMapping("group/post/{name}")
    GroupModel PostGroupByPath(@PathVariable String name){
        GroupModel newGroup = new GroupModel();
        newGroup.setName(name);
        groupRepository.save(newGroup);
        return newGroup;
    }

    //BY BODY: Create just a group with name and nothing more
    @PostMapping("group/post")
    GroupModel PostGroupByBody(@RequestBody GroupModel newGroup){
        groupRepository.save(newGroup);
        return newGroup;
    }



}
