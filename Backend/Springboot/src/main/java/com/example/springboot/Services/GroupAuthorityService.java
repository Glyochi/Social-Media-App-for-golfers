package com.example.springboot.Services;

import com.example.springboot.Models.Exception.GroupException.NoAdminException;
import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Repositories.GroupRepository;
import com.example.springboot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class GroupAuthorityService {



    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;

    public GroupAuthorityService(){};


//---------------------   ---------------------   ---------------------   ---------------------   ---------------------   ---------------------   ---------------------   ---------------------
//Group admin functions - Group admin functions - Group admin functions - Group admin functions - Group admin functions - Group admin functions - Group admin functions - Group admin functions
//---------------------   ---------------------   ---------------------   ---------------------   ---------------------   ---------------------   ---------------------   ---------------------





// Group admin's admin functions --------- Group admin's admin functions --------- Group admin's admin functions --------- Group admin's admin functions --------- Group admin's admin functions

    /**
     * If the caller has permission, they will give the target admin role in the specified group
     *
     * @param caller  the caller thats calling this function
     * @param group  the group that the caller has admin privilege
     * @param target the target that is getting the admin role
     * @return whether the target was successfully assigned the role or not
     */
    public boolean giveAdminRole(UserModel caller, GroupModel group, UserModel target) {

        if (group.HELPER_inGroup(caller) == GroupModel.ADMIN) {
            int t = group.HELPER_inGroup(target);
            if(t == -1)
                return false;
            else if(t == 0){
                //Give admin role when user is currently a member
                group.HELPER_removeMemberRole(target);
                target.HELPER_removeMemberRoleInGroup(group);
            }
            else if(t == 1){
                //Give admin role when user is currently a mod
                group.HELPER_removeModRole(target);
                target.HELPER_removeModRoleInGroup(group);
            }
            else if(t == 2){
                return false;
            }
            group.HELPER_giveAdminRole(target);
            target.HELPER_addAdminRoleInGroup(group);
            return true;
        }

        //If user doesn't have permission then return false
        return false;
    }


    /**
     * If the caller has permission, they will remove the target's admin role in the specified group
     *
     * @param caller  the caller thats calling this function
     * @param group  the group that the caller has admin privilege
     * @param target the target that is losing the admin role
     * @throws NoAdminException if there's only one admin and that admin is trying to remove his admin role, then throw this error because there has to always be at least one admin
     * @return whether the target's role was successfully removed the role or not
     */
    public boolean removeAdminRole(UserModel caller, GroupModel group, UserModel target) throws NoAdminException {


        if (group.HELPER_inGroup(caller) == GroupModel.ADMIN) {
            if (caller.equals(target)) {
                if (group.getAdmins().size() == 1) {
                    //if the admin is trying to remove his admin role and there are no more admin then throw error
                    throw new NoAdminException("Group " + group.getName() + " needs to have at least one admin");
                }
            }
            group.HELPER_removeAdminRole(target);
            group.HELPER_giveMemberRole(target);
            target.HELPER_removeAdminRoleInGroup(group);
            target.HELPER_addMemberRoleInGroup(group);
            return true;
        }

        //If user doesn't have permission then return false
        return false;
    }



// Group admin's mod functions --------- Group admin's mod functions --------- Group admin's mod functions --------- Group admin's mod functions --------- Group admin's mod functions --------- Group admin's mod functions


    /**
     * If the caller has permission, they will give the target mod role in the specified group
     *
     * @param caller  the caller thats calling this function
     * @param group  the group that the caller has mod privilege
     * @param target the target that is getting the mod role
     * @return whether the target was successfully assigned the role or not
     */
    public boolean giveModRole(UserModel caller, GroupModel group, UserModel target) {

        if (group.HELPER_inGroup(caller) >= GroupModel.MOD) {
            int t = group.HELPER_inGroup(target);
            if(t == 0){
                //Only give mod role if user is currently a member
                group.HELPER_removeMemberRole(target);
                target.HELPER_removeMemberRoleInGroup(group);
            }
            else {
                return false;
            }
            group.HELPER_giveModRole(target);
            target.HELPER_addModRoleInGroup(group);
            return true;
        }

        //If user doesn't have permission then return false
        return false;
    }


    /**
     * Remove mod role of a user in a group
     *
     * @param caller  the caller thats calling this function
     * @param group  the group that the target is a mod in
     * @param target the user who's mod role is going to get removed
     * @return Whether the removal process was successful or not
     */
    public boolean removeModRole(UserModel caller, GroupModel group, UserModel target) {
        //So if the function caller is an admin (higher priviledge than mod)
        //or if the function caller is a mod and is the target (trying to resign from being a mod)
        if (group.HELPER_inGroup(caller) >= GroupModel.MOD) {
            group.HELPER_removeModRole(target);
            group.HELPER_giveMemberRole(target);
            target.HELPER_removeModRoleInGroup(group);
            target.HELPER_addMemberRoleInGroup(group);
            return true;
        }

        //If user doesn't have permission then return false
        return false;
    }



// Group admin's member functions --------- Group admin's member functions --------- Group admin's member functions --------- Group admin's member functions --------- Group admin's member functions

    /**
     * This function is not meant to be used. Admin/Mod cannot just give a user member's role.
     * They can only accept a user's join request.
     */
    private boolean giveMemberRole(UserModel caller, GroupModel group, UserModel target) {
        return false;
    }


    /**
     * If the caller has permission, they will give the target mod role in the specified group
     *
     * @param caller  the caller thats calling this function
     * @param group  the group that the caller has mod privilege
     * @param target the target that is getting the mod role
     * @return Whether the removal process was successful or not
     */
    public boolean removeMemberRole(UserModel caller, GroupModel group, UserModel target) {
        //So if the function caller is an admin
        //or if the function caller is a mod and is the target (trying to resign from being a mod)
        if ((group.HELPER_inGroup(target) == GroupModel.MEMBER) && group.HELPER_inGroup(caller) >= GroupModel.MOD) {
            group.HELPER_removeMemberRole(target);
            target.HELPER_removeMemberRoleInGroup(group);
            return true;
        }
        return false;
    }



// Group admin's joinRequestQueue functions --------- Group admin's joinRequestQueue functions --------- Group admin's joinRequestQueue functions --------- Group admin's joinRequestQueue functions

    /**
     * If the caller has permission, they will accept a user's join group request and make them a member
     *
     * @param caller  the caller thats calling this function
     * @param group  the group that the caller has privilege in
     * @param target the target that requested to join the group
     * @return whether the target was successfully added as a member or not
     */
    public boolean acceptJoinRequest(UserModel caller, GroupModel group, UserModel target) {

        if (target.isJoinGroupRequest(group) && group.HELPER_inGroup(caller) >= GroupModel.MOD) {
            group.HELPER_removeJoinRequestQueue(target);
            group.HELPER_giveMemberRole(target);
            target.HELPER_removeJoinGroupRequest(group);
            target.HELPER_addMemberRoleInGroup(group);
            return true;
        }

        //If user doesn't have permission then return false
        return false;
    }

    /**
     * If the caller has permission, they will reject a user's join group request and make them a member
     *
     * @param caller  the caller thats calling this function
     * @param group  the group that the caller has privilege in
     * @param target the target that requested to join the group
     * @return whether the target was successfully rejected or not
     */
    public boolean rejectJoinRquest(UserModel caller, GroupModel group, UserModel target) {

        if (target.isJoinGroupRequest(group) && group.HELPER_inGroup(caller) >= GroupModel.MOD) {
            group.HELPER_removeJoinRequestQueue(target);
            target.HELPER_removeJoinGroupRequest(group);
            return true;
        }

        //If user doesn't have permission then return false
        return false;
    }


// Group admin's bannedUser functions --------- Group admin's bannedUser functions --------- Group admin's bannedUser functions --------- Group admin's bannedUser functions --------- Group admin's bannedUser functions

    /**
     * Ban a user and add them to the banned list, also add the group to the user's bannedFromGroup list
     * @param caller the caller that called this function
     * @param group the group the target is banned from
     * @param target the user that is getting banned
     * @return whether the ban was successfully delivered or not
     */
    public boolean banUser(UserModel caller, GroupModel group, UserModel target) {
        int callerRole = group.HELPER_inGroup(caller);
        int targetRole = group.HELPER_inGroup(target);
        if(callerRole >= GroupModel.MOD && callerRole > targetRole){
            if(targetRole == GroupModel.MEMBER){
                group.HELPER_removeMemberRole(target);
                target.HELPER_removeMemberRoleInGroup(group);
            }else if (targetRole == GroupModel.MOD){
                group.HELPER_removeModRole(target);
                target.HELPER_removeModRoleInGroup(group);
            }
            group.HELPER_addBannedUser(target);
            target.HELPER_addBannedFromGroup(group);
            return true;
        }
        return false;
    }

    /**
     * Un-Ban a user and remove them to the banned list, also remove the group from the user's bannedFromGroup list
     * @param caller the caller that called this function
     * @param group the group the target was banned from
     * @param target the user that is getting their ban removed
     * @return whether the ban was successfully removed or not
     */
    public boolean unbanUser(UserModel caller, GroupModel group, UserModel target) {
        if(group.HELPER_checkBannedUser(target) && group.HELPER_inGroup(caller) >= GroupModel.MOD){
            group.HELPER_removeBannedUser(target);
            target.HELPER_removeBannedFromGroup(group);
        }
        return false;
    }



}
