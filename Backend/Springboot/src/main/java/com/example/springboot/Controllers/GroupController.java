package com.example.springboot.Controllers;

import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Repositories.GroupRepository;
import com.example.springboot.Repositories.UserRepository;
import com.example.springboot.Services.GroupService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
public class GroupController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupService groupService;


//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------
//GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST
//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------

    @GetMapping("group/getAllGroup")
    @ApiOperation(value = "Gets all groups that a user is part of",
            notes = "This function will return all groups that the user calling it is a part of",
            response = ArrayList.class)
    List<GroupModel> getAllGroup() {
        return groupRepository.findAll();
    }

    @GetMapping("group/getGroup/{groupID}")
    @ApiOperation(value = "Gets a specific group based on ID",
            notes = "Provided the group's ID, This function will return the group with that specific ID",
            response = ArrayList.class)
    GroupModel getGroupByID_ByPath(@ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to return",
            example = "2",
            required = true)
                                   @PathVariable long groupID) {
        return groupRepository.findById(groupID).get();
    }

    @GetMapping("group/getGroupAdmins/{groupID}")
    @ApiOperation(value = "Gets a list of a specific groups admins",
            notes = "Provided the group's ID, This function will return a list of admins in the group currently",
            response = ArrayList.class)
    ArrayList<UserModel> getGroupAdmins_ByPath(@ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to return admins",
            example = "2",
            required = true) @PathVariable long groupID) {
        Set<UserModel> groups = groupRepository.findById(groupID).get().getAdmins();
        ArrayList<UserModel> returnedArrayList = new ArrayList<>();
        Iterator<UserModel> ite = groups.iterator();
        while (ite.hasNext()) {
            returnedArrayList.add(ite.next());
        }
        return returnedArrayList;
    }

    @GetMapping("group/getGroupMods/{groupID}")
    @ApiOperation(value = "Gets a list of a specific groups mods",
            notes = "Provided the group's ID, This function will return a list of mods in the group currently",
            response = ArrayList.class)
    Set<UserModel> getGroupMods_ByPath(@ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to return mods",
            example = "2",
            required = true) @PathVariable long groupID) {
        return groupRepository.findById(groupID).get().getMods();
    }

    @GetMapping("group/getGroupMembers/{groupID}")
    @ApiOperation(value = "Gets a list of a specific groups members",
            notes = "Provided the group's ID, This function will return a list of members in the group currently",
            response = ArrayList.class)
    Set<UserModel> getGroupMembers_ByPath(@ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to return members",
            example = "2",
            required = true) @PathVariable long groupID) {
        return groupRepository.findById(groupID).get().getMembers();
    }

    @GetMapping("group/getGroupJoinRequestQueue/{groupID}")
    @ApiOperation(value = "Gets a list of a specific groups join requests from other users",
            notes = "Provided the group's ID, This function will return join requests from other users trying to join the group",
            response = ArrayList.class)
    Set<UserModel> getGroupJoinRequestQueue_ByPath(@ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to return members",
            example = "2",
            required = true) @PathVariable long groupID) {
        return groupRepository.findById(groupID).get().getJoinRequestQueue();
    }

    @GetMapping("group/getGroupBannedUser/{groupID}")
    @ApiOperation(value = "Gets a list of a specific groups banned users",
            notes = "Provided the group's ID, This function will return banned users that have been kicked out of the group",
            response = ArrayList.class)
    Set<UserModel> getGroupBannedUsers_ByPath(@ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to return members",
            example = "2",
            required = true) @PathVariable long groupID) {
        return groupRepository.findById(groupID).get().getBannedUsers();
    }
//    //REQUIRE ATTENTION: Might not include this function because a group doesn't need to see ppl who blocked the group
//    @GetMapping("group/get/getGroupUserBlocked/{groupID}")
//    Set<UserModel> Get_GetGroupUserBlocked_ByPath(@PathVariable long groupID){
//        return groupRepository.getById(groupID).getUserBlocked();
//    }


    @GetMapping("group/getGroupMemberCount/{groupID}")
    @ApiOperation(value = "Gets the number of members in the specified group",
            notes = "Provided the group's ID, This function will return the number of members",
            response = Integer.class)
    Integer getGroupMemberCount_ByPath(@ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to return members count",
            example = "2",
            required = true) @PathVariable long groupID) {
        return groupService.getGroupMemberCount(groupID);
    }


    @GetMapping("group/checkIfGroupNameExisted/{groupName}")
    @ApiOperation(value = "Check if the groupName has already existed or not",
            notes = "Used to keep unique group name",
            response = boolean.class)
    boolean checkIfGroupNameExisted(@ApiParam(
            name = "groupName",
            type = "String",
            value = "the groupName that the user want to have",
            example = "onetwothreefour",
            required = true)
                                    @PathVariable String groupName) {
        return groupService.checkIfGroupNameExisted(groupName);
    }



//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------
//POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST
//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------


    @PostMapping("group/createNewGroup/{groupName}/{groupDescription}/{creatorUserID}")
    @ApiOperation(value = "A user creates a new group and sets its name",
            notes = "Provided the user's ID and a group name, This function will create a new group with specified name",
            response = ArrayList.class)
    GroupModel createNewGroup_ByPath(@ApiParam(
            name = "groupName",
            type = "String",
            value = "name that you want the group to be called",
            example = "God_Squad",
            required = true) @PathVariable String groupName,
                               @ApiParam(
                                       name = "groupDescription",
                                       type = "String",
                                       value = "the group's description",
                                       example = "God_Squad",
                                       required = true) @PathVariable String groupDescription,
                               @ApiParam(
                                       name = "creatorUserID",
                                       type = "long",
                                       value = "User ID that is creating the new group",
                                       example = "2",
                                       required = true) @PathVariable long creatorUserID) {
        UserModel creator = userRepository.findById(creatorUserID).get();
        GroupModel newGroup = new GroupModel();
        newGroup.assignCreator(creator);
        newGroup.setName(groupName);
        newGroup.setDescription(groupDescription);
        newGroup.setJoiningRequirePermission(false);
        groupRepository.save(newGroup);
        userRepository.save(creator);

        return newGroup;
    }


//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------
//PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST
//-----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------   -----------


}
