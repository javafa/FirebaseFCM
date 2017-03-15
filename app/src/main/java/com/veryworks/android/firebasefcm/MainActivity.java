package com.veryworks.android.firebasefcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    EditText editMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMsg = (EditText) findViewById(R.id.editMsg);
    }

    public void sendNotification(View view){
        String token = FirebaseInstanceId.getInstance().getToken();
        String msg = editMsg.getText().toString();

        if(!"".equals(msg)){ // 입력값이 있으면 노티를 날려준다

        }
    }
}
