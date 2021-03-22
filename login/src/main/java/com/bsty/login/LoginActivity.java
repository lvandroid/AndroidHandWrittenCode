package com.bsty.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bsty.annotation.BindPath;
import com.bsty.arouter.ARouter;

@BindPath(value = "login/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void jumpActivity(View view) {
        ARouter.getInstance().jumpActivity("member", null);
    }
}