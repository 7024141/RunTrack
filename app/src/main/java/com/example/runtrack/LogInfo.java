package com.example.runtrack;

public class LogInfo {
    private int id;
    private int timeSec;         //s
    private String strDate;
    private String picPath;     //path
    private double distance;  //m
    private double speed;    //min/km

    public LogInfo(int id, int timeSec,
                    String strDate,  String picPath, double distance){
        this.id = id; this.strDate = strDate;
        this.timeSec = timeSec; this.picPath = picPath;
        this.distance = distance;
        distance = distance / 1000.0;
        if(distance > 0.01) this.speed = (((double) timeSec)/60.0)/distance;
        else this.speed = 0;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public int getId() {
        return id;
    }

    public int getTimeSec() {
        return timeSec;
    }

    public String getPicPath() {
        return picPath;
    }

    public String getStrDate() {
        return strDate;
    }
}
