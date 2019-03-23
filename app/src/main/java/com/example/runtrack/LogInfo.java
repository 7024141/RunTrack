package com.example.runtrack;

public class LogInfo {
    private int id;
    private int timeSec;
    private String strDate;
    private String picPath;
    private double distance;
    private double speed;

    public LogInfo(int id, int timeSec,
                    String strDate,  String picPath, double distance){
        this.id = id; this.strDate = strDate;
        this.timeSec = timeSec; this.picPath = picPath;
        this.distance = distance;
        if(distance != 0) this.speed = (((double) timeSec)/60.0)/(distance/1000.0);
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
