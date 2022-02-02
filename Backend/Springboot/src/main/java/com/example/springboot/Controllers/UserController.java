package com.example.springboot.Controllers;

import com.example.springboot.Models.Exception.UserException.UserNameAlreadyExistException;
import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Repositories.UserRepository;
import com.example.springboot.Repositories.GroupRepository;
import com.example.springboot.Services.GroupMemberService;
import com.example.springboot.Services.UserService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.apache.catalina.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;


    @Autowired
    UserService userService;

    @Autowired
    GroupMemberService groupMemberService;


    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("user/getAllUser")
    @ApiOperation(value = "Get the list of all the users",
            notes = "Return a list of all the users",
            response = List.class)
    List<UserModel> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping("user/getUserByID/{userID}")
    @ApiOperation(value = "Get user using callerID",
            notes = "Provide the userID to look up a user in the repository",
            response = UserModel.class)
    UserModel getUserByID_ByPath(@ApiParam(
            name = "userID",
            type = "Long",
            value = "userID for the user needed to be retrieved",
            example = "2",
            required = true)
                                 @PathVariable long userID) {
        return userRepository.getById(userID);
    }

    @GetMapping("user/getUserByCredentials/{userName}/{password}")
    @ApiOperation(value = "Get user using login credentials",
            notes = "Provide the credentials to look up a user in the repository",
            response = UserModel.class)
    UserModel getUserByCredentials(@ApiParam(
            name = "userName",
            type = "String",
            value = "the login name used to log in an account",
            example = "noobmaster",
            required = true)
                                   @PathVariable String userName,
                                   @ApiParam(
                                           name = "password",
                                           type = "String",
                                           value = "the password used to log in an account",
                                           example = "onetwothreefour",
                                           required = true)
                                   @PathVariable String password) {
        return userService.getUserByCredentials(userName, password);
    }

    @GetMapping("user/checkIfUserNameExisted/{userName}")
    @ApiOperation(value = "Check if the userName has already existed or not",
            notes = "Used to keep unique login credentials",
            response = boolean.class)
    boolean checkIfUserNameExisted(@ApiParam(
            name = "userName",
            type = "String",
            value = "the userName that the user want to have",
            example = "onetwothreefour",
            required = true)
                                   @PathVariable String userName) {
        return userService.checkIfUserNameExisted(userName);
    }


    @GetMapping("user/getJoinedGroups/{userID}")
    @ApiOperation(value = "Get user using callerID",
            notes = "Provide the userID to get all the user's joined groups",
            response = List.class)
    List<GroupModel> getJoinedGroups_ByPath(@ApiParam(
            name = "userID",
            type = "Long",
            value = "userID for the user needed to be retrieved",
            example = "2",
            required = true)
                                            @PathVariable long userID) {
        return userService.getJoinedGroups(userID);
    }

    @GetMapping("user/isJoinedGroup/{userID}/{groupID}")
    @ApiOperation(value = "check if the user is in the group",
            notes = "Provide the userID and groupID to check if the user is in the group",
            response = List.class)
    boolean getJoinedGroups_ByPath(@ApiParam(
            name = "userID",
            type = "Long",
            value = "userID for the user needed to be retrieved",
            example = "2",
            required = true)
                                            @PathVariable long userID,
                                            @ApiParam(
                                                    name = "groupID",
                                                    type = "Long",
                                                    value = "groupID of the group that is being checkd",
                                                    example = "2",
                                                    required = true)
                                            @PathVariable long groupID) {
        UserModel user = userRepository.findById(userID).get();
        GroupModel group = groupRepository.findById(groupID).get();
        if(user.getMemberInGroup().contains(group) ||
                user.getModInGroup().contains(group)||
                user.getAdminInGroup().contains(group)){
            return true;
        }
        return false;
    }


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @PostMapping("user/createNewUser/{displayName}/{userScore}/{userName}/{password}")
    @ApiOperation(value = "Create new user",
            notes = "Provided the name, userScore, userName, and password, this function will return a new user",
            response = UserModel.class)
    UserModel createNewUser_ByPath(@ApiParam(
            name = "displayName",
            type = "String",
            value = "what the new user wants to be called",
            example = "bot_1",
            required = true)
                                   @PathVariable String displayName,
                                   @ApiParam(name = "userScore",
                                           type = "int",
                                           value = "Score to enter for the user",
                                           example = "52",
                                           required = true)
                                   @PathVariable int userScore,
                                   @ApiParam(name = "userName",
                                           type = "String",
                                           value = "the login name",
                                           example = "IAmBlue",
                                           required = true)
                                   @PathVariable String userName,
                                   @ApiParam(name = "password",
                                           type = "String",
                                           value = "password for the user",
                                           example = "thisisapassword",
                                           required = true)
                                   @PathVariable String password
    ) {
        if (userService.checkIfUserNameExisted(userName)) {
            System.out.println("Username: " + userName + " has already existed");
            return null;
        }
        return userService.createNewUser(displayName, userScore, userName, password);
    }

    @PostMapping("user/changeUserPassword/{userID}/{oldPassword}/{newPassword}")
    @ApiOperation(value = "Change an existing user's password",
            notes = "Provided the userID, oldPassword, and  newPassword, this function will change the user with the specified id's password",
            response = boolean.class)
    boolean changeUserPassword(@ApiParam(
            name = "userID",
            type = "Long",
            value = "the user's ID",
            example = "121",
            required = true)
                               @PathVariable Long userID,
                               @ApiParam(
                                       name = "old password",
                                       type = "String",
                                       value = "the user's old password",
                                       example = "onetwothreefour",
                                       required = true)
                               @PathVariable String oldPassword,
                               @ApiParam(
                                       name = "new password",
                                       type = "String",
                                       value = "the user's new password",
                                       example = "fourthreetwoone",
                                       required = true)
                               @PathVariable String newPassword
    ) {
        return userService.changeUserPassword(userID, oldPassword, newPassword);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @DeleteMapping("user/deleteUser/{callerID}")
    @ApiOperation(value = "delete user",
            notes = "Provided a user's ID, this function will delete the user",
            response = ArrayList.class)
    void deleteUser_ByPath(@ApiParam(
            name = "callerID",
            type = "long",
            value = "User ID provided for deletion",
            example = "2",
            required = true) @PathVariable long callerID) {
        userRepository.deleteById(callerID);
    }

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
//PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @PutMapping("user/joinGroup/{callerID}/{groupID}")
    @ApiOperation(value = "user join a group",
            notes = "Provided a user's ID and group's ID, this function will let the user join the group",
            response = ArrayList.class)
    boolean joinGroup_ByPath(@ApiParam(
            name = "callerID",
            type = "long",
            value = "UserID",
            example = "2",
            required = true) @PathVariable long callerID, @ApiParam(
            name = "groupID",
            type = "long",
            value = "groupID",
            example = "2",
            required = true) @PathVariable long groupID) {
        return groupMemberService.joinGroup(userRepository.findById(callerID).get(), groupRepository.findById(groupID).get());
    }

    @PutMapping("user/leaveGroup/{callerID}/{groupID}")
    @ApiOperation(value = "user leave a group",
            notes = "Provided a user's ID and group's ID, this function will let the user leave the group",
            response = ArrayList.class)
    boolean leaveGroup_ByPath(@ApiParam(
            name = "callerID",
            type = "long",
            value = "UserID",
            example = "2",
            required = true) @PathVariable long callerID, @ApiParam(
            name = "groupID",
            type = "long",
            value = "groupID",
            example = "2",
            required = true) @PathVariable long groupID) {
        return groupMemberService.leaveGroup(userRepository.findById(callerID).get(), groupRepository.findById(groupID).get());
    }

}
