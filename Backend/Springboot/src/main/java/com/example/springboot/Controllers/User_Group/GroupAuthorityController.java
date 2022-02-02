package com.example.springboot.Controllers.User_Group;

import com.example.springboot.Models.Exception.GroupException.NoAdminException;
import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Repositories.GroupRepository;
import com.example.springboot.Repositories.UserRepository;
import com.example.springboot.Services.GroupAuthorityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class GroupAuthorityController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    GroupAuthorityService groupAuthorityService;

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST _ GET REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST _ POST REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
//DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST _ DELETE REQUEST
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


//---------------------------------------------------------------------------------------------------------------------------------------------------------------------
//PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST _ PUT REQUEST
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @PutMapping("groupAuthority/giveAdminRoleInGroup/{callerID}/{groupID}/{targetID}")
    @ApiOperation(value = "Give admin role to a user in a certain group",
            notes = "Provided a user's ID, group ID, and adminID, this function will grant a user in a group with the rank admin",
            response = ArrayList.class)
    public boolean giveAdminRoleInGroup_ByPath(@ApiParam(
            name = "callerID",
            type = "Integer",
            value = "ID of the caller that has access to this function",
            example = "2",
            required = true)
                                               @PathVariable long callerID, @ApiParam(
            name = "groupID",
            type = "long",
            value = "Group ID to specific in which group",
            example = "2",
            required = true) @PathVariable long groupID, @ApiParam(
            name = "targetID",
            type = "Integer",
            value = "ID of the target that is getting the role",
            example = "2",
            required = true) @PathVariable long targetID) throws Exception {
        UserModel caller = userRepository.findById(callerID).get();
        GroupModel group = groupRepository.findById(groupID).get();
        UserModel target = userRepository.findById(targetID).get();


        if (groupAuthorityService.giveAdminRole(caller, group, target)) {
            groupRepository.save(group);
            userRepository.save(target);
            return true;
        }
        return false;
    }


    @PutMapping("groupAuthority/removeAdminRoleInGroup/{callerID}/{groupID}/{targetID}")
    public boolean removeAdminRoleInGroup_ByPath(@PathVariable long callerID, @PathVariable long groupID, @PathVariable long targetID) {
        UserModel caller = userRepository.findById(callerID).get();
        GroupModel group = groupRepository.findById(groupID).get();
        UserModel target = userRepository.findById(targetID).get();

        try {
            if (groupAuthorityService.removeAdminRole(caller, group, target)) {
                groupRepository.save(group);
                userRepository.save(target);
                return true;
            }
        }catch (NoAdminException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @PutMapping("groupAuthority/giveModRoleInGroup/{callerID}/{groupID}/{targetID}")
    public boolean giveModRoleInGroup_ByPath(@PathVariable long callerID, @PathVariable long groupID, @PathVariable long targetID) throws Exception {
        UserModel caller = userRepository.findById(callerID).get();
        GroupModel group = groupRepository.findById(groupID).get();
        UserModel target = userRepository.findById(targetID).get();


        if (groupAuthorityService.giveModRole(caller, group, target)) {
            groupRepository.save(group);
            userRepository.save(target);
            return true;
        }
        return false;
    }


    @PutMapping("groupAuthority/removeModRoleInGroup/{callerID}/{groupID}/{targetID}")
    public boolean removeModRoleInGroup_ByPath(@PathVariable long callerID, @PathVariable long groupID, @PathVariable long targetID) throws Exception {
        UserModel caller = userRepository.findById(callerID).get();
        GroupModel group = groupRepository.findById(groupID).get();
        UserModel target = userRepository.findById(targetID).get();


        if (groupAuthorityService.removeModRole(caller, group, target)) {
            groupRepository.save(group);
            userRepository.save(target);
            return true;
        }
        return false;
    }
}
