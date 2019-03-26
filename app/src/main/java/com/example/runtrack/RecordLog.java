package com.example.runtrack;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordLog extends AppCompatActivity {
    private DateBaseHelper dbHelp;
    private List<LogInfo> logInfoList = new ArrayList<>();
    private LogInfoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_log);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        Button log = (Button)findViewById(R.id.Log);
        log.setVisibility(View.INVISIBLE);

        dbHelp = new DateBaseHelper(this, 1);

        getInfos();
        adapter = new LogInfoAdapter(RecordLog.this,
                                                    R.layout.listview_base, logInfoList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final LogInfo log = logInfoList.get(position);
                final SQLiteDatabase db = dbHelp.getWritableDatabase();
                AlertDialog.Builder dialog = new AlertDialog.Builder(RecordLog.this);
                dialog.setTitle("Tips");
                dialog.setMessage("Sure to delete this record?\nTrackId:"+log.getId());
                dialog.setCancelable(false);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete("InfoTable", "id == ?",
                                new String[]{String.valueOf(log.getId())});
                        db.close();
                        logInfoList.remove(log);
                        File file = new File(log.getPicPath());
                        file.delete();
                        Handler handler = new Handler();
                        handler.post(updateListView);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                return false;
            }
        });

    }

    Runnable updateListView = new Runnable() {
        @Override
        public void run() {
            logInfoList.clear();
            adapter.clear();
            readTable();
            adapter.notifyDataSetChanged();
        }
    };

    public void getInfos(){
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);
        if(type == 0){
            //读数据库
            readTable();
        }else {
            //写数据库
            int time = intent.getIntExtra("time", 0);
            String strDate = intent.getStringExtra("strDate");
            double distance = intent.getDoubleExtra("distance", 0.0);
            String picPath = intent.getStringExtra("picPath");

            insertTable(time, strDate, distance, picPath);

            //读数据库
            readTable();
        }
    }

    public void insertTable(int sec, String date, double distance, String path){
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sec", sec);
        values.put("date", date);
        values.put("distance", distance);
        values.put("path", path);
        db.insert("InfoTable", null, values);
        db.close();
    }

    public void readTable(){
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        Cursor cursor = db.query("InfoTable", null, null,
                null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int second = cursor.getInt(cursor.getColumnIndex("sec"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                double dis = cursor.getDouble(cursor.getColumnIndex("distance"));
                String path = cursor.getString(cursor.getColumnIndex("path"));

                Log.e("TrackId", String.valueOf(id));
                LogInfo log = new LogInfo(id, second, date, path, dis);
                logInfoList.add(log);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
}
