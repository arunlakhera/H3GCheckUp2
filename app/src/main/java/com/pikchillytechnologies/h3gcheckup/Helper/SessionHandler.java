package com.pikchillytechnologies.h3gcheckup.Helper;


import android.content.Context;
import android.content.SharedPreferences;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERPHONE = "userphone";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param userphone
     */
    public void loginUser(String userphone) {
        mEditor.putString(KEY_USERPHONE, userphone);
        mEditor.commit();
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.putString(KEY_USERPHONE, "");
        mEditor.clear();
        mEditor.apply();
        mEditor.commit();
    }

}