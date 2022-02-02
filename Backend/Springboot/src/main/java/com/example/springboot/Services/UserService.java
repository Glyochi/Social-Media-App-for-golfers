package com.example.springboot.Services;

import com.example.springboot.Models.Exception.UserException.UserNameAlreadyExistException;
import com.example.springboot.Models.GroupModel;
import com.example.springboot.Models.UserModel;
import com.example.springboot.Repositories.GroupRepository;
import com.example.springboot.Repositories.UserRepository;
import org.apache.catalina.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {


    @Autowired
    UserRepository userRepository;


    @Autowired
    GroupRepository groupRepository;

    /**
     * Create a new user using the given paramter (No checking if userName already existed or not)
     * @param displayName the user's display name
     * @param userScore the user's initial score
     * @param userName the user's login/username
     * @param password ther user's password
     * @return The newly created User
     */
    public UserModel createNewUser(String displayName, int userScore, String userName, String password) {
        UserModel newUser = new UserModel();
        newUser.setDisplayName(displayName);
        newUser.setScore(userScore);
        newUser.setUserName(userName);
        newUser.setPassword(password);
        userRepository.save(newUser);
        return newUser;
    }

    /**
     * Check if the userName has already been taken
     * @param userName the userName that is being checked
     * @return true/false
     */
    public boolean checkIfUserNameExisted(String userName){
        if(userRepository.findUserModelByUserName(userName) != null)
            return true;
        return false;
    }

    /**
     * Get the user based on the given ID
     * @param userID
     * @return
     */
    public UserModel getUserByID(long userID){
        UserModel user = userRepository.findById(userID).get();

        return user;
    }

    /**
     * Get the user based on the credentials
     * @param userName
     * @param password
     * @return
     */
    public UserModel getUserByCredentials(String userName, String password){
        UserModel user = userRepository.findUserModelByUserName(userName);
        if(user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    /**
     * Change the user's current password
     * @param userID
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public boolean changeUserPassword(Long userID, String oldPassword, String newPassword){
        UserModel user = userRepository.findById(userID).get();
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Return the list of all groups the user is in
     * @param userID
     * @return
     */
    public List<GroupModel> getJoinedGroups(Long userID){
        ArrayList<GroupModel> joinedGroups = new ArrayList<>();

        UserModel user = userRepository.findById(userID).get();

        Set<GroupModel> tempMap = user.getMemberInGroup();
        for(GroupModel group : tempMap){
            joinedGroups.add(group);
        }
        tempMap = user.getModInGroup();
        for(GroupModel group : tempMap){
            joinedGroups.add(group);
        }
        tempMap = user.getAdminInGroup();
        for(GroupModel group : tempMap){
            joinedGroups.add(group);
        }
        return joinedGroups;
    }


//
//    public JSONObject convertUserToJson(UserModel user) throws JSONException {
//        JSONObject userJson = new JSONObject();
//        userJson.put("id", user.getId());
//        userJson.put("displayName", user.getDisplayName());
//        userJson.put("score", user.getScore());
//        userJson.put("userName", user.getUserName());
//        userJson.put("password", user.getPassword());
//        return userJson;
//    }

}
