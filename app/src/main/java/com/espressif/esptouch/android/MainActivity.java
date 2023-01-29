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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {       //================== 이름 바꾸는 기능 구현 했지만 쓸려면 preference에 넣어야 함
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //         안드로이드 내부 db? 색 바뀌는 기준을 어떻게 잡아야하지?
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        Button btn4 = findViewById(R.id.button4);
        Button btn5 = findViewById(R.id.button5);
        Button btn6 = findViewById(R.id.button6);
        Button btn7 = findViewById(R.id.button7);
        Button btn8 = findViewById(R.id.button8);
        Button btn9 = findViewById(R.id.button9);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);

        ArrayList<Button> btnArr = new ArrayList<Button>();

        btnArr.add(btn1);
        btnArr.add(btn2);
        btnArr.add(btn3);
        btnArr.add(btn4);
        btnArr.add(btn5);
        btnArr.add(btn6);
        btnArr.add(btn7);
        btnArr.add(btn8);
        btnArr.add(btn9);

        for (int i = 0; i < btnArr.size(); i++) {
//            if(true){
            int k = i;
            btnArr.get(i).setTextColor(Color.parseColor("#ff0000"));


            btnArr.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class); // 버튼별로 다른 페이지(SharedPreferences) 만들기
                    intent.putExtra("첫페이지", k);
                    intent.putExtra("위치", btnArr.get(k).getText().toString());
                    startActivity(intent);
                }
            });
            btnArr.get(k).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.e("=======시험용 LongClick : ", "성공" + k);
                    changeName(btnArr.get(k), k);

                    return true;
                }
            });

        }

    }

    public void changeName(Button button, int pos){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);

        builder.setTitle("변경할 이름을 입력하시오");
        builder.setView(input);

        builder.setPositiveButton("확인",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.e("=============변경할 이름 : ", input.getText().toString());
                        button.setText(input.getText().toString());

                        //SharedPreferences.Editor editor = preferences.edit();

                        //editor.putString("name" + count, input.getText().toString());
                        Log.e("", ("=======================name " +  (pos - 1) + " : " ) + input.getText().toString()); // 없애기

                       // editor.commit();

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

}