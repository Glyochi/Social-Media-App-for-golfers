package com.example.springboot.Controllers.User_Group;

import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.PostModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Repositories.GroupRepository;
import com.example.springboot.Repositories.UserRepository;
import com.example.springboot.Services.GroupAuthorityService;
import com.example.springboot.Services.GroupMemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

@RestController
public class GroupMemberController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    GroupMemberService groupMemberService;


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("groupMember/getAdminInGroup/{callerID}/")
    @ApiOperation(value = "Get all the groups the user has admin role in",
            notes = "Provided the callerID, this function will return an array containing all the GroupModels that the user has admin role in",
            response = ArrayList.class)
    ArrayList<GroupModel> getAdminInGroup_ByPath(@ApiParam(
            name = "callerID",
            type = "Integer",
            value = "callerID for the user that called this function",
            example = "2",
            required = true) @PathVariable long callerID) {
        Set<GroupModel> groups = userRepository.findById(callerID).get().getAdminInGroup();
        ArrayList<GroupModel> returnedArrayList = new ArrayList<>();
        Iterator<GroupModel> ite = groups.iterator();
        while (ite.hasNext()) {
            returnedArrayList.add(ite.next());
        }
        return returnedArrayList;
    }

    @GetMapping("groupMember/getModInGroup/callerID/{callerID}/")
    @ApiOperation(value = "Get all the groups the user has mod role in",
            notes = "Provided the callerID, this function will return an array containing all the GroupModels that the user has mod role in",
            response = ArrayList.class)
    ArrayList<GroupModel> getModInGroup_ByPath(@ApiParam(
            name = "callerID",
            type = "Integer",
            value = "callerID for the user that called this function",
            example = "2",
            required = true) @PathVariable long callerID) {
        Set<GroupModel> groups = userRepository.findById(callerID).get().getModInGroup();
        ArrayList<GroupModel> returnedArrayList = new ArrayList<>();
        Iterator<GroupModel> ite = groups.iterator();
        while (ite.hasNext()) {
            returnedArrayList.add(ite.next());
        }
        return returnedArrayList;
    }

    @GetMapping("groupMember/getMemberInGroup/{callerID}/")
    @ApiOperation(value = "Get all the groups the user has member role in",
            notes = "Provided the callerID, this function will return an array containing all the GroupModels that the user has member role in",
            response = ArrayList.class)
    ArrayList<GroupModel> getMemberInGroup_ByPath(@ApiParam(
            name = "callerID",
            type = "Integer",
            value = "callerID for the user that called this function",
            example = "2",
            required = true) @PathVariable long callerID) {
        Set<GroupModel> groups = userRepository.findById(callerID).get().getMemberInGroup();
        ArrayList<GroupModel> returnedArrayList = new ArrayList<>();
        Iterator<GroupModel> ite = groups.iterator();
        while (ite.hasNext()) {
            returnedArrayList.add(ite.next());
        }
        return returnedArrayList;
    }


    ArrayList<PostModel> getPostInGroup_ByPath(@PathVariable long userID, @PathVariable long groupID){
        UserModel user = userRepository.findById(userID).get();
        GroupModel group = groupRepository.findById(groupID).get();
        return groupMemberService.getPostsInGroup(user, group);
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
//PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------







}

