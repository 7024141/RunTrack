package com.example.runtrack;

import android.os.CountDownTimer;

public class Timer{
    //是否计时
    private boolean isStart;
    //时间
    private String strTime;
    int time;

    Timer(){
        isStart = false;
        strTime = "";
        time = 0;
    }

    public boolean getStateOfTimer(){
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public int getTime() {
        return time;
    }

    public String secToTime(int nunTime) {
        time = nunTime;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                strTime = unitFormat(0) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                strTime = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return strTime;
    }

    public String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
