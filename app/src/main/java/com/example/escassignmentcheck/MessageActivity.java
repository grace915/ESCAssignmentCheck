package com.example.escassignmentcheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText phoneNum;
    private EditText content;
    private FloatingActionButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        setUpUI();

        setSupportActionBar(toolbar); //툴바를 액션바로
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기, 기능은 x

        String getPhoneNum = getIntent().getStringExtra("phone_num"); //mainActivity에서 입력한 전화번호 값 받음
        phoneNum.setText(getPhoneNum);
        phoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //메시지 입력 창에서 전화번호 입력할 때 "-" 자동으로 입력되게 함
    }

    private void setUpUI() {
        toolbar = findViewById(R.id.message_toolbar);
        phoneNum = findViewById(R.id.message_et_phone);
        content = findViewById(R.id.message_et_content);
        send = findViewById(R.id.message_fab_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    // 문자 전송하는 코드
                    smsManager.sendTextMessage(phoneNum.getText().toString(), null, content.getText().toString(), null, null);
                    finish();
                    Toast.makeText(MessageActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //툴바에서 메뉴(뒤로가기 버튼) 눌렀을 때의 기능 설정
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}