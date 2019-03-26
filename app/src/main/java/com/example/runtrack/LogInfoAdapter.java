package com.example.runtrack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class LogInfoAdapter extends ArrayAdapter<LogInfo> {
    private int resourceId;

    public LogInfoAdapter(Context context, int textViewResourceId, List<LogInfo> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogInfo info = getItem(position);
        View view;
        ViewHolder viewHolder;

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) view.findViewById(R.id.trackPic);
            viewHolder.date = (TextView) view.findViewById(R.id.tvdate);
            viewHolder.time = (TextView) view.findViewById(R.id.tvtime);
            viewHolder.dis = (TextView) view.findViewById(R.id.tvdis);
            viewHolder.speed = (TextView) view.findViewById(R.id.tvspeed);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        Timer timer = new Timer();
        DecimalFormat df = new DecimalFormat("00.00");

        try {
            FileInputStream f = new FileInputStream(info.getPicPath());
            Bitmap bm = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;//图片的长宽都是原来的1/3
            BufferedInputStream bis = new BufferedInputStream(f);
            bm = BitmapFactory.decodeStream(bis, null, options);
            viewHolder.img.setImageBitmap(bm);
            viewHolder.date.setText(info.getStrDate());
            viewHolder.time.setText(timer.secToTime(info.getTimeSec()));
            viewHolder.dis.setText(String.valueOf(df.format(info.getDistance()/1000.0)));
            viewHolder.speed.setText(String.valueOf(df.format(info.getSpeed())));
        }catch (IOException e){}

        return view;
    }

    class ViewHolder{
        TextView date;
        TextView time;
        TextView dis;
        TextView speed;
        ImageView img;
    }
}
