package com.espressif.esptouch.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private Button addButton;
    private SharedPreferences preferences;
    private int count = 0;
    private Button button;
    private LinearLayout lView;
    private List<Button> btnList = new ArrayList<>();
    private Button[] btnArr;
    private int pos= -1;
    private String deviceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = findViewById(R.id.button1);
        TextView textView = (TextView) findViewById(R.id.txtLocation);

        Intent intent = getIntent(); //

        int numFromPageOne = intent.getIntExtra("첫페이지", 0);
        textView.setText(intent.getStringExtra("위치"));
//        Log.e("=====================", numFromPageOne);
        Log.e("====================", "SecondPage" + numFromPageOne);
        preferences = getSharedPreferences("SecondPage" + numFromPageOne, MODE_PRIVATE); // 거실 등 지역별로 나눠야할듯? 앞 페이지에서 intent로 지정된 번호 받아와서 하기

        count = preferences.getInt("count",0);
        Log.e("", "=========================count : " + count);
        lView = (LinearLayout) findViewById(R.id.linearLayout);

        if(count !=0) {
            initialCall();
        }

        clickButton(button);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            deviceId = data.getStringExtra("deviceId");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("deviceId" + (count-1), deviceId);
            editor.commit();
            Log.e("============", "qr"+deviceId);

            finish();//인텐트 종료
            Intent intent = getIntent(); //인텐트
            startActivity(intent); //액티비티 열기
        }


    }



    public void clickButton(Button button){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
//        EditText input2 = new EditText(this);

        builder.setTitle("이름을 입력하시오");
        builder.setView(input);

        builder.setPositiveButton("확인",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        btnList.add(button);
                        //Log.e("========btnList size : ",Integer.toString(btnList.size()));

                        button.setText(input.getText().toString());
                        button.setTextSize(24);
                        addButton = pushButton();
                        addButton.setText("+");
                        addButton.setTextSize(50);
                        addButton.setTextColor(Color.parseColor("#FFFFFF"));

                        //Editor를 preferences에 쓰겠다고 연결
                        SharedPreferences.Editor editor = preferences.edit();
                        //putString(KEY,VALUE)
                        editor.putInt("btnNum" + (btnList.size() - 1), (btnList.size() - 1));
                        //Log.e("", ("=======================btnList.size() - 1 : " + (btnList.size() - 1))); // 없애기
                        editor.putString("name" + count, input.getText().toString());
                        count++;
                        Log.e("", ("=======================name " +  (count - 1) + " : " ) + input.getText().toString()); // 없애기
                        editor.putInt("count",count);
                        //항상 commit & apply 를 해주어야 저장이 된다.
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                        startActivityForResult(intent,1);

//                        onPause();

//                        Intent intent3 = getIntent();
//                        String deviceID = intent3.getStringExtra("deviceId");
//                        Log.e("===============", deviceID);

                        //11/28추가 추가하면 그냥 액티비티 갱신
//                        finish();//인텐트 종료
//                        Intent intent = getIntent(); //인텐트
//                        startActivity(intent); //액티비티 열기


                        addButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                clickButton(addButton);
                            }
                        });

//                        addButton.setOnLongClickListener(new View.OnLongClickListener() {
//                            @Override
//                            public boolean onLongClick(View view) {
//                                Log.e("=====시험용 LongClick23 : ", "성공23" + count);
//                                changeName(addButton, count);
//                                return true;
//                            }
//                        });

                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

        final AlertDialog alert = builder.create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button.getText().toString().equals("+")){
                    alert.show();

                }
                else{
                    Log.e("====", "==========================시험용123");
                    Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                    intent.putExtra("위치", button.getText().toString());
                    intent.putExtra("deviceID", deviceId);
                    startActivity(intent);
                }
            }

        });

        button.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                Log.e("====2", "==========================시험용");         //못하긋다 ;;
                //builder2.setTitle("변경할 이름을 입력하시오");
               // builder2.setView(input2);
                Log.e("=====시험용 LongClick23 : ", "성공23" + count);
                changeName(addButton);

                return true;
            }
        });

        for(int i=0; i<count; i++){
            int j = i;
            btnArr[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e("=======시험용 LongClick : ", "성공" + j);
                    changeName(btnArr[j], j);

                    return true;
                }
            });
        }

    }

    public void changeName(Button button, int count2){   //함수 나중에 밑으로 내리기
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);

        builder.setTitle("변경할 이름을 입력하시오"); //        /n(취소하려면 뒤로가기키를 누르시오)
        builder.setView(input);

        builder.setPositiveButton("확인",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.e("=============변경할 이름 : ", input.getText().toString());
                        button.setText(input.getText().toString());

                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("name" + count2, input.getText().toString());
                        Log.e("", ("=======================name " +  (count2 - 1) + " : " ) + input.getText().toString()); // 없애기

                        editor.commit();

                        //11/28추가 추가하면 그냥 액티비티 갱신
                        finish();//인텐트 종료
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기

                    }
                });

        builder.setNegativeButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Button tmpButton =  btnArr[count-1];
                        btnArr = removeBtn(btnArr, count2);

                        //11/28추가 추가하면 그냥 액티비티 갱신
                        finish();//인텐트 종료
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기


                    }
                });

        final AlertDialog alert = builder.create(); // 아마 이거 위치때문에 오류나는 듯? 차라리 2페이지랑 똑같이 하는게 어떤지...
            alert.show();


    }

    public void changeName(Button button){   //함수 나중에 밑으로 내리기
        int tmpCount = count - 1;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);

        builder.setTitle("변경할 이름을 입력하시오"); //        \n(삭제를 원할시 " + "'삭제'" + "라고 입력하시오)
        builder.setView(input);

        builder.setPositiveButton("확인",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.e("=============변경할 이름 : ", input.getText().toString());
                        button.setText(input.getText().toString());

                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("name" + tmpCount, input.getText().toString());
                        Log.e("", ("=======================name " +  (tmpCount - 1) + " : " ) + input.getText().toString()); // 없애기

                        editor.commit();

                    }
                });

        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

        final AlertDialog alert = builder.create(); // 아마 이거 위치때문에 오류나는 듯? 차라리 2페이지랑 똑같이 하는게 어떤지...
        alert.show();


    }

    public Button[] removeBtn(Button[] arr, int index) {
        if (arr == null || index < 0 || index >= arr.length) {
            return arr;
        }

        Button[] result = new Button[arr.length - 1];

        System.arraycopy(arr, 0, result, 0, index);
        System.arraycopy(arr, index + 1, result, index, arr.length - index - 1);


        String[] sArr = new String[count];
        String[] sArr2 = new String[count - 1];
        String[] s2Arr = new String[count];
        String[] s2Arr2 = new String[count - 1];


        for(int i=0; i< count; i++) {
                sArr[i] = preferences.getString("name" + i, "");
                s2Arr[i] = preferences.getString("deviceId" + i, "");
        }

        SharedPreferences.Editor editor = preferences.edit();

        System.arraycopy(sArr, 0, sArr2, 0, index);
        System.arraycopy(sArr, index + 1, sArr2, index, sArr.length - index - 1);

        System.arraycopy(s2Arr, 0, s2Arr2, 0, index);
        System.arraycopy(s2Arr, index + 1, s2Arr2, index, s2Arr.length - index - 1);

        for (int i=0; i<sArr2.length; i++){
            editor.putString("name" + i, sArr2[i]);
            editor.putString("deviceId" + i, s2Arr2[i]);
        }

        count--;

        editor.putInt("count", count);

        editor.commit();


        return result;
    }

    public Button pushButton() {


        Button button = new Button(this);
        lView.addView(button);
        button.setHeight(100);
        button.setBackgroundResource(R.drawable.btnsize); // 색 바꾸기

        return button;
    }

//    int inCouunt = 0;

    public void initialCall(){

//        if(count !=0){
        button.setVisibility(View.GONE);
//        }

       btnArr = new Button[count+1];

        for(int i=0; i< count; i++) {


            String name = preferences.getString("name" + i,"");
            String device = preferences.getString("deviceId" + i,"");

            Log.e("", ("=======================btnList.size() - 1 : " + (btnList.size() - 1))); // 없애기
            Log.e("", ("=======================name " +  i + " : " ) + name); // 없애기

            btnArr[i] = new Button(this);
            lView.addView(btnArr[i]);
            btnArr[i].setText(name);
            btnArr[i].setHeight(100);
            btnArr[i].setBackgroundResource(R.drawable.btnsize); // 색 바꾸기

            btnArr[i].setTextSize(24);

            int j = i;
            btnArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                    intent.putExtra("위치", btnArr[j].getText().toString());
                    intent.putExtra("deviceId", device);
                    startActivity(intent);
                }
            });

        }

        addButton = pushButton();
        addButton.setText("+");
        addButton.setTextSize(50);
        addButton.setTextColor(Color.parseColor("#FFFFFF"));


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButton(addButton);
            }
        });


    }

}
