package com.softians.poller.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.softians.poller.R;

public class ProfilePage extends AppCompatActivity {
    private android.support.constraint.ConstraintLayout pConstraintLayout2;
    private de.hdodenhof.circleimageview.CircleImageView pCircleImageView;
    private TextView pUserNameTV;
    private android.support.constraint.ConstraintLayout pConstraintLayout3;
    private TextView pStatsTV;
    private TextView pLogoutTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        bindViews();
        pStatsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints = new Intent(ProfilePage.this,Tablay.class);
                ints.putExtra("position",3);
                startActivity(ints);
            }
        });
    }

    private void bindViews() {

        pConstraintLayout2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.constraintLayout2);
        pCircleImageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.circleImageView);
        pUserNameTV = (TextView) findViewById(R.id.userNameTV);
        pConstraintLayout3 = (android.support.constraint.ConstraintLayout) findViewById(R.id.constraintLayout3);
        pStatsTV = (TextView) findViewById(R.id.statsTV);
        pLogoutTV = (TextView) findViewById(R.id.logoutTV);
    }
}
