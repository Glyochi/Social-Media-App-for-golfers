package com.example.springboot.Controllers;

import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Services.GroupAuthorityService;
import com.example.springboot.Services.GroupMemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupMemberControllerTest {

    private UserModel admin, mod, target;
    GroupMemberService groupMemberService;

    @Mock
    private GroupModel group;

    @BeforeEach
    public void beforeMethod(){
        admin = new UserModel();
        mod = new UserModel();
        target = new UserModel();
        group = mock(GroupModel.class);
        admin.addGroupToAdminInGroup(group);
        groupMemberService = new GroupMemberService();
    }

    @AfterEach
    public void afterMethod(){
        target = null;
        admin = null;
    }


    @Test
    void canInteractWithGroup() {
        when(group.HELPER_checkBannedUser(target)).thenReturn(false);

        //Test
        assertTrue(groupMemberService.canInteractWithGroup(target, group));

    }
    @Test
    void joinGroup_DoesntRequirePermission() {
        //For first canInteractWithGroup call
        when(group.HELPER_checkBannedUser(target)).thenReturn(false);

        when(group.getJoiningRequirePermission()).thenReturn(false);
        when(group.HELPER_giveMemberRole(target)).thenReturn(true);


        groupMemberService.joinGroup(target, group);

        when(group.HELPER_checkMember(target)).thenReturn(true);
        when(group.HELPER_checkJoinRequestQueue(target)).thenReturn(false);

        //Test
        //The target should be a member
        assertTrue(target.isMemberInGroup(group));
        //The target should not be on the join request queue
        assertFalse(target.isJoinGroupRequest(group));

    }
    @Test
    void joinGroup_DoesRequirePermission() {

        //For first canInteractWithGroup call
        when(group.HELPER_checkBannedUser(target)).thenReturn(false);

        when(group.getJoiningRequirePermission()).thenReturn(true);
        when(group.HELPER_giveMemberRole(target)).thenReturn(true);


        groupMemberService.joinGroup(target, group);

        when(group.HELPER_checkMember(target)).thenReturn(false);
        when(group.HELPER_checkJoinRequestQueue(target)).thenReturn(true);

        //Test
        //The target should not be a member
        assertFalse(target.isMemberInGroup(group));
        //The target should be on the join request queue
        assertFalse(target.isJoinGroupRequest(group));

    }

    @Test
    void blockGroup() {
        when(group.HELPER_checkUserBlocked(target)).thenReturn(false, true);
        when(group.HELPER_addUserBlocked(target)).thenReturn(true);
        assertFalse(target.isBlockedGroup(group));

        groupMemberService.blockGroup(target, group);

        //Test
        assertTrue(target.isBlockedGroup(group));
    }

    @Test
    void unblockGroup() {
        when(group.HELPER_checkUserBlocked(target)).thenReturn(false, true, false);
        when(group.HELPER_addUserBlocked(target)).thenReturn(true);
        when(group.HELPER_removeUserBlocked(target)).thenReturn(true);
        assertFalse(target.isBlockedGroup(group));

        groupMemberService.blockGroup(target, group);

        //Test
        assertTrue(target.isBlockedGroup(group));

        groupMemberService.unblockGroup(target, group);

        assertFalse(target.isBlockedGroup(group));
    }
}
