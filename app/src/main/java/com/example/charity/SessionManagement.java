package com.example.charity;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY_ID = "session_user";

    SessionManagement(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(User user){
        String id = user.getId();

        editor.putString(SESSION_KEY_ID,id).commit();
    }

    public String getSession(){

        return sharedPreferences.getString(SESSION_KEY_ID, "-1");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY_ID,"-1").commit();
    }
}
