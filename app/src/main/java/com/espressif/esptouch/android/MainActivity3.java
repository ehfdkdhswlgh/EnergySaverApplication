package com.espressif.esptouch.android;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.espressif.esptouch.android.esp.EspTouchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity {
    private Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    boolean on = true;
    boolean on2 = true;
    Button onOffBtn;
    int way; // 몇구인지
    boolean chk = true; // 버튼누르고 나면 한루프동안 on off 상태 안바뀜

    private TextView textView;
    private TextView tv_temperature;
    private TextView tv_humidity;
    private TextView tv_watt1;
    private TextView tv_watt2;
    private Button turnOnAll;
    private Button turnOffAll;

    GetData task = null;
    InsertData insertData = null;

    private final int maxWatt = 1000;
    public static String outlet = "";
    public static String id = "";

    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG_TAG, "---------------------------------onCreate-----------------------------");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button alarm = findViewById(R.id.alarm);

        textView = findViewById(R.id.txt);

        tv_temperature = (TextView)findViewById(R.id.tv_temperature);
        tv_humidity = (TextView)findViewById(R.id.tv_humidity);
        tv_watt1 = (TextView)findViewById(R.id.tv_watt1);
        tv_watt2 = (TextView)findViewById(R.id.tv_watt2);
        turnOnAll = findViewById(R.id.turnOnAll);
        turnOffAll = findViewById(R.id.turnOffAll);

        Intent intent = getIntent();

        id = intent.getStringExtra("deviceId");


        arrayList = new ArrayList<>();
        arrayList.add("1구");
        arrayList.add("2구");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, arrayList);

        spinner = (Spinner)findViewById(R.id.spinner2);
        spinner.setAdapter(arrayAdapter); // 이름 바꾸려면 spinner말고 다른 방식이 좋을듯?

        outlet = spinner.getSelectedItem().toString();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                way = pos + 1; // way = 1이 1구
                outlet = String.valueOf(way);
                if(way == 1){
                    if (on == true) {
                        onOffBtn.setTextColor(Color.parseColor("#ff0000"));
                        onOffBtn.setText("on");
                    } else {
                        onOffBtn.setTextColor(Color.parseColor("#ffffff"));
                        onOffBtn.setText("off");
                    }
                }else if(way == 2){
                    if (on2 == true) {
                        onOffBtn.setTextColor(Color.parseColor("#ff0000"));
                        onOffBtn.setText("on");
                    } else {
                        onOffBtn.setTextColor(Color.parseColor("#ffffff"));
                        onOffBtn.setText("off");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        textView.setText(intent.getStringExtra("위치"));


        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmView.class);
                startActivity(intent);
            }
        });

        onOffBtn = findViewById(R.id.onoffbtn);


        onOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData = new InsertData();
                String relay = String.valueOf(way);
                String thing = id;
                String status;

                if(way == 1) {

                    if (on == false) {
                        onOffBtn.setTextColor(Color.parseColor("#ff0000"));
                        onOffBtn.setText("on");
                        status = "1";
                        Executor ex = null;
                        insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
                        on = true;
                    } else {
                        onOffBtn.setTextColor(Color.parseColor("#ffffff"));
                        onOffBtn.setText("off");
                        status = "0";
                        insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
                        on = false;
                    }
                }else{

                    if (on2 == false) {
                        onOffBtn.setTextColor(Color.parseColor("#ff0000"));
                        onOffBtn.setText("on");
                        status = "1";
                        insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
                        on2 = true;
                    } else {
                        onOffBtn.setTextColor(Color.parseColor("#ffffff"));
                        onOffBtn.setText("off");
                        status = "0";
                        insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
                        on2 = false;
                    }
                }
                chk = false;
            }
        });

        turnOnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = "1";
                String thing = id;
                String relay = "1";
                insertData = new InsertData();
                insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
                insertData = new InsertData();
                relay = "2";
                insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
//                chk = false;
            }
        });

        turnOffAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = "0";
                String thing = id;
                String relay = "1";
                insertData = new InsertData();
                insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
                insertData = new InsertData();
                relay = "2";
                insertData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://de2lstuv44.execute-api.ap-northeast-1.amazonaws.com/default/Lambda_Iot_Pub", status, thing, relay);
//                chk = false;
            }
        });



        Button analysis = findViewById(R.id.analysis);

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AnalysisView.class);
                startActivity(intent);
            }
        });

        Button connetbtn = findViewById(R.id.connetbtn);

        connetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EspTouchActivity.class);
                startActivity(intent);
            }
        });



        task = new GetData();
        task.execute( "https://1s33vkqsza.execute-api.ap-northeast-1.amazonaws.com/default/latestData", id);

    }

    private void notification(int way) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String NOTIFICATION_ID = "10002";
        String NOTIFICATION_NAME = "경고알림";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

//채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, IMPORTANCE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity3.this, NOTIFICATION_ID)
                .setContentTitle("Warning") //타이틀 TEXT
                .setContentText(way + "구 전력량이 매우 높습니다.") //세부내용 TEXT
                .setSmallIcon(R.drawable.warning) //필수 (안해주면 에러)
                ;

        notificationManager.notify(0, builder.build());
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(task!=null){
            task.cancel(true);
            task = null;
        }
        Log.d(LOG_TAG, "---------------------------------onPause-----------------------------");
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(task!=null){
            task.cancel(true);
            task = null;
        }
        Log.d(LOG_TAG, "---------------------------------onStop-----------------------------");
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(task==null){
            task = new GetData();
            task.execute( "https://1s33vkqsza.execute-api.ap-northeast-1.amazonaws.com/default/latestData", id);
        }

        Log.d(LOG_TAG, "---------------------------------onResume-----------------------------");
    }





    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity3.this,
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

    private class GetData extends AsyncTask<String, String, String>{

        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... progress) { //4
            super.onProgressUpdate(progress);

            String temperature = null;
            String wh = null;
            String humidity = null;
            String watt1 = null;
            String watt2 = null;
            String relay1 = null;
            String relay2 = null;



            int watt1st;
            int watt2st;



            try {
                JSONObject jsonObject = new JSONObject(progress[0]);
                String str = jsonObject.getString("payload");
                JSONObject jsonObjectBody = new JSONObject(str);

                temperature = jsonObjectBody.getString("temperature");
                wh = jsonObjectBody.getString("wh");
                humidity = jsonObjectBody.getString("humidity");
                watt1 = jsonObjectBody.getString("watt1");
                watt2 = jsonObjectBody.getString("watt2");
                relay1 = jsonObjectBody.getString("relay1");
                relay2 = jsonObjectBody.getString("relay2");

                watt1st = Integer.parseInt(watt1);
                watt2st = Integer.parseInt(watt2);

                if(watt1st > maxWatt ) {

                    notification(1);
                } else if(watt2st > maxWatt) {

                    notification(2);
                }


            } catch (JSONException e) {

            }

            tv_temperature.setText(temperature);
            tv_humidity.setText(humidity);
            tv_watt1.setText(watt1);
            tv_watt2.setText(watt2);

            if(relay1 == null){
                Toast myToast = Toast.makeText(MainActivity3.this,"비정상적인 접근입니다." + textView.getText().toString() + "을(를) 삭제하고 다시 만들어주세요", Toast.LENGTH_SHORT);
                myToast.show();
//                onBackPressed();
            }

            if(relay1.equals("0")) {
                on = false;
            } else{
                on = true;
            }

            if(relay2.equals("0")) {
                on2 = false;
            } else{
                on2 = true;
            }

            if(chk) {
                if (spinner.getSelectedItem().toString().equals("1구")) {
                    if (on) {
                        onOffBtn.setTextColor(Color.parseColor("#ff0000"));
                        onOffBtn.setText("on");
                    } else {
                        onOffBtn.setTextColor(Color.parseColor("#ffffff"));
                        onOffBtn.setText("off");
                    }
                } else {
                    if (on2) {
                        onOffBtn.setTextColor(Color.parseColor("#ff0000"));
                        onOffBtn.setText("on");
                    } else {
                        onOffBtn.setTextColor(Color.parseColor("#ffffff"));
                        onOffBtn.setText("off");
                    }
                }
            }
            chk = true;

        }

        @Override
        protected String doInBackground(String... params) {

            String name = params[1];
//            int name2 = Integer.parseInt(name);
            String serverURL = params[0];
            String resStr = null;

            for(int i = 0; i < 10000000; i++){

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("ID", name);
//                    jsonObject.put("ID", name2);
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
                    response = client.newCall(request).execute(); //인자 서버로 보내고 json값 받아오기
                    resStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                publishProgress(resStr);



                try {
                    Thread.sleep(3000) ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isCancelled()){
                    break;
                }

            }

            return resStr;
        }
    }

}
