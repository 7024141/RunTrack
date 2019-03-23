package com.example.runtrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.util.AttributeSet;
import android.widget.Toast;

public class TitleLayout extends LinearLayout {
    public TitleLayout(final Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_title, this);

        Button btn_Log = (Button) findViewById(R.id.Log);
        Button btn_back = (Button) findViewById(R.id.back);
        btn_Log.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecordLog.class);
                getContext().startActivity(intent);
            }
        });
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)getContext()).finish();
            }
        });
    }
}
