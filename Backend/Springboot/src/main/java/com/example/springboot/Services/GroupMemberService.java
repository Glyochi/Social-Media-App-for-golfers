package com.example.springboot.Services;

import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.PostModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Repositories.GroupRepository;
import com.example.springboot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class GroupMemberService {


    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


//---------------------------   ---------------------------   ---------------------------   ---------------------------   ---------------------------   ---------------------------   ---------------------------
//Group interaction functions - Group interaction functions - Group interaction functions - Group interaction functions - Group interaction functions - Group interaction functions - Group interaction functions
//---------------------------   ---------------------------   ---------------------------   ---------------------------   ---------------------------   ---------------------------   ---------------------------


    public GroupModel createGroup(UserModel creator, String groupName, boolean permissionRequired) {
        GroupModel newGroup = new GroupModel();
        newGroup.setName(groupName);
        newGroup.HELPER_setJoiningRequirePermission(permissionRequired);
        newGroup.assignCreator(creator);

        groupRepository.save(newGroup);
        userRepository.save(creator);
        return newGroup;
    }


    /**
     * Check if the group and the user can interact (false if the group banned the user or the user blocked the group)
     *
     * @param group the group that the function is checking
     * @param user the user that the function is checking
     * @return whether the group and the user can interact
     */
    public boolean canInteractWithGroup(UserModel user, GroupModel group) {
        //Check whether the group banned the user or the user blocked the group
        if (group.HELPER_checkBannedUser(user) || user.isBlockedGroup(group)) {
            return false;
        }

        return true;
    }


    /**
     * Joining a group. If the user cannot interact with the group then return false.
     * Else return true. Still it depends on the setting of the group, the user either get the member role or go into a joinRequestQueue.
     *
     * @param group the group the user is trying to join
     * @param user the user that's trying to join the group
     * @return Whether joining the group was successful or not
     */
    public boolean joinGroup(UserModel user, GroupModel group) {
//        if (!canInteractWithGroup(user, group))
//            return false;
//
//        if (group.getJoiningRequirePermission()) {
//            user.HELPER_addJoinGroupRequest(group);
//            group.HELPER_addJoinRequestQueue(user);
//
//            userRepository.save(user);
//            groupRepository.save(group);
//            return true;
//        }
        if(group.getMembers().contains(user) || group.getMods().contains(user) || group.getAdmins().contains(user)){
            return false;
        }
        user.HELPER_addMemberRoleInGroup(group);
        group.HELPER_giveMemberRole(user);

        return true;
    }


    /**
     * Leaving a group.... more comments but time constraint so no
     *
     * @param group the group the user is trying to leave
     * @param user the user that's trying to leave the group
     * @return Whether leaving the group was successful or not
     */
    public boolean leaveGroup(UserModel user, GroupModel group) {

        Set<GroupModel> temp = user.getMemberInGroup();
        if(temp.contains(group)){
            user.HELPER_removeMemberRoleInGroup(group);
            group.HELPER_removeMemberRole(user);
            userRepository.save(user);
            groupRepository.save(group);
            return true;
        }
        temp = user.getModInGroup();
        if(temp.contains(group)){
            user.HELPER_removeModRoleInGroup(group);
            group.HELPER_removeModRole(user);
            userRepository.save(user);
            groupRepository.save(group);
            return true;
        }
        temp = user.getAdminInGroup();
        if(temp.contains(group)){
            user.HELPER_removeAdminRoleInGroup(group);
            group.HELPER_removeAdminRole(user);
            userRepository.save(user);
            groupRepository.save(group);
            return true;
        }
        return false;
    }

    /**
     * Block a group. If the group was already blocked then return false.
     *
     * @param group the group that is being blocked
     * @param user the user that's blocking the group
     * @return whether blocking the group was successful or not
     */
    public boolean blockGroup(UserModel user, GroupModel group) {
        if (!user.getBlockedGroup().contains(group)) {
            user.HELPER_addBlockedGroup(group);
            group.HELPER_addUserBlocked(user);

            return true;
        }
        return false;
    }

    /**
     * Unblock a group. If the group wasn't blocked then return false.
     *
     * @param group the group that is being unblocked
     * @param user the user that's unblocking the group
     * @return whether unblocking the group was successful or not
     */
    public boolean unblockGroup(UserModel user, GroupModel group) {
        if (user.isBlockedGroup(group)) {
            user.HELPER_removeBlockedGroup(group);
            group.HELPER_removeUserBlocked(user);

            return true;
        }
        return false;
    }

    /**
     * Get all the posts in the group
     * @param user the user that's calling this function
     * @param group the group that the user want to see posts from
     * @return an arrayList of all posts from the group
     */
    public ArrayList<PostModel> getPostsInGroup(UserModel user, GroupModel group){
        ArrayList<PostModel> posts = new ArrayList<>();
        if(group.HELPER_inGroup(user) >= GroupModel.MEMBER){
            Iterator<PostModel> ite = group.getPosts().iterator();
            while(ite.hasNext()){
                posts.add(ite.next());
            }
        }
        return posts;
    }

}
