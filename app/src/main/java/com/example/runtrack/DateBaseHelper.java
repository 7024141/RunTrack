package com.example.runtrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DateBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "RunInfo.db";
    public static final String CREAT_INFO = "create table InfoTable ("
            +"id integer primary key autoincrement, "
            +"sec integer, "
            +"date text, "
            +"distance real, "
            +"path text)";
    private Context mContext;

    public DateBaseHelper(Context context, int version){
        super(context, DB_NAME, null,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_INFO);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
