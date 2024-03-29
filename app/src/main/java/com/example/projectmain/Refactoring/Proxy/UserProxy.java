package com.example.projectmain.Refactoring.Proxy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.projectmain.Model.Post;
import com.example.projectmain.Model.User;
import com.example.projectmain.Refactoring.Singleton.GlobalUser;

import java.util.ArrayList;

public class UserProxy implements IUserManager {
    Context context;
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_DESCRIPTION = "description";

    private static final String KEY_IMAGE_LINK = "linkImage";
    private static final String KEY_NAME = "name";
    public static final String SHARED_PREF_NAME = "mypref";
    private UserManager realUserManager;
    public UserProxy(UserManager editor, Context context){
        realUserManager = editor;
        this.context = context;
    }
    public boolean checkAccess(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences == null){
            return false;
        }
        User user = GlobalUser.getInstance(context).getUser();
        if(user == null){
            return false;
        }
        String name = sharedPreferences.getString(KEY_NAME, null);
        String desc = sharedPreferences.getString(KEY_DESCRIPTION, null);
        Log.d("name", "checkAccess: " + desc);
        return user.getId() == realUserManager.getUser().getId() && user.getName().equals(name);
    }

    @Override
    public ArrayList<Post> getOwnPosts() {
        if(checkAccess()){
            return realUserManager.getOwnPosts();
        }
        return new ArrayList<>();
    }

    @Override
    public void postEditedInfo(User newInfo, String password, String confirmPass) {
        if(checkAccess()){
            realUserManager.postEditedInfo(newInfo, password, confirmPass);
        }
    }

    @Override
    public User getUser() {
        if(checkAccess()){
            return realUserManager.getUser();
        }
        return new User();
    }
}
