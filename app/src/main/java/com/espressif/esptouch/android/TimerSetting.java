package com.espressif.esptouch.android;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//시연때 일시정지후 나갔다가 들어오는거 금지.
public class TimerSetting extends AppCompatActivity {

    static RadioButton on;
    static RadioButton off;

    private Button startButton; //시작버튼
    private Button stopButton;  //정지버튼
    private Button cancelButton;//취소버튼
    private RadioGroup rg;


    private EditText hourText; //시
    private EditText minText; //분
    private EditText secondText;//초

    private TextView finish;
    private TextView hourTV;
    private TextView minTV;
    private TextView secondTV;
    private TextView onOffTV;


    static CountDownTimer countDownTimer;

    private boolean timerRunning;// 타이머 상태
    private boolean firstState; // 처음인지 아닌지?
    public static boolean isFinish = true;


    private long time = 0;
    private long tempTime = 0;


    LinearLayout timeCountSettingLV, timeCountLV;
    String sHour;
    String sMin;
    String sSecond;

    static String str = "";

    int hour;
    int minutes;
    int seconds;

    boolean onChecker = true;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_setting);
        rg = findViewById(R.id.radioGroup);
        timeCountSettingLV = (LinearLayout) findViewById(R.id.timeCountSettingLV);
        timeCountLV = (LinearLayout) findViewById(R.id.timeCountLV);

        startButton = findViewById(R.id.start_btn); //시작
        stopButton = findViewById(R.id.stop_btn); //정지
        cancelButton = findViewById(R.id.cancel_btn);//취소

        hourText = findViewById(R.id.hour);//시
        minText = findViewById(R.id.min); //분
        secondText = findViewById(R.id.second);//초


        hourTV = findViewById(R.id.hourTV);
        minTV = findViewById(R.id.minuteTV);
        secondTV = findViewById(R.id.secondTV);


        finish = findViewById(R.id.finishTV);
        /*onOffTV = findViewById(R.id.textView);*/
        on = findViewById(R.id.onCheck);
        off = findViewById(R.id.offCheck);
        onOffTV = findViewById(R.id.onoffbtn);

        firstState = true;



        if (isFinish == false) {
            rg.setVisibility(View.GONE);
            if(on.isChecked()) {
                onOffTV.setText("켜짐 예약");
//                onChecker = true;
            } else {
//                onChecker = false;
                onOffTV.setText("꺼짐 예약");
            }
            firstState = false;
            tempTime = (Long.parseLong(str.substring(0, 2)) * 3600000) + (Long.parseLong(str.substring(3, 5)) * 60000) + (Long.parseLong(str.substring(6, 8)) * 1000) + 1000;
            timeCountSettingLV.setVisibility(View.GONE);
            timeCountLV.setVisibility(View.VISIBLE);
            startButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.VISIBLE);
            countDownTimer.cancel();
            startTimer();
        }



        //타이머 시작
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg.setVisibility(View.GONE);
                if(on.isChecked()) {
                    onOffTV.setText("켜짐 예약");
                } else {
                    onOffTV.setText("꺼짐 예약");
                }


                finish.setText("");
                timeCountSettingLV.setVisibility(View.GONE);
                timeCountLV.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                firstState = true;
                isFinish = false;
                startStop();


            }
        });

        //일시정지
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStop();
            }
        });

        //취소
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg.setVisibility(View.VISIBLE);
                onOffTV.setText("");
                finish.setText("");
                firstState = true;
                timeCountLV.setVisibility(View.GONE);
                timeCountSettingLV.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                tempTime = 0;
                stopTimer();
                isFinish = true;
            }
        });

    }


    //타이머 상태에 따른 시작&정지
    private void startStop() {
        if (timerRunning) { //시작이면 정지
            stopTimer();

        } else {
            startTimer(); //정지면 시작
        }
    }

    //타이머구현
    private void startTimer() {
/*
        String sHour;
        String sMin;
        String sSecond;*/
        //처음이면 설정 타이머값을 사용한다.
        if (firstState) {

            if (hourText.length() == 0) {
                sHour = "0";
            } else {
                sHour = hourText.getText().toString();
            }

            if (minText.length() == 0) {
                sMin = "0";
            } else {
                sMin = minText.getText().toString();
            }

            if (secondText.length() == 0) {
                sSecond = "0";
            } else {
                sSecond = secondText.getText().toString();
            }

            time = (Long.parseLong(sHour) * 3600000) + (Long.parseLong(sMin) * 60000) + (Long.parseLong(sSecond) * 1000) + 1000;


        } else {
            time = tempTime;
        }


        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                tempTime = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {
                rg.setVisibility(View.VISIBLE);
                onOffTV.setText("");
                finish.setText("타이머가 종료되었습니다.");
                timeCountSettingLV.setVisibility(View.VISIBLE);
                timeCountLV.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
                startButton.setVisibility(View.VISIBLE);
                timerRunning = false;
                isFinish = true;

                InsertData insertData = new InsertData();

                String status = ""; // 1이 켜기 0이 끄기
                if(on.isChecked())
                    status = "1";
                else
                    status = "0";
                String thing = MainActivity3.id;
                String relay = MainActivity3.outlet;
                insertData.execute("https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);


                notification();

            }
        }.start();


        stopButton.setText("일시정지");
        timerRunning = true;
        firstState = false;
    }

    //타이머 정지
    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        stopButton.setText("계속");
    }

    //시간업데이트
    private void updateTimer() {

/*        int hour = (int) tempTime / 3600000;
        int minutes = (int) tempTime % 3600000 / 60000;
        int seconds = (int) tempTime % 3600000 % 60000 / 1000;*/


        hour = (int) tempTime / 3600000;
        minutes = (int) tempTime % 3600000 / 60000;
        seconds = (int) tempTime % 3600000 % 60000 / 1000;

        /* String timeLeftText = "";*/

        String hour2 = "0";
        String minutes2 = "0";
        String seconds2 = "0";

        hour2 += String.valueOf(hour);
        minutes2 += String.valueOf(minutes);
        seconds2 += String.valueOf(seconds);


        if (hour < 10) {
            hourTV.setText(hour2);
        } else {
            hourTV.setText(String.valueOf(hour));
        }
        if (minutes < 10) {
            minTV.setText(minutes2);
        } else {
            minTV.setText(String.valueOf(minutes));
        }

        if (seconds < 10) {
            secondTV.setText(seconds2);
        } else {
            secondTV.setText(String.valueOf(seconds));
        }


        str = "";

        if (hour < 10) str += "0";
        str += hour + ":";

        if (minutes < 10) str += "0";
        str += minutes + ":";

        //초가 10보다 작으면 0이 붙는다.
        if (seconds < 10) str += "0";
        str += seconds;





    }

    private void notification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String NOTIFICATION_ID = "10001";
        String NOTIFICATION_NAME = "타이머";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

//채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, IMPORTANCE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerSetting.this, NOTIFICATION_ID)
                .setContentTitle("타이머가 끝났어요!") //타이틀 TEXT
                .setContentText("동작을 시작하겠습니다.") //세부내용 TEXT
                .setSmallIcon(R.drawable.alarm_icon) //필수 (안해주면 에러)
                ;

        notificationManager.notify(0, builder.build());
    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(TimerSetting.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String status = params[1];
            String thing = params[2];
            String relay = params[3];

            String serverURL = params[0];

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("Status", status);
                jsonObject.put("Thing", thing);
                jsonObject.put("Relay", relay);
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







