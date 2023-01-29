package com.espressif.esptouch.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlarmSetting extends AppCompatActivity {       // 3. 시간 안바꾸면 null, null로 뜸

    String id = MainActivity3.id;

    String hourOfString, minOfString;
    String alarmRecord = "";
    String time = "";
    String checkOnOff = "";
    String outlet = MainActivity3.outlet;
    ArrayList<AlarmData> alarmList = new ArrayList<>();
    ProgressDialog progressDialog;
    CheckBox mon;
    CheckBox tue;
    CheckBox wed;
    CheckBox thu;
    CheckBox fri;
    CheckBox sat;
    CheckBox sun;
    TimePicker timePicker;
    RadioButton on;
    RadioButton off;
    Button save;
    boolean checker = true;
    boolean onOffChanger = false;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting);
        timePicker = findViewById(R.id.timePicker);

        checker = true;

        on = findViewById(R.id.onCheckAlarm);
        off = findViewById(R.id.offCheckAlarm);
        on.setChecked(true);

        mon = findViewById(R.id.cb_mon);
        tue = findViewById(R.id.cb_tue);
        wed = findViewById(R.id.cb_wed);
        thu = findViewById(R.id.cb_thu);
        fri = findViewById(R.id.cb_fri);
        sat = findViewById(R.id.cb_sat);
        sun = findViewById(R.id.cb_sun);

        save = findViewById(R.id.save);

        Button delete = findViewById(R.id.delete);

        Intent intent = getIntent();
        alarmList = (ArrayList<AlarmData>) intent.getSerializableExtra("alarmList");
        AlarmData alarmData = (AlarmData) intent.getSerializableExtra("alarmData");

        if(alarmData != null){
            String time = alarmData.getTime();
            String day = alarmData.getDay();
            String action = alarmData.getAction();
            onOffChanger = true;

            dayChecker(day);
            String[] timeArr = time.split(":");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(Integer.parseInt(timeArr[0]));
                timePicker.setMinute(Integer.parseInt(timeArr[1]));
            }

            if(action.equals("on")){
                on.setChecked(true);
            }else{
                off.setChecked(true);
            }

            delete.setText("삭제");
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AlarmView.class);
                    Log.e("=============", "삭제 함수 넣기");
                    InsertData insertData = new InsertData();
                    insertData.execute("https://rd162u1fma.execute-api.ap-northeast-1.amazonaws.com/default/deleteAlarm", alarmData.getTimestamp(), alarmData.getOutlet(), id, time, day, action);
                    startActivity(intent);
                    finish();
                }
            });
            checker = false;
        }
        else{
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AlarmView.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (on.isChecked()) {
                    checkOnOff = "켜짐예약";
                } else if (off.isChecked()) {
                    checkOnOff = "꺼짐예약";
                }
                if(hourOfString == null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // 이거 맞는지 잘 모름
                        hourOfString = String.valueOf(timePicker.getHour());
                        minOfString = String.valueOf(timePicker.getMinute());
                    }
                }

                int minute = Integer.parseInt(minOfString);
                if(minute < 10)
                    minOfString = "0" + String.valueOf(minute);
                else
                    minOfString = String.valueOf(minute);

                alarmRecord = hourOfString + ":" + minOfString + " " + sendCheck(mon, tue, wed, thu, fri, sat, sun) + checkOnOff;
                Log.e("==========alarm=", alarmRecord);
                String onOff;
                if(checkOnOff.equals("켜짐예약"))
                    onOff = "on";
                else
                    onOff = "off";
                String day = sendCheck(mon, tue, wed, thu, fri, sat, sun).replaceAll("\\(|\\)","");

                if(mon.isChecked() || tue.isChecked() || wed.isChecked() || thu.isChecked() || fri.isChecked() || sat.isChecked() || sun.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), AlarmView.class);
                    intent.putExtra("알람시간", alarmRecord);
                    time = (hourOfString + ":" + minOfString);
                    InsertData insertData = new InsertData();
                    // 디바이스 ID = 123 ========================================================================

                    if(alarmExist(outlet, time, day)){
                        Toast.makeText(getApplicationContext(), "중복되는 알람이 존재합니다. 확인해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String strStamp = "";
                        if(checker){
                            Long nowDate = System.currentTimeMillis();
                            Timestamp timestamp1 = new Timestamp(nowDate);
                            strStamp = String.valueOf(timestamp1.getTime());
                        }else{
                            strStamp = alarmData.getTimestamp();
                        }

                        insertData.execute("https://frdit18ba7.execute-api.ap-northeast-1.amazonaws.com/default/addAlarm", strStamp, outlet, id, time, day, onOff);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                hourOfString = String.valueOf(hour);
                if(minute < 10)
                    minOfString = "0" + String.valueOf(minute);
                else
                    minOfString = String.valueOf(minute);
            }
        });
    }

    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), AlarmView.class);
        startActivity(intent);
        finish();
    }

    private void dayChecker(String day) {
        String str = day.replaceAll(" ","");
        String[] split = str.split(",");

        for(int i=0; i<split.length; i++){
            if(split[i].equals("MON")){
                mon.setChecked(true);
            }else if(split[i].equals("TUE")){
                tue.setChecked(true);
            }else if(split[i].equals("WED")){
                wed.setChecked(true);
            }else if(split[i].equals("THU")){
                thu.setChecked(true);
            }else if(split[i].equals("FRI")){
                fri.setChecked(true);
            }else if(split[i].equals("SAT")){
                sat.setChecked(true);
            }else if(split[i].equals("SUN")){
                sun.setChecked(true);
            }
        }

    }

    public void onDestroy(){
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private boolean alarmExist(String outlet, String time, String day){

        if(onOffChanger) {
            return false;
        }
        for(int i=0; i<alarmList.size(); i++){
            if(alarmList.get(i).getOutlet().equals(outlet) && alarmList.get(i).getTime().equals(time) && alarmList.get(i).getDay().equals(day)){
                return true;
            }

        }
        return false;
    }



    private String sendCheck(CheckBox mon, CheckBox tue, CheckBox
            wed, CheckBox thu, CheckBox fri, CheckBox sat, CheckBox sun) {
        String dayRecord = "";

            if (mon.isChecked()) {
                dayRecord += "MON,";
            }
            if (tue.isChecked()) {
                dayRecord += "TUE,";
            }
            if (wed.isChecked()) {
                dayRecord += "WED,";
            }
            if (thu.isChecked()) {
                dayRecord += "THU,";
            }
            if (fri.isChecked()) {
                dayRecord += "FRI,";
            }
            if (sat.isChecked()) {
                dayRecord += "SAT,";
            }
            if (sun.isChecked()) {
                dayRecord += "SUN,";
            }

            String[] hArr = dayRecord.split(",");

            String result = "";

            for(int i = 0; i<hArr.length; i++) {
                if(i== hArr.length-1){
                    result += hArr[i];
                } else {
                    result += hArr[i] + ",";
                }
        }
            return "(" + result + ")";
    }

    public int getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        //1 : 일요일, 2: 월요일 ..., 7: 토요일
        return dayOfWeekNumber;
    }


    class InsertData extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AlarmSetting.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {

            String timestamp = params[1];
            String outlet = params[2];
            String id = params[3];
            String time = params[4];
            String day = params[5];
            String action = params[6];

            String serverURL = params[0];

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("Timestamp", timestamp);
                jsonObject.put("Outlet", outlet);
                jsonObject.put("ID", id);
                jsonObject.put("Time", time);
                jsonObject.put("Day", day);
                jsonObject.put("Action", action);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(serverURL)
                    .post(body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                String resStr = response.body().string();

                return resStr;
            } catch (IOException e) {
                e.printStackTrace();

                return e.toString();
            }
        }
    }


}

