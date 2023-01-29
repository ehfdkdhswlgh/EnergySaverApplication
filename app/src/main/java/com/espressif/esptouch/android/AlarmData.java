package com.espressif.esptouch.android;


import java.io.Serializable;
import java.sql.Timestamp;

public class AlarmData implements Serializable {
    private String timestamp;
    private String outlet;
    private String day;
//    private int hour;
//    private int minute;
    private String time;
    private String Action;

    public AlarmData(String timestamp, String outlet, String day, String time, String action) {
        this.timestamp = timestamp;
        this.outlet = outlet;
        this.day = day;
        this.time = time;
        Action = action;
    }

    public AlarmData(String outlet, String day, String time) {
        this.outlet = outlet;
        this.day = day;
        this.time = time;
    }

    public AlarmData(String outlet, String day, String time, String action) {
        this.outlet = outlet;
        this.day = day;
        this.time = time;
        this.Action = action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    public int getMinute() {
//        return minute;
//    }
//
//    public void setMinute(int minute) {
//        this.minute = minute;
//    }
//
//    public int getHour() {
//        return hour;
//    }
//
//    public void setHour(int hour) {
//        this.hour = hour;
//    }


    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    @Override
    public String toString(){
        String onOff = "";
        if(Action.equals("on"))
            onOff = "켜짐예약";
        else if (Action.equals("off"))
            onOff = "꺼짐예약";

//        String dayOfWeek = "";
//        switch (day){
//            case "MON":
//                dayOfWeek = "월";
//                break;
//            case "TUE":
//                dayOfWeek = "화";
//                break;
//            case "WEN":
//                dayOfWeek = "수";
//                break;
//            case "THU":
//                dayOfWeek = "목";
//                break;
//            case "FRI":
//                dayOfWeek = "금";
//                break;
//            case "SAT":
//                dayOfWeek = "토";
//                break;
//            case "SUN":
//                dayOfWeek = "일";
//                break;
//
//        }

        return outlet +"구 " + time + " (" + day + ") " + onOff;
    }
}
