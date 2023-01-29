package com.espressif.esptouch.android;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnalysisView extends AppCompatActivity {

    private BarChart barChart;
    GetDayData task = null;
    GetWeekData task2 = null;
    String id = MainActivity3.id;

    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis_view);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        RadioButton radioButton = findViewById(R.id.radioButton1);
        radioButton.setChecked(true);

        task = new GetDayData();
        task.execute("https://h1wr3zuvm7.execute-api.ap-northeast-1.amazonaws.com/default/GetTodayData", id);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton1:
                        task = new GetDayData();
                        task.execute("https://h1wr3zuvm7.execute-api.ap-northeast-1.amazonaws.com/default/GetTodayData", id);
                        break;
                    case R.id.radioButton2:
                        task2 = new GetWeekData();
                        task2.execute("https://34f5zf1gbf.execute-api.ap-northeast-1.amazonaws.com/default/GetWeekData", id);
                        break;
                }
            }
        });


    }




    private class GetDayData extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AnalysisView.this,
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... progress) { //4
            super.onProgressUpdate(progress);



            ArrayList<BarEntry> entry_chart = new ArrayList<>(); // 데이터를 담을 Arraylist

            barChart = (BarChart) findViewById(R.id.chart);




            BarData barData = new BarData(); // 차트에 담길 데이터


            float hour0 = 0;
            float hour1 = 0;
            float hour2 = 0;
            float hour3 = 0;
            float hour4 = 0;
            float hour5 = 0;
            float hour6 = 0;
            float hour7 = 0;
            float hour8 = 0;
            float hour9 = 0;
            float hour10 = 0;
            float hour11 = 0;
            float hour12 = 0;
            float hour13 = 0;
            float hour14 = 0;
            float hour15 = 0;
            float hour16 = 0;
            float hour17 = 0;
            float hour18 = 0;
            float hour19 = 0;
            float hour20 = 0;
            float hour21 = 0;
            float hour22 = 0;
            float hour23 = 0;




            try{
                JSONObject jsonObject = new JSONObject(progress[0]);
                int count = Integer.parseInt(jsonObject.getString("Count"));
                String str = jsonObject.getString("Items");
                JSONArray jsonArray = new JSONArray(str);

                for(int i = 0; i < jsonArray.length() - 1; i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    str = jsonObject.getString("payload");
                    long DATE = Long.parseLong(jsonObject.getString("DATE"));
                    Timestamp timestamp = new Timestamp(DATE);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.KOREA);
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    String time2 = sdf.format(timestamp);
                    String time = time2.substring(11,13);
                    String nowTime = sdf.format(System.currentTimeMillis());
                    String nowTime2 = nowTime.substring(11,13);
                    jsonObject = new JSONObject(str);
                    String wh = jsonObject.getString("wh");

                    if (time.equals("00")) {
                        hour0 += Float.parseFloat(wh);
                    }
                    else if (time.equals("01")) {
                        hour1 += Float.parseFloat(wh);
                    }
                    else if (time.equals("02")){
                        hour2 += Float.parseFloat(wh);
                    }
                    else if (time.equals("03")){
                        hour3 += Float.parseFloat(wh);
                    }
                    else if (time.equals("04")){
                        hour4 += Float.parseFloat(wh);
                    }
                    else if (time.equals("05")){
                        hour5 += Float.parseFloat(wh);
                    }
                    else if (time.equals("06")){
                        hour6 += Float.parseFloat(wh);
                    }
                    else if (time.equals("07")){
                        hour7 += Float.parseFloat(wh);
                    }
                    else if (time.equals("08")){
                        hour8 += Float.parseFloat(wh);
                    }
                    else if (time.equals("09")){
                        hour9 += Float.parseFloat(wh);
                    }
                    else if (time.equals("10")){
                        hour10 += Float.parseFloat(wh);
                    }
                    else if (time.equals("11")){
                        hour11 += Float.parseFloat(wh);
                    }
                    else if (time.equals("12")){
                        hour12 += Float.parseFloat(wh);
                    }
                    else if (time.equals("13")){
                        hour13 += Float.parseFloat(wh);
                    }
                    else if (time.equals("14")){
                        hour14 += Float.parseFloat(wh);
                    }
                    else if (time.equals("15")){
                        hour15 += Float.parseFloat(wh);
                    }
                    else if (time.equals("16")){
                        hour16 += Float.parseFloat(wh);
                    }
                    else if (time.equals("17")){
                        hour17 += Float.parseFloat(wh);
                    }
                    else if (time.equals("18")){
                        hour18 += Float.parseFloat(wh);
                    }
                    else if (time.equals("19")){
                        hour19 += Float.parseFloat(wh);
                    }
                    else if (time.equals("20")){
                        hour20 += Float.parseFloat(wh);
                    }
                    else if (time.equals("21")){
                        hour21 += Float.parseFloat(wh);
                    }
                    else if (time.equals("22")){
                        hour22 += Float.parseFloat(wh);
                    }
                    else if (time.equals("23")){
                        hour23 += Float.parseFloat(wh);
                    }

                }



                entry_chart.add(new BarEntry(0, hour0)); //entry_chart1에 좌표 데이터를 담는다.
                entry_chart.add(new BarEntry(1, hour1));
                entry_chart.add(new BarEntry(2, hour2));
                entry_chart.add(new BarEntry(3, hour3));
                entry_chart.add(new BarEntry(4, hour4));
                entry_chart.add(new BarEntry(5, hour5));
                entry_chart.add(new BarEntry(6, hour6));
                entry_chart.add(new BarEntry(7, hour7));
                entry_chart.add(new BarEntry(8, hour8));
                entry_chart.add(new BarEntry(9, hour9));
                entry_chart.add(new BarEntry(10, hour10));
                entry_chart.add(new BarEntry(11, hour11));
                entry_chart.add(new BarEntry(12, hour12));
                entry_chart.add(new BarEntry(13, hour13));
                entry_chart.add(new BarEntry(14, hour14));
                entry_chart.add(new BarEntry(15, hour15));
                entry_chart.add(new BarEntry(16, hour16));
                entry_chart.add(new BarEntry(17, hour17));
                entry_chart.add(new BarEntry(18, hour18));
                entry_chart.add(new BarEntry(19, hour19));
                entry_chart.add(new BarEntry(20, hour20));
                entry_chart.add(new BarEntry(21, hour21));
                entry_chart.add(new BarEntry(22, hour22));
                entry_chart.add(new BarEntry(23, hour23));



                BarDataSet barDataSet = new BarDataSet(entry_chart, "W/h \t 오늘중 몇시에 사용하였는지"); // 데이터가 담긴 Arraylist 를 BarDataSet 으로 변환한다.

                barDataSet.setColor(Color.RED); // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.

                barDataSet.setValueTextSize(10);


                barData.addDataSet(barDataSet); // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.


                barChart.setData(barData); // 차트에 위의 DataSet 을 넣는다.
                barChart.invalidate(); // 차트 업데이트
                barChart.setTouchEnabled(true); // 차트 터치 불가능하게


                //여기부터
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
                xAxis.setTextSize(15);

                final String[] labels = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(false);

                barChart.getDescription().setEnabled(false);
                //여기까지








            }catch (JSONException e){}
        }


        @Override
        protected String doInBackground(String... params) {

            String name = params[1];
            int name2 = Integer.parseInt(name);
            String serverURL = params[0];
            String resStr = null;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", name2);
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

            return resStr;
        }


    }


    private class GetWeekData extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AnalysisView.this,
                    "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... progress) { //4
            super.onProgressUpdate(progress);



            ArrayList<BarEntry> entry_chart = new ArrayList<>(); // 데이터를 담을 Arraylist
            ArrayList<String> labelList = new ArrayList<>();

            barChart = (BarChart) findViewById(R.id.chart);

            BarData barData = new BarData(); // 차트에 담길 데이터


            float today = 0;
            float todayMinus1 = 0;
            float todayMinus2 = 0;
            float todayMinus3 = 0;
            float todayMinus4 = 0;
            float todayMinus5 = 0;
            float todayMinus6 = 0;



            try{
                JSONObject jsonObject = new JSONObject(progress[0]);
                int count = Integer.parseInt(jsonObject.getString("Count"));
                String str = jsonObject.getString("Items");
                JSONArray jsonArray = new JSONArray(str);

//                Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
//                String nowTimeStr = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(nowTimestamp);
//                String nowDay = nowTimeStr.substring(8,10);

                Date date = new Date();
                long time3 = date.getTime();
                Timestamp ts = new Timestamp(time3);
                Calendar cal = Calendar.getInstance();
                cal.setTime(ts);
                ts.setTime(cal.getTime().getTime());
                String NowDay = new SimpleDateFormat("dd").format(ts);

                cal.add(Calendar.DATE, -1);
                ts.setTime(cal.getTime().getTime());
                String NowDaySub1 = new SimpleDateFormat("dd").format(ts);

                cal.add(Calendar.DATE, -1);
                ts.setTime(cal.getTime().getTime());
                String NowDaySub2 = new SimpleDateFormat("dd").format(ts);

                cal.add(Calendar.DATE, -1);
                ts.setTime(cal.getTime().getTime());
                String NowDaySub3 = new SimpleDateFormat("dd").format(ts);

                cal.add(Calendar.DATE, -1);
                ts.setTime(cal.getTime().getTime());
                String NowDaySub4 = new SimpleDateFormat("dd").format(ts);

                cal.add(Calendar.DATE, -1);
                ts.setTime(cal.getTime().getTime());
                String NowDaySub5 = new SimpleDateFormat("dd").format(ts);

                cal.add(Calendar.DATE, -1);
                ts.setTime(cal.getTime().getTime());
                String NowDaySub6 = new SimpleDateFormat("dd").format(ts);


                labelList.add(NowDaySub6);
                labelList.add(NowDaySub5);
                labelList.add(NowDaySub4);
                labelList.add(NowDaySub3);
                labelList.add(NowDaySub2);
                labelList.add(NowDaySub1);
                labelList.add(NowDay);








                for(int i = 0; i < jsonArray.length() - 1; i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    str = jsonObject.getString("payload");
                    long DATE = Long.parseLong(jsonObject.getString("DATE"));


                    Timestamp timestamp = new Timestamp(DATE);

                    String time2 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(timestamp);
                    String time = time2.substring(8,10);

                    jsonObject = new JSONObject(str);
                    String wh = jsonObject.getString("wh");


                    if (time.equals(NowDay)) {
                        today += Float.parseFloat(wh);
                    }
                    else if (time.equals(NowDaySub1)){
                        todayMinus1 += Float.parseFloat(wh);
                    }
                    else if (time.equals(NowDaySub2)){
                        todayMinus2 += Float.parseFloat(wh);
                    }
                    else if (time.equals(NowDaySub3)){
                        todayMinus3 += Float.parseFloat(wh);
                    }
                    else if (time.equals(NowDaySub4)){
                        todayMinus4 += Float.parseFloat(wh);
                    }
                    else if (time.equals(NowDaySub5)){
                        todayMinus5 += Float.parseFloat(wh);
                    }
                    else if (time.equals(NowDaySub6)){
                        todayMinus6 += Float.parseFloat(wh);
                    }


                }


                entry_chart.add(new BarEntry(6, today)); //entry_chart1에 좌표 데이터를 담는다.
                entry_chart.add(new BarEntry(5, todayMinus1));
                entry_chart.add(new BarEntry(4, todayMinus2));
                entry_chart.add(new BarEntry(3, todayMinus3));
                entry_chart.add(new BarEntry(2, todayMinus4));
                entry_chart.add(new BarEntry(1, todayMinus5));
                entry_chart.add(new BarEntry(0, todayMinus6));





                BarDataSet barDataSet = new BarDataSet(entry_chart, "W/h"); // 데이터가 담긴 Arraylist 를 BarDataSet 으로 변환한다.

                barDataSet.setValueTextSize(30);

                barDataSet.setColor(Color.RED); // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.


                barData.addDataSet(barDataSet); // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.

                barChart.setData(barData); // 차트에 위의 DataSet 을 넣는다.


                barChart.invalidate(); // 차트 업데이트
                barChart.setTouchEnabled(false); // 차트 터치 불가능하게

                barChart.zoom(0,0,0,0);



                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
                xAxis.setTextSize(16);

                final String[] labels = new String[] {"6일전", "5일전", "4일전", "3일전", "2일전", "1일전", "오늘"};
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(true);

                barChart.getDescription().setEnabled(false);





            }catch (JSONException e){}
        }


        @Override
        protected String doInBackground(String... params) {

            String name = params[1];
            int name2 = Integer.parseInt(name);
            String serverURL = params[0];
            String resStr = null;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ID", name2);
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

            return resStr;
        }


    }




}
