package com.example.springboot.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String displayName;
    private int score;
    private String userName;
    private String password;


    @ManyToMany
    @JoinTable(
            name = "admin_in_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private Set<GroupModel> adminInGroup = new HashSet<GroupModel>();

    @ManyToMany
    @JoinTable(
            name = "mod_in_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private Set<GroupModel> modInGroup = new HashSet<GroupModel>();

    @ManyToMany
    @JoinTable(
            name = "member_in_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private Set<GroupModel> memberInGroup = new HashSet<GroupModel>();


    @ManyToMany
    @JoinTable(
            name = "group_join_request",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private Set<GroupModel> joinGroupRequest = new HashSet<GroupModel>();

    @ManyToMany
    @JoinTable(
            name = "user_banned_from_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private Set<GroupModel> bannedFromGroup = new HashSet<GroupModel>();

    @ManyToMany
    @JoinTable(
            name = "group_blocked_by_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @JsonIgnore
    private Set<GroupModel> blockedGroup = new HashSet<GroupModel>();




    //REQUIRE ATTENTION: This function is for mock testing only
    public void addGroupToAdminInGroup(GroupModel group) {
        this.adminInGroup.add(group);
    }


    public boolean addAdminInGroup(GroupModel group) {
        if (this.adminInGroup.contains(group))
            return false;
        this.adminInGroup.add(group);
        return true;
    }


    //Basic user creation function and get/set basic attributes functions
    public UserModel() {
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


// -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------
// (Set AdminInGroup)'s  functions - (Set AdminInGroup)'s  functions - (Set AdminInGroup)'s  functions - (Set AdminInGroup)'s  functions - (Set AdminInGroup)'s  functions - (Set AdminInGroup)'s  functions
// -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------

    /**
     * Get the set of all groups the user is an admin in
     *
     * @return the set of all groups the user is an admin in
     */
    public Set<GroupModel> getAdminInGroup() {
        return this.adminInGroup;
    }


    /**
     * Check if the user has group in adminInGroup set and the group has user in admins set
     *
     * @param group the group that the function is checking
     * @return if the user is an admin in that group
     */
    public boolean isAdminInGroup(GroupModel group) {
        if (this.adminInGroup.contains(group)) {
            if (group.HELPER_checkAdmin(this)) {
                return true;
            } else {
                //If the user says that they are an admin but the group says they are not, then remove the user's admin role
                // => have to call function joinGroup, and some admin has to call function giveAdminRole again where it checks all the preconditions => better
                this.adminInGroup.remove(group);
                return false;
            }
        } else if (group.HELPER_checkAdmin(this)) {
            //If the user says that they are not an admin but the group says they are, then remove the user's admin role
            // => have to call function joinGroup, and some admin has to call function giveAdminRole again where it checks all the preconditions => better
            group.HELPER_removeAdminRole(this);
            return false;
        }
        return false;
    }


    /**
     * This function is used as a helper method for the giveAdminRole
     * If adminInGroup already contains that group then return false, else add it and return true
     *
     * @param group the group that is going to be added
     * @return whether the group was successfully added
     */
    public boolean HELPER_addAdminRoleInGroup(GroupModel group) {
        if (this.adminInGroup.contains(group))
            return false;
        this.adminInGroup.add(group);
        return true;
    }


    /**
     * This function is used as a helper method for removeAdminRole
     * If adminInGroup set doesn't contain that group then return false, else remove it and return true
     *
     * @param group the group that is going to be removed
     * @return whether the group was successfully removed
     */
    public boolean HELPER_removeAdminRoleInGroup(GroupModel group) {
        if (!this.adminInGroup.contains(group))
            return false;
        this.adminInGroup.remove(group);
        return true;
    }


// -----------------------------   -----------------------------   -----------------------------   -----------------------------   -----------------------------   -----------------------------
// (Set ModInGroup)'s  functions - (Set ModInGroup)'s  functions - (Set ModInGroup)'s  functions - (Set ModInGroup)'s  functions - (Set ModInGroup)'s  functions - (Set ModInGroup)'s  functions
// -----------------------------   -----------------------------   -----------------------------   -----------------------------   -----------------------------   -----------------------------


    /**
     * Get the set of all groups the user is a mod in
     *
     * @return the set of all groups the user is a mod in
     */
    public Set<GroupModel> getModInGroup() {
        return this.modInGroup;
    }


    /**
     * Check if the user has group in modInGroup set and the group has user in mods set
     *
     * @param group the group that the function is checking
     * @return if the user is a mod in that group
     */
    public boolean isModInGroup(GroupModel group) {
        if (this.modInGroup.contains(group)) {
            if (group.HELPER_checkMod(this)) {
                return true;
            } else {
                //If the user says that they are a mod but the group says they are not, then remove the user's mod role
                // => have to call function joinGroup, and some admin has to call function giveModRole again where it checks all the preconditions => better
                this.modInGroup.remove(group);
                return false;
            }
        } else if (group.HELPER_checkMod(this)) {
            //If the user says that they are not a mod but the group says they are , then remove the user's mod role
            // => have to call function joinGroup, and some admin has to call function giveModRole again where it checks all the preconditions => better
            group.HELPER_removeModRole(this);
            return false;
        }
        return false;
    }


    /**
     * This function is used as a helper method for the giveModRole
     * If modInGroup already contains that group then return false, else add it and return true
     *
     * @param group the group that is going to be added
     * @return whether the group was successfully added
     */
    public boolean HELPER_addModRoleInGroup(GroupModel group) {
        if (this.modInGroup.contains(group))
            return false;
        this.modInGroup.add(group);
        return true;
    }


    /**
     * This function is used as a helper method for removeModRole
     * If modInGroup set doesn't contain that group then return false, else remove it and return true
     *
     * @param group the group that is going to be removed
     * @return whether the group was successfully removed
     */
    public boolean HELPER_removeModRoleInGroup(GroupModel group) {
        if (!modInGroup.contains(group))
            return false;
        modInGroup.remove(group);
        return true;
    }


// --------------------------------   --------------------------------   --------------------------------   --------------------------------   --------------------------------   --------------------------------
// (Set MemberInGroup)'s  functions - (Set MemberInGroup)'s  functions - (Set MemberInGroup)'s  functions - (Set MemberInGroup)'s  functions - (Set MemberInGroup)'s  functions - (Set MemberInGroup)'s  functions
// --------------------------------   --------------------------------   --------------------------------   --------------------------------   --------------------------------   --------------------------------


    /**
     * Get the set of all groups the user is a member in
     *
     * @return the set of all groups the user is a member in
     */
    public Set<GroupModel> getMemberInGroup() {
        return this.memberInGroup;
    }


    /**
     * Check if the user has group in memberInGroup set and the group has user in members set
     *
     * @param group the group that the function is checking
     * @return if the user is a member in that group
     */
    public boolean isMemberInGroup(GroupModel group) {
        if (this.memberInGroup.contains(group)) {
            if (group.HELPER_checkMember(this)) {
                return true;
            } else {
                //If the user says that they are a member but the group says they are not, then remove the user's member role
                // => have to call function joinGroup again where it checks all the preconditions => better
                this.memberInGroup.remove(group);
                return false;
            }
        } else if (group.HELPER_checkMember(this)) {
            //If the user says that they are not a member but the group says they are, then remove the user's member role
            // => have to call function joinGroup again where it checks all the preconditions => better
            group.HELPER_removeMemberRole(this);
            return false;
        }
        return false;
    }


    /**
     * This function is used as a helper method for the joinGroup
     * If memberInGroup already contains that group then return false, else add it and return true
     *
     * @param group the group that is going to be added
     * @return whether the group was successfully added
     */
    public boolean HELPER_addMemberRoleInGroup(GroupModel group) {
        if (this.memberInGroup.contains(group))
            return false;
        this.memberInGroup.add(group);
        return true;
    }


    /**
     * This function is used as a helper method for exitGroup
     * If memberInGroup set doesn't contain that group then return false, else remove it and return true
     *
     * @param group the group that is going to be removed
     * @return whether the group was successfully removed
     */
    public boolean HELPER_removeMemberRoleInGroup(GroupModel group) {
        if (!this.memberInGroup.contains(group))
            return false;
        this.memberInGroup.remove(group);
        return true;
    }

// -----------------------------------   -----------------------------------   -----------------------------------   -----------------------------------   -----------------------------------
// (Set joinGroupRequest)'s  functions - (Set joinGroupRequest)'s  functions - (Set joinGroupRequest)'s  functions - (Set joinGroupRequest)'s  functions - (Set joinGroupRequest)'s  functions
// -----------------------------------   -----------------------------------   -----------------------------------   -----------------------------------   -----------------------------------

    /**
     * Get the set of all groups the member is requesting to join
     *
     * @return the set of all groups the member is requesting to join
     */
    public Set<GroupModel> getJoinGroupRequest() {
        return this.joinGroupRequest;
    }

    /**
     * Check if the user has group in joinGroupRequest set and the group has user in joinRequestQueue set
     *
     * @param group the group that the function is checking
     * @return if the user is on the joining queue in that group
     */
    public boolean isJoinGroupRequest(GroupModel group) {
        if (this.joinGroupRequest.contains(group)) {
            if (group.HELPER_checkJoinRequestQueue(this)) {
                return true;
            } else {
                //If the user is saying that they are on the list but the group say they are not on the list, then remove the user from that list
                // => set the user to not on the joinRequestQueue => have to call function joinGroup again where it checks all the preconditions => better
                this.joinGroupRequest.remove(group);
                return false;
            }
        } else if (group.HELPER_checkJoinRequestQueue(this)) {
            //If the user is saying they are not on the list but the group has the user on the list then remove the user from that list
            // => set the user to not on the joinRequestQueue => have to call function joinGroup again where it checks all the preconditions => better
            group.HELPER_removeJoinRequestQueue(this);
            return false;
        }
        return false;
    }

    /**
     * This function is used as a helper method for the HELPER_giveMemberRole, which is a helper method for the joinGroup function
     * If joinGroupRequest already contains that group then return false, else add it and return true
     *
     * @param group the group that is going to be added
     * @return whether the group was successfully added
     */
    public boolean HELPER_addJoinGroupRequest(GroupModel group) {
        if (this.joinGroupRequest.contains(group))
            return false;
        this.joinGroupRequest.add(group);
        return true;
    }


    /**
     * This function remove the join group request that the user had
     * If joinGroupRequest set doesn't contain that group then return false, else remove it and return true
     *
     * @param group the group that is going to be removed
     * @return whether the group was successfully removed
     */
    public boolean HELPER_removeJoinGroupRequest(GroupModel group) {
        if (!this.joinGroupRequest.contains(group))
            return false;
        this.joinGroupRequest.remove(group);
        return true;
    }


// ----------------------------------   ----------------------------------   ----------------------------------   ----------------------------------   ----------------------------------
// (Set BannedFromGroup)'s  functions - (Set BannedFromGroup)'s  functions - (Set BannedFromGroup)'s  functions - (Set BannedFromGroup)'s  functions - (Set BannedFromGroup)'s  functions
// ----------------------------------   ----------------------------------   ----------------------------------   ----------------------------------   ----------------------------------

    /**
     * Get the set of all groups the user is banned from
     *
     * @return the set of all groups the user is banned from
     */
    public Set<GroupModel> getBannedFromGroup() {
        return this.bannedFromGroup;
    }

    /**
     * Check if the user has group in bannedFromGroup set and the group has user in bannedUser set
     *
     * @param group the group that the function is checking
     * @return if the user is banned by the group
     */
    public boolean isBannedFromGroup(GroupModel group) {
        if (this.bannedFromGroup.contains(group)) {
            if (group.HELPER_checkBannedUser(this)) {
                return true;
            } else {
                //If the user is saying that they are blocked by the group but the group says that it is not blocking the user, then remove the group off bannedFromGroup set.
                // => follow the status provided by the group since the group is the entity that blocks users
                this.bannedFromGroup.remove(group);
                return false;
            }
        } else if (group.HELPER_checkBannedUser(this)) {
            //If the user is saying that they are not blocked by the group but the group says that it is blocking the user, then add the group onto bannedFromGroup set.
            // => follow the status provided by the group since the group is the entity that blocks users
            this.bannedFromGroup.add(group);
            return true;
        }
        return false;
    }

    /**
     * Add a group into the set bannedFromGroup
     *
     * @param group the group that is going to be added
     * @return whether the group was successfully added
     */
    public boolean HELPER_addBannedFromGroup(GroupModel group) {
        if (this.bannedFromGroup.contains(group))
            return false;
        this.bannedFromGroup.add(group);
        return true;
    }

    /**
     * Remove a group out of the set bannedFromGroup
     *
     * @param group the group that is going to be removed
     * @return whether the group was successfully removed
     */
    public boolean HELPER_removeBannedFromGroup(GroupModel group) {
        if (!this.bannedFromGroup.contains(group))
            return false;
        this.bannedFromGroup.remove(group);
        return true;
    }

// -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------
// (Set BlockedGroup)'s  functions - (Set BlockedGroup)'s  functions - (Set BlockedGroup)'s  functions - (Set BlockedGroup)'s  functions - (Set BlockedGroup)'s  functions - (Set BlockedGroup)'s  functions
// -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------   -------------------------------

    /**
     * Get the set of all groups the user is banned from
     *
     * @return the set of all groups the user is banned from
     */
    public Set<GroupModel> getBlockedGroup() {
        return this.blockedGroup;
    }

    /**
     * Check if the user has group in blockedGroup and the group has user in blockedUser list
     *
     * @param group the group that the function is checking
     * @return if the user is banned by the group
     */
    public boolean isBlockedGroup(GroupModel group) {
        if (this.blockedGroup.contains(group)) {
            if (group.HELPER_checkUserBlocked(this)) {
                return true;
            } else {
                //If the user is saying that they are blocking the group and the group is saying that they are not being blocked by the user then add user onto the UserBlocked set
                // => follow the status provided by the user since the user is the entity blocking the group
                group.HELPER_addUserBlocked(this);
                return true;
            }
        } else if (group.HELPER_checkUserBlocked(this)) {
            //If the user is saying that they are not blocking the group and the group is saying that they are being blocked by the user then remove user out of UserBlocked set
            // => follow the status provided by the user since the user is the entity blocking the group
            group.HELPER_removeUserBlocked(this);
            return false;
        }
        return false;
    }

    /**
     * Add a group to the set blockedGroup
     *
     * @param group the group that is going to be added
     * @return whether the group was successfully added
     */
    public boolean HELPER_addBlockedGroup(GroupModel group) {
        if (this.blockedGroup.contains(group))
            return false;
        this.blockedGroup.add(group);
        return true;
    }

    /**
     * Remove a group out of the set blockedGroup
     *
     * @param group the group that is going to be removed
     * @return whether the group was successfully removed
     */
    public boolean HELPER_removeBlockedGroup(GroupModel group) {
        if (!this.blockedGroup.contains(group))
            return false;
        this.blockedGroup.remove(group);
        return true;
    }

}
