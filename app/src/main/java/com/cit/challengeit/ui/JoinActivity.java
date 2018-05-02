package com.cit.challengeit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.cit.challengeit.R;
import com.cit.challengeit.network.Question;
import com.cit.challengeit.network.QuizInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity {

    @BindView(R.id.et_join_code)
    EditText mEtCode;

    public static void start(Context context) {
        Intent intent = new Intent(context, JoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_join_join)
    public void onJoinClicked() {
//        String code = mEtCode.getText().toString();
//        if (TextUtils.isEmpty(code)) return;

        // TODO: 17.3.2018 bu kod burda olmamalı, test için burada
        FirebaseDatabase.getInstance().getReference("rooms").child("ABC123").child("questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Question> allQuestions = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    allQuestions.add(snapshot.getValue(Question.class));
                }
                QuestionActivity.start(JoinActivity.this, new QuizInfo("ABC123", allQuestions, 0, 0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
