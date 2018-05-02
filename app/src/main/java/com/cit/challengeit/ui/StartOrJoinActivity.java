package com.cit.challengeit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cit.challengeit.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class StartOrJoinActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, StartOrJoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_or_join);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start_or_join_start)
    public void onStartClicked() {
        StartActivity.start(this);
    }

    @OnClick(R.id.btn_start_or_join_join)
    public void onJoinClicked() {
        JoinActivity.start(this);
    }
}