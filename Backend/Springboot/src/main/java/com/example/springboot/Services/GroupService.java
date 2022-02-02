package com.example.springboot.Services;

import com.example.springboot.Models.GroupModel;
import com.example.springboot.Repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {



    @Autowired
    GroupRepository groupRepository;

    /**
     * Return the number of members (including admins and mods) in a group
     * @param groupID
     * @return the number of users in the specified group
     */
    public int getGroupMemberCount(long groupID){
        GroupModel group = groupRepository.findById(groupID).get();
        return group.getAdmins().size() + group.getMods().size() + group.getMembers().size();
    }

    public boolean checkIfGroupNameExisted(String groupName){
        if(groupRepository.findGroupModelByName(groupName) != null)
            return true;
        return false;
    }
}
