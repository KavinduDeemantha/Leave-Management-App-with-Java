package com.s22010068.leave_management_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "leaves.db";
    public static final String LEAVES_TABLE_NAME = "leaves_table";
    public static final String USERS_TABLE_NAME = "users_table";
    private static DatabaseHelper instance;

    // Columns for leaves_table
    public static final String COL_1 = "LEAVE_ID";
    public static final String COL_2 = "LEAVE_NO";
    public static final String COL_3 = "LEAVE_DATE";
    public static final String COL_4 = "REASON";

    // Columns for users_table
    public static final String USER_COL_1 = "ID";
    public static final String USER_COL_2 = "EMPLOYEE_NO";
    public static final String USER_COL_3 = "USERNAME";
    public static final String USER_COL_4 = "PASSWORD";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            String createLeavesTable = "CREATE TABLE " + LEAVES_TABLE_NAME + " (" +
                    COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_2 + " TEXT, " +
                    COL_3 + " TEXT," +
                    COL_4 + " TEXT)";
            db.execSQL(createLeavesTable);

            String createUsersTable = "CREATE TABLE " + USERS_TABLE_NAME + " (" +
                    USER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_COL_2 + " TEXT, " +
                    USER_COL_3 + " TEXT,"+
                    USER_COL_4 + " TEXT)";
            db.execSQL(createUsersTable);
        }catch(Exception e){
            Log.e("DatabaseHelper", "Error creating tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LEAVES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String leave_no, String leave_date, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, leave_no);
        contentValues.put(COL_3, leave_date);
        contentValues.put(COL_4, reason);

        long result = -1;
        try {
            result = db.insert(LEAVES_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting data", e);
        }

        return result != -1;
    }

    public Cursor getLeaveDetails(String leaveNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + LEAVES_TABLE_NAME + " WHERE " + COL_2 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{leaveNo});
        return cursor;
    }

    public boolean updateData(String leaveNo, String newLeaveDate, String newReason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, newLeaveDate);
        contentValues.put(COL_3, newReason);

        int result = -1;
        try {
            result = db.update(LEAVES_TABLE_NAME, contentValues, COL_2 + " = ?", new String[]{leaveNo});
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating data", e);
        }

        return result > 0;
    }

    public boolean deleteData(String leaveNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(LEAVES_TABLE_NAME, COL_2 + " = ?", new String[]{leaveNo});
        return result > 0;
    }
    // Users table methods
    public boolean insertUser(String employeeNo, String username, String password ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, employeeNo);
        contentValues.put(USER_COL_3, username);
        contentValues.put(USER_COL_4, password);

        long result = -1;
        try {
            result = db.insert(USERS_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting user", e);
        }
        return result != -1;
    }
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USER_COL_3 + " = ? AND " + USER_COL_4 + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}