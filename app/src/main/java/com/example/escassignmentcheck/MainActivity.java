package com.example.escassignmentcheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton addContact; //변수 만들기
    private ImageButton contact;
    private TextView phoneNum;
    private TextView[] dials = new TextView[10];
    private TextView star;
    private TextView sharp;
    private ImageButton message;
    private ImageButton call;
    private ImageButton backspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions(); //권한 허용하도록 하는 함수 //오류 안나도록 앱 실행할 때 권한 요청

        setUpUI();

        if(phoneNum.getText().length() == 0) {
            message.setVisibility(View.GONE);
            backspace.setVisibility(View.GONE);
        }
    }

    private void checkPermissions() { //permission 여부 확인 후, 안되어있으면 허용 요청을 보내는 메소드
        int resultCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int resultSms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (resultCall == PackageManager.PERMISSION_DENIED || resultSms == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, 1005);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1005) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) { //권한 허용 누르면 이 문장 성립
                Toast.makeText(this, "권한 허용됨", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpUI() { //레이아웃과 변수 연결
        addContact = findViewById(R.id.main_ibtn_add); //findViewById 매우 중요! 기억하기
        contact = findViewById(R.id.main_ibtn_contact); //id 이름은 activity종류_타입_기능
        phoneNum = findViewById(R.id.main_tv_phone);

        for (int i = 0; i < dials.length; i++) {
            dials[i] = findViewById(getResourceID("main_tv_" + i, "id", this));
        }

        star = findViewById(R.id.main_tv_star);
        sharp = findViewById(R.id.main_tv_sharp);
        message = findViewById(R.id.main_ibtn_massage);
        call = findViewById(R.id.main_ibtn_call);
        backspace = findViewById(R.id.main_ibtn_backspace);

        addContact.setOnClickListener(new View.OnClickListener() { //이 부분은 공식처럼 암기, view가 클릭되면 어떤 행동을 할지 정해주는 부분
            @Override
            public void onClick(View view) {
                // TODO: 연락처 추가
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : 연락처 추가
            }
        });

        setOnClickDial(star, "*");
        setOnClickDial(sharp, "#");

        for (int i = 0; i < 10; i++) {
            setOnClickDial(dials[i], String.valueOf(i));
        }

        message.setOnClickListener(new View.OnClickListener() { //View.OnClickListener 입력할 때 new O 라고 치면 자동완성 가능
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                messageIntent.putExtra("phone_num", phoneNum.getText().toString()); //메시지로 넘어갈 때 전화번호 값 넘어감
                startActivity(messageIntent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent : 화면 전환 정보 저장
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.getText()));
                startActivity(callIntent);
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNum.getText().length() > 0) {
                    //    String formatPhoneNum = PhoneNumberUtils.formatNumber(
                    //            phoneNum.getText().subSequence(0, phoneNum.getText().length() - 1).toString(), //한 글자 지우기
                    //            Locale.getDefault().getCountry());
                    phoneNum.setText(changeToDial(phoneNum.getText().subSequence(0, phoneNum.getText().length() - 1).toString()));

                    if (phoneNum.getText().length() == 0) {
                        message.setVisibility(View.GONE);
                        backspace.setVisibility(View.GONE);
                    }
                }
            }
        });

        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                phoneNum.setText("");

                message.setVisibility(View.GONE);
                backspace.setVisibility(View.GONE);

                return true;
            }
        });

    }

    private void setOnClickDial(View view, final String input) { //다이얼 클릭하면 값이 입력되게 하는 부분
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String formatPhoneNum = PhoneNumberUtils.formatNumber(phoneNum.getText() + input, Locale.getDefault().getCountry());
                //하이픈은 숫자를 입력/백스패이스 클릭 할 때 나타남, formatPhoneNum은 하이픈이 없는 문자를 받았을 때 있는 문자로 반환받는 변수
                //하이픈이 없는 문자가 phoneNum.getText() + input 임. 버튼을 입력하면 원래 입력된 값에 새로운 값을 더하는 것
                //phoneNum.setText(formatPhoneNum);
                phoneNum.setText(changeToDial(phoneNum.getText() + input));

                message.setVisibility(View.VISIBLE);
                backspace.setVisibility(View.VISIBLE);
            }
        });
    }

    private int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID = ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        } else {
            return ResourceID;
        }
    }

    private String changeToDial(String phoneNum) {
        if (phoneNum.contains("#") || phoneNum.contains("*")) {
            phoneNum = phoneNum.replace("-", "");
            return phoneNum;
        } else {
            phoneNum = phoneNum.replace("-", "");
            if (phoneNum.length() > 3 && phoneNum.length() <= 7) {
                return phoneNum.substring(0, 3) + "-" + phoneNum.substring(3);
            } else if (phoneNum.length() > 7 && phoneNum.length() < 12) {
                return phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7);
            } else if (phoneNum.length() > 12) {
                return phoneNum;
            }
        }
        //전화번호 기준 01033844341
        //4글자 이상일 때 3번째 숫자 다음에 -
        //8글자 이상일 때 3번째 다음이랑 7번째 다음에 -
        //12글자 이상이면 - 전부 제거
        //특수문자 * # 있으면 - 전부 제거
        return phoneNum;
    }
}