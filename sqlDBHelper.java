package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TableLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
public class sqlDBHelper extends SQLiteOpenHelper {
    private static final String DataBaseName = "DBAPPDIARY";
    private static final int DataBaseVersion = 1;
    protected String TableName = "DiaryRecord";

    public sqlDBHelper(Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("create", "success");
        try {
            Log.e("create", "success");
            sqLiteDatabase.execSQL("create Table DiaryRecord (date Text primary key, weather Text, mood Text, diary Text)");

        } catch (SQLException e) {
            Log.e("Create fail", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists DiaryRecord");
        onCreate(sqLiteDatabase);
    }


    //檢查資料表狀態，若無指定資料表則新增
    public void checkTable() {
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        Log.e("tablename", TableName);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                //Log.w("true create", "mooo");
                try {
                    Log.w("true create", "mooo");
                    getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + " ( " +
                            "date INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "weather text not null, " +
                            "nood TEXT not null, " +
                            "diary TEXT not null);");
                    //Log.w("true create", "mooo");
                } catch (SQLException e) {
                    Log.e("error create", e.getMessage());
                }
            }
            cursor.close();
        }
    }

    //新增資料
    public boolean addData(String date, String weather, String mood, String diary) {
        Log.i("Date", date);
        Log.i("Weather", weather);
        Log.i("Mood", mood);
        Log.i("diary", diary);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("weather", weather);
        values.put("mood", mood);
        values.put("diary", diary);
        long flag = db.replace(TableName,null, values);
        return (flag == -1) ? false : true;
    }

    //以date搜尋特定資料
    public HashMap<String, Object> searchByDate(String getDate) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE date =" + "'" + getDate + "'", null);
        if (c.getCount() == 0) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        c.moveToFirst();
        String date = c.getString(0);
        String weather = c.getString(1);
        String mood = c.getString(2);
        String diary = c.getString(3);

        hashMap.put("date", date);
        hashMap.put("weather", weather);
        hashMap.put("mood", mood);
        hashMap.put("diary", diary);
        return hashMap;
    }

    //修改資料
    public void modify(String date, String weather, String mood, String diary) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("weather", weather);
        values.put("mood", mood);
        values.put("diary", diary);
        db.update(TableName, values, "date = " + date, null);
    }

    //刪除全部資料
    /*public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM" + TableName);
    }*/

    //以Account刪除資料
    public void deleteRecord(String date) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE date =" + "'" + date + "'", null);
        db.delete(TableName, "date = " + date, null);
        c.close();
    }
}
