package com.espressif.esptouch.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlarmView extends AppCompatActivity {

    private LinearLayout lView;
    private Button addButton2;
    private Button addBtnMe;
    Timer timer;
    Timer timer2 = timer;
    private String mJsonString;
    ArrayList<AlarmData> alarmList = new ArrayList<>();
    List<Button> btnList = new ArrayList<Button>();
    String id = MainActivity3.id;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetData getData = new GetData();
        getData.execute("https://whg3he3qo5.execute-api.ap-northeast-1.amazonaws.com/default/getAlarms", id);

        setContentView(R.layout.alram_view);

        lView = (LinearLayout) findViewById(R.id.linear2);
        Button timer_Move = findViewById(R.id.timer_move);

        addBtnMe = findViewById(R.id.addBtn2);
        Intent getIntent = getIntent();
        String alarm = getIntent.getStringExtra("알람시간");

        timer_Move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(getApplicationContext(), TimerSetting.class);
                 startActivity(intent);
            }
        });

        addBtnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmSetting.class);
                intent.putExtra("alarmList", alarmList);

                startActivity(intent);
                finish();
            }
        });



    }


    public Button addButton() {
        Button button = new Button(this);
        lView.addView(button);
        button.setHeight(100);
        button.setBackgroundResource(R.drawable.alarmbtn); // 색 바꾸기

        btnList.add(button);

        return button;
    }



    public class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AlarmView.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();


            mJsonString = result;
            alarmList = getResult(mJsonString);
            for(int i=0; i<alarmList.size(); i++){
                addButton2 = addButton();
                addButton2.setText(alarmList.get(i).toString());
                addButton2.setTextSize(30);
                addButton2.setTextColor(Color.parseColor("#FFFFFF"));
                int k = i;
                addButton2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), AlarmSetting.class);
                        intent.putExtra("alarmData", alarmList.get(k));
                        intent.putExtra("alarmList", alarmList);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String id = params[1];
            String serverURL = params[0];

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("ID", id);
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

    private ArrayList<AlarmData> getResult(String json){
        ArrayList<AlarmData> alarmList = new ArrayList<>();

        try{

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jArray = jsonObject.getJSONArray("Items");
            int size = jArray.length();
            Log.e("====== size : ", String.valueOf(size));
            for(int i=0; i< jArray.length(); i++){
                JSONObject jsonObject1 = jArray.getJSONObject(i);
                String timestamp = jsonObject1.getString("Timestamp");
                String outlet = jsonObject1.getString("Outlet");
                String day = jsonObject1.getString("Day");
                String time = jsonObject1.getString("Time");
                String action = jsonObject1.getString("Action");
                AlarmData alarmData = new AlarmData(timestamp, outlet, day, time, action);
                alarmList.add(alarmData);
            }
        } catch (JSONException e){
            Log.e("==============JSON에러","showResult : " + e);
        }




        return alarmList;
    }

}
