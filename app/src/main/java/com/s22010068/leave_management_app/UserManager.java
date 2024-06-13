package com.s22010068.leave_management_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {
    private DatabaseHelper dbHelper;

    public UserManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean registerUser(String employeeNo, String username, String password ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_COL_2, employeeNo);
        contentValues.put(DatabaseHelper.USER_COL_3, username);
        contentValues.put(DatabaseHelper.USER_COL_4, password);
        long result = db.insert(DatabaseHelper.USERS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.USERS_TABLE_NAME + " WHERE USERNAME=? AND PASSWORD=?", new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}
