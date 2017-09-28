package com.softians.poller.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.softians.poller.R;

import customfonts.MyTextView;

public class LoginRegister extends AppCompatActivity {
    //checking how it goes
    ImageView logoclick ;
    MyTextView signin,signup;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemClock.sleep(1000);
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        getSupportActionBar().hide();
        constraintLayout = (ConstraintLayout)findViewById(R.id.constraint);
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
        signin = (MyTextView)findViewById(R.id.sin);
        logoclick = (ImageView)findViewById(R.id.onaddi);
        signup = (MyTextView)findViewById(R.id.sign_up);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints = new Intent(LoginRegister.this,ProfilePage.class);
                startActivity(ints);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent(LoginRegister.this,Registration.class);
                startActivity(intents);
            }
        });
    }
}
