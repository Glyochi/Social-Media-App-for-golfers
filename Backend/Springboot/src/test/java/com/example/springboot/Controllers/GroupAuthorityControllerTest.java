package com.example.springboot.Controllers;

import com.example.springboot.Models.Exception.GroupException.NoAdminException;
import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Services.GroupAuthorityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupAuthorityControllerTest {
    private UserModel admin, mod, target;
    GroupAuthorityService groupAuthorityService;

    @Mock
    private GroupModel group;

    @BeforeEach
    public void beforeMethod(){
        admin = new UserModel();
        mod = new UserModel();
        target = new UserModel();
        group = mock(GroupModel.class);
        admin.addGroupToAdminInGroup(group);
        groupAuthorityService = new GroupAuthorityService();
    }

    @AfterEach
    public void afterMethod(){
        target = null;
        admin = null;
    }


    @Test
    void giveAdminRole() {
        assertFalse(admin.isAdminInGroup(group));

        when(group.HELPER_inGroup(admin)).thenReturn(2);
        when(group.HELPER_inGroup(target)).thenReturn(0);
        when(group.HELPER_removeMemberRole(target)).thenReturn(true);
        when(group.HELPER_giveMemberRole(target)).thenReturn(true);

        //give the target the admin role
        groupAuthorityService.giveAdminRole(admin, group, target);

        //Test
        assertTrue(target.getAdminInGroup().contains(group));
    }

    @Test
    void removeAdminRole() throws NoAdminException {

        assertFalse(target.isAdminInGroup(group));

        when(group.HELPER_inGroup(admin)).thenReturn(2);
        when(group.HELPER_giveAdminRole(admin)).thenReturn(true);

        //give the target the admin role
        assertTrue(groupAuthorityService.giveAdminRole(admin, group, target));
        when(group.HELPER_checkAdmin(target)).thenReturn(true);

        //Checking if the test case was set up properly
        assertTrue(target.isAdminInGroup(group));

        when(group.HELPER_checkAdmin(admin)).thenReturn(true);
        when(group.HELPER_removeAdminRole(admin)).thenReturn(true);

        //remove the target admin role
        assertTrue(groupAuthorityService.removeAdminRole(admin, group, target));
        when(group.HELPER_checkAdmin(target)).thenReturn(false);

        //Test output
        assertFalse(target.isAdminInGroup(group));
    }

//
//    @Test
//    void removeModRole() {
//    }
//
//    @Test
//    void giveModRole() {
//    }
//
//    @Test
//    void removeMemberRole() {
//    }
//
//    @Test
//    void giveMemberRole() {
//    }
//
//    @Test
//    void acceptJoinRequest() {
//    }
//
//    @Test
//    void rejectJoinRquest() {
//    }

    @Test
    void bantarget() {
        when(group.HELPER_inGroup(admin)).thenReturn(2);
        when(group.HELPER_inGroup(admin)).thenReturn(0);
        when(group.HELPER_removeMemberRole(target)).thenReturn(true);
        when(group.HELPER_addBannedUser(target)).thenReturn(true);
        groupAuthorityService.banUser(admin, group, target);
        when(group.HELPER_checkBannedUser(target)).thenReturn(true);

        assertTrue(target.isBannedFromGroup(group));
    }

    @Test
    void unbantarget() {
        when(group.HELPER_inGroup(admin)).thenReturn(2);
        when(group.HELPER_inGroup(admin)).thenReturn(0);
        when(group.HELPER_removeMemberRole(target)).thenReturn(true);
        when(group.HELPER_addBannedUser(target)).thenReturn(true);
        groupAuthorityService.banUser(admin, group, target);

        when(group.HELPER_checkBannedUser(target)).thenReturn(true);
        assertTrue(target.isBannedFromGroup(group));


        when(group.HELPER_checkBannedUser(target)).thenReturn(true);
        when(group.HELPER_inGroup(admin)).thenReturn(2);
        when(group.HELPER_removeBannedUser(target)).thenReturn(true);
        groupAuthorityService.unbanUser(admin, group, target);

        when(group.HELPER_checkBannedUser(target)).thenReturn(false);
        assertFalse(target.isBannedFromGroup(group));
    }
//



}
