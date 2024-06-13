package com.s22010068.leave_management_app;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String username) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }
}
