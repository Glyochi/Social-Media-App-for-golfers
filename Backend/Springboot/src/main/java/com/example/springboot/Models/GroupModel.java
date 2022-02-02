package com.example.springboot.Models;


import com.example.springboot.Services.GroupAuthorityService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class GroupModel {

    public static final int USER_BLOCKED = -3;
    public static final int BANNED = -2;
    public static final int NON_MEMBER = -1;
    public static final int MEMBER = 0;
    public static final int MOD = 1;
    public static final int ADMIN = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;


    private boolean joiningRequirePermission;

    //This boolean will only get flipped to true once and stay true.
    //This is to ensure the function assignCreator will only be able to be called once.
    @JoinColumn
    private boolean assignedCreator = false;


    @ManyToMany(mappedBy = "adminInGroup")
    @JsonIgnore
    private Set<UserModel> admins = new HashSet<UserModel>();

    @ManyToMany(mappedBy = "modInGroup")
    @JsonIgnore
    private Set<UserModel> mods = new HashSet<UserModel>();

    @ManyToMany(mappedBy = "memberInGroup")
    @JsonIgnore
    private Set<UserModel> members = new HashSet<UserModel>();

    @ManyToMany(mappedBy = "joinGroupRequest")
    @JsonIgnore
    private Set<UserModel> joinRequestQueue = new HashSet<UserModel>();

    @ManyToMany(mappedBy = "bannedFromGroup")
    @JsonIgnore
    private Set<UserModel> bannedUsers = new HashSet<UserModel>();

    @ManyToMany(mappedBy = "blockedGroup")
    @JsonIgnore
    private Set<UserModel> userBlocked = new HashSet<UserModel>();

    public GroupModel() {
    }

    /**
     * This function will only be called once after the constructor. After that this function does nothing.
     * This function assign add an UserModel creator to the group's admins set, and add this group to that creator's adminInGroup set.
     *
     * @param creator the creator of the group
     */
    public void assignCreator(UserModel creator) {
        if (!assignedCreator) {
            System.out.println(admins.contains(creator));
            assignedCreator = true;
            this.admins.add(creator);
            creator.HELPER_addAdminRoleInGroup(this);
            System.out.println(admins.contains(creator));
        }
    }

    public boolean assignAdmin(UserModel admin){
        if(this.admins.contains(admin)){
            return false;
        }
        this.admins.add(admin);
        return true;
    }

    //Group attributes

    /**
     * Return the group's unique ID
     *
     * @return group's ID
     */
    public long getId() {
        return id;
    }

//    /**
//     * Set the group's unique ID
//     * @param id
//     */
//    public void setId(int id) {
//        this.id = id;
//    }

    /**
     * Return the group's name
     *
     * @return group's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the group's name
     *
     * @param name group's new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the group's joiningRequirePermission status
     *
     * @return group's joiningRequirePermission
     */
    public boolean getJoiningRequirePermission() {
        return joiningRequirePermission;
    }


    /**
     * Set the group's joiningRequirePermission status
     *
     * @return group's joiningRequirePermission
     */
    public void setJoiningRequirePermission(boolean newStatus) {
        joiningRequirePermission = newStatus;
    }

    /**
     * This is a helper function for UserModel's setJoiningRequirePermission function
     *
     * @param newCondition the new condition for the group
     */
    public void HELPER_setJoiningRequirePermission(boolean newCondition) {
        this.joiningRequirePermission = newCondition;
    }

    /**
     * Return the group's set of admins
     *
     * @return set of admins
     */
    public Set<UserModel> getAdmins() {
        return this.admins;
    }

    /**
     * Return the group's set of mods
     *
     * @return set of mods
     */
    public Set<UserModel> getMods() {
        return this.mods;
    }

    /**
     * Return the group's set of members
     *
     * @return set of members
     */
    public Set<UserModel> getMembers() {
        return this.members;
    }

    /**
     * Return the group's set of join requests
     *
     * @return set of join requests
     */
    public Set<UserModel> getJoinRequestQueue() {
        return this.joinRequestQueue;
    }


    /**
     * Return the group's set of banned users
     *
     * @return set of banned users
     */
    public Set<UserModel> getBannedUsers() {
        return this.bannedUsers;
    }

    /**
     * Get the group's description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the group's description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

//    /**
//     * Return the group's set of users who blocked this group
//     * @return set of users who blocked this group
//     */
//    public Set<UserModel> getUserBlocked() {
//        return this.userBlocked;
//    }


    //  ** Notes **
    //Only admin can give or remove admin/mod/member role
    //All admin/mod/member related modifying functions are public so that they can only be accessed by the admins (in the UserModel class)
    //Because of this, when creating a new group me must pass in a UserModel object so that there's someone with access to all the public function.


//*-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*
//UserModel's Functions - UserModel's Functions - UserModel's Functions - UserModel's Functions - UserModel's Functions - UserModel's Functions - UserModel's Functions
//*-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*   *-*-*-*-*-*-*-*-*-*-*

    /**
     * Check if the user is in the group
     * Return 0 if member, 1 if mod, 2 if admin, and -1 if not in the group
     * @param user the user that's being checked
     * @return -1, 0, 1, or 2
     */
    public int HELPER_inGroup(UserModel user) {
        if(admins.contains(user))
            return GroupModel.ADMIN;
        if(mods.contains(user))
            return GroupModel.MOD;
        if(members.contains(user))
            return GroupModel.MEMBER;
        if(bannedUsers.contains(user))
            return GroupModel.BANNED;
        if(userBlocked.contains(user))
            return GroupModel.USER_BLOCKED;
        return GroupModel.NON_MEMBER;
    }

//-----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------
//Admin's functions - Admin's functions - Admin's functions - Admin's functions - Admin's functions - Admin's functions - Admin's functions - Admin's functions - Admin's functions
//-----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------   -----------------




    /**
     * Check if the user is in the set admins
     *
     * @param user the person that the function is checking
     * @return if the user is in the set admins or not
     */
    public boolean HELPER_checkAdmin(UserModel user) {
        return admins.contains(user);
    }


    /**
     * Note: Give admin Permission of a user (this function cannot be called independently - public, can only be called inside user.addAdminPermission function)
     * This function adds a user to the set admins
     *
     * @param user person to be added
     * @return if the person was successfully added or not
     */
    public boolean HELPER_giveAdminRole(UserModel user) {

        if(!admins.contains(user)) {
            admins.add(user);
            return true;
        }

        // If they aren't then return false
        return false;
    }


    /**
     * Note: Remove admin Permission of a user (this function cannot be called independently - public, can only be called inside user.removeAdminPermission function)
     * This function removes a user from the set admins
     *
     * @param user person to be removed
     * @return if the removal process was successful or not
     */
    public boolean HELPER_removeAdminRole(UserModel user) {
        // Check if the user has admin permission
        // if they do then remove them from the admins list and move them to member list
        if (admins.contains(user)) {
            admins.remove(user);
            return true;
        }

        // if they don't then return false
        return false;
    }



//---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------
//Mod's functions - Mod's functions - Mod's functions - Mod's functions - Mod's functions - Mod's functions - Mod's functions - Mod's functions - Mod's functions - Mod's functions
//---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------   ---------------


    /**
     * Check if the user is in the set mods
     *
     * @param user the person that the function is checking
     * @return if the user is in the set mods or not
     */
    public boolean HELPER_checkMod(UserModel user) {
        return mods.contains(user);
    }


    /**
     * Note: Give admin Permission of a user (this function cannot be called independently - public, can only be called inside user.addAdminPermission function)
     * This function adds a user to the set admins
     *
     * @param user person to be added
     * @return if the person was successfully added or not
     */
    public boolean HELPER_giveModRole(UserModel user) {

        if(!mods.contains(user)) {
            mods.add(user);
            return true;
        }

        // If they aren't then return false
        return false;
    }


    public boolean HELPER_removeModRole(UserModel user) {


        // Check if the user has admin permission
        // if they do then remove them from the admins list and move them to member list
        if (mods.contains(user)) {
            mods.remove(user);
            return true;
        }

        // if they don't then return false
        return false;
    }



//------------------   ------------------   ------------------   ------------------   ------------------   ------------------   ------------------   ------------------   ------------------
//Member's Functions - Member's Functions - Member's Functions - Member's Functions - Member's Functions - Member's Functions - Member's Functions - Member's Functions - Member's Functions
//------------------   ------------------   ------------------   ------------------   ------------------   ------------------   ------------------   ------------------   ------------------

    /**
     * Check if the user is in the set members
     *
     * @param user the person that the function is checking
     * @return if the user is in the set members
     */
    public boolean HELPER_checkMember(UserModel user) {
        return members.contains(user);
    }

    /**
     * Adding a user to the set of members. This function is only called if the group doesn't require permission to join or when admin/mod approving a join request.
     *
     * @param user person to be added
     * @return whether the person was added or not
     */
    public boolean HELPER_giveMemberRole(UserModel user) {

        // If already an admin/mod/member return false
        if(!members.contains(user)){
            members.add(user);
            return true;
        }
        return false;

    }

    /**
     * Removing a user's member role
     * @param user the user that is getting removed from the group
     * @return whether the removal was successful or not
     */
    public boolean HELPER_removeMemberRole(UserModel user) {

        if(members.contains(user)){
            members.remove(user);
            return true;
        }
        return false;
    }


//----------------------------   ----------------------------   ----------------------------   ----------------------------   ----------------------------   ----------------------------
//JoinRequestQueue's Functions - JoinRequestQueue's Functions - JoinRequestQueue's Functions - JoinRequestQueue's Functions - JoinRequestQueue's Functions - JoinRequestQueue's Functions
//----------------------------   ----------------------------   ----------------------------   ----------------------------   ----------------------------   ----------------------------


    /**
     * Check if the user is in the join request queue
     *
     * @param user the person that the function is checking
     * @return if the user is in the join request queue or not
     */
    public boolean HELPER_checkJoinRequestQueue(UserModel user) {
        return joinRequestQueue.contains(user);
    }

    /**
     * Adding a user on to the join request queue
     *
     * @param user the person that wants to join the group
     * @return whether adding onto the queue was successful or not
     */
    public boolean HELPER_addJoinRequestQueue(UserModel user) {

        if (joinRequestQueue.contains(user)) {
            return false;
        }

        members.add((user));
        return true;
    }

    /**
     * Removing a user out of the join request queue
     *
     * @param user the person to be removed
     * @return whether the removal was successful or not
     */
    public boolean HELPER_removeJoinRequestQueue(UserModel user) {

        if (joinRequestQueue.contains(user)){
            joinRequestQueue.remove(user);
            return true;
        }
        return false;
    }



//----------------------   ----------------------   ----------------------   ----------------------   ----------------------   ----------------------   ----------------------   ----------------------
//BannedUser's Functions - BannedUser's Functions - BannedUser's Functions - BannedUser's Functions - BannedUser's Functions - BannedUser's Functions - BannedUser's Functions - BannedUser's Functions
//----------------------   ----------------------   ----------------------   ----------------------   ----------------------   ----------------------   ----------------------   ----------------------


    /**
     * Check if the user is in banned by the group
     *
     * @param user the person that the function is checking
     * @return if the user is banned by the group or not
     */
    public boolean HELPER_checkBannedUser(UserModel user) {
        return bannedUsers.contains(user);
    }

    /**
     * Adding a user to the banned list
     *
     * @param user the person that is getting banned
     * @return whether banning that user was successful or not
     */
    public boolean HELPER_addBannedUser(UserModel user) {

        if (bannedUsers.contains(user)) {
            return false;
        }

        bannedUsers.add((user));
        return true;
    }

    /**
     * Removing a user from the banned list
     *
     * @param user the person that is getting their ban removed
     * @return whether unbanning that user was successful or not
     */
    public boolean HELPER_removeBannedUser(UserModel user) {
        if (!bannedUsers.contains(user)) {
            return false;
        }
        bannedUsers.remove((user));
        return true;
    }


//-----------------------   -----------------------   -----------------------   -----------------------   -----------------------   -----------------------   -----------------------   -----------------------
//UserBlocked's Functions - UserBlocked's Functions - UserBlocked's Functions - UserBlocked's Functions - UserBlocked's Functions - UserBlocked's Functions - UserBlocked's Functions - UserBlocked's Functions
//-----------------------   -----------------------   -----------------------   -----------------------   -----------------------   -----------------------   -----------------------   -----------------------


    /**
     * Check if the user blocked the group
     *
     * @param user the person that the function is checking
     * @return if the user blocked the group or not
     */
    public boolean HELPER_checkUserBlocked(UserModel user) {
        return userBlocked.contains(user);
    }

    /**
     * Adding a user to the set of users that blocked the group
     *
     * @param user the person that wants to block the group
     * @return whether adding the user onto the set was successful or not
     */
    public boolean HELPER_addUserBlocked(UserModel user) {

        if (userBlocked.contains(user)) {
            return false;
        }

        userBlocked.add((user));
        return true;
    }

    /**
     * Removing a user from the list of user that blocked the group
     *
     * @param user the person that stopped blocking the group
     * @return whether removing the person from the list was successful or not
     */
    public boolean HELPER_removeUserBlocked(UserModel user) {
        if (!userBlocked.contains(user)) {
            return false;
        }

        userBlocked.remove((user));
        return true;
    }




    @OneToMany(mappedBy = "inGroup")
    @JsonIgnore
    private Set<PostModel> posts = new HashSet<PostModel>();


    public Set<PostModel> getPosts(){
        return posts;
    }

}
