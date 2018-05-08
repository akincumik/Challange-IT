package com.cit.challengeit.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cit.challengeit.R;
import com.cit.challengeit.network.Question;
import com.cit.challengeit.network.QuizInfo;
import com.cit.challengeit.utils.SharedPrefManager;
import com.cit.challengeit.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionActivity extends AppCompatActivity {

    private static final long QR_REFRESH_INTERVAL = TimeUnit.SECONDS.toMillis(20);
    private static final String ARG_QUIZ_INFO = "ARG_QUIZ_INFO";

    @BindView(R.id.iv_question_user_photo)
    ImageView mIvUserPhoto;
    @BindView(R.id.tv_question_text)
    TextView mTvText;
    @BindView(R.id.tv_question_count)
    TextView mTvCount;
    @BindView(R.id.tv_question_score)
    TextView mTvScore;
    @BindView(R.id.pb_question)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_question_time)
    TextView mTvTime;
    @BindView(R.id.cv_question_answer_a)
    CardView mCvAnswerA;
    @BindView(R.id.cv_question_answer_b)
    CardView mCvAnswerB;
    @BindView(R.id.cv_question_answer_c)
    CardView mCvAnswerC;
    @BindView(R.id.cv_question_answer_d)
    CardView mCvAnswerD;
    @BindView(R.id.tv_question_answer_a)
    TextView mTvAnswerA;
    @BindView(R.id.tv_question_answer_b)
    TextView mTvAnswerB;
    @BindView(R.id.tv_question_answer_c)
    TextView mTvAnswerC;
    @BindView(R.id.tv_question_answer_d)
    TextView mTvAnswerD;
    @BindView(R.id.iv_question_image)
    ImageView mIvImage;
    @BindView(R.id.ll_question_choice_container)
    LinearLayout mLlChoiceContainer;

    private CountDownTimer mCountDownTimer;
    private boolean mTimeFinished;
    private Question mCurrentQuestion;
    private int mRemainSec;
    private int mRemainMillis;
    private boolean mQuestionAnswered;
    private QuizInfo mQuizInfo;
    private String mUserAnswer = "-";
    private String deneme;

    public static void start(Context context, QuizInfo quizInfo) {
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(ARG_QUIZ_INFO, quizInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        setUI();
//        showToolbar(false);
    }

    private void setUI() {
        mQuizInfo = (QuizInfo) getIntent().getSerializableExtra(ARG_QUIZ_INFO);
        mCurrentQuestion = mQuizInfo.questions.get(mQuizInfo.currentQuestionNumber);
        Utils.setUserPhoto(SharedPrefManager.getUser().photo, mIvUserPhoto);
        mQuestionAnswered = false;

        mTvCount.setText((mQuizInfo.currentQuestionNumber + 1 + mQuizInfo.questions.size() - mQuizInfo.questions.size()) + "/" + mQuizInfo.questions.size());
        mTvText.setText(mCurrentQuestion.text);
        mTvScore.setText(String.valueOf(mQuizInfo.score));
        mTvAnswerA.setText(mCurrentQuestion.a);
        mTvAnswerB.setText(mCurrentQuestion.b);
        mTvAnswerC.setText(mCurrentQuestion.c);
        mTvAnswerD.setText(mCurrentQuestion.d);
        shuffleChoices();
        if (!TextUtils.isEmpty(mCurrentQuestion.image)) {
            Glide.with(this).load(mCurrentQuestion.image).into(mIvImage);
        }

        //QR_REFRESH_INTERVAL = 20.000 milli second
        mProgressBar.setMax((int) (QR_REFRESH_INTERVAL / 1000));
        mProgressBar.setProgress(0);

        mCountDownTimer = new CountDownTimer(QR_REFRESH_INTERVAL, 1000) {

            public void onTick(long millisUntilFinished) {
                mRemainMillis = (int) millisUntilFinished;
                mRemainSec = (int) (millisUntilFinished / 1000);
                mProgressBar.setProgress(mRemainSec);
                mTvTime.setText(String.valueOf(mRemainSec));
            }

            public void onFinish() {
                mTimeFinished = true;
                mTvTime.setText("0");
                mProgressBar.setProgress(0);
                showAnswer(mUserAnswer);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 31.3.2018 son soru cevaplandıysa bitiş ekranını göster

                        finish();
                        QuestionActivity.start(QuestionActivity.this, mQuizInfo);
                    }
                }, 3000);
            }
        };
        mCountDownTimer.start();
    }

    private void shuffleChoices() {
        mLlChoiceContainer.removeAllViews();
        for (String choice : mCurrentQuestion.mChoiceOrder) {
            switch (choice) {
                case "a":
                    mLlChoiceContainer.addView(mCvAnswerA);
                    break;
                case "b":
                    mLlChoiceContainer.addView(mCvAnswerB);
                    break;
                case "c":
                    mLlChoiceContainer.addView(mCvAnswerC);
                    break;
                case "d":
                    mLlChoiceContainer.addView(mCvAnswerD);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        revealChoice(mCvAnswerA);
        revealChoice(mCvAnswerB);
        revealChoice(mCvAnswerC);
        revealChoice(mCvAnswerD);
    }

    private void revealChoice(View view) {
        view.setAlpha(0f);
        view.setScaleX(0.7f);
        view.setScaleY(0.7f);
        ObjectAnimator.ofFloat(view, "alpha", 1).setDuration(100).start();
        ObjectAnimator.ofFloat(view, "scaleX", 1).setDuration(300).start();
        ObjectAnimator.ofFloat(view, "scaleY", 1).setDuration(300).start();
    }

    @Override
    protected void onDestroy() {
        mCountDownTimer.cancel();
//        showToolbar(true);

        super.onDestroy();
    }

    @OnClick({R.id.tv_question_answer_a, R.id.tv_question_answer_b, R.id.tv_question_answer_c, R.id.tv_question_answer_d,
            R.id.cv_question_answer_a, R.id.cv_question_answer_b, R.id.cv_question_answer_c, R.id.cv_question_answer_d})
    public void onClick(View view) {
        if (!mTimeFinished && !mQuestionAnswered) {
            mQuestionAnswered = true;
            mUserAnswer = (String) view.getTag();
            selectChoice();
        }
    }

    private void updateScore(boolean isAnswerCorrect) {
        // if answer is correct, update score
        if (isAnswerCorrect) {
            final int score = (10 + (int) Math.ceil(mRemainSec / 4.0));
            mQuizInfo.score += score;
            mTvScore.setText(String.valueOf(mQuizInfo.score));
            FirebaseDatabase.getInstance().getReference("rooms").child(mQuizInfo.roomId)
                    .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("score").setValue(mQuizInfo.score);
        }

//        // update true and false count
//        getFirebaseRootRef().child("scoreboard/" + getUser().role + "/users/" + getUid() + "/qr" + mQuizRepeat + (isAnswerCorrect ? "true" : "false"))
//                .runTransaction(new Transaction.Handler() {
//                    @Override
//                    public Transaction.Result doTransaction(MutableData mutableData) {
//                        Integer oldValue = mutableData.getValue(Integer.class);
//                        mutableData.setValue(oldValue == null ? 1 : oldValue + 1);
//                        return Transaction.success(mutableData);
//                    }
//
//                    @Override
//                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
//                });
    }

    private void showAnswer(String userAnswer) {
        boolean isAnswerCorrect = mCurrentQuestion.correct.equals(userAnswer);
        mQuizInfo.answeredTime = (int) ((QR_REFRESH_INTERVAL - mRemainMillis) / 1000);

        updateScore(isAnswerCorrect);
//        getFirebaseRootRef().child("userInfo/" + getUid() + "/answers/" + mQuizRepeat).child(mCurrentQuestion.id).setValue(userAnswer);

        CardView correctView = findChoiceWithTag(mCurrentQuestion.correct);
        CardView userView = findChoiceWithTag(userAnswer);
        hideFalseChoices(correctView, userView);

        ++mQuizInfo.currentQuestionNumber;
        // if current question is last question
//        if (mQuizInfo.currentQuestionNumber == mQuizInfo.questions.size()) {
//            getFirebaseRootRef().child("userInfo/" + getUid() + "/quizRepeat").setValue(mQuizRepeat);
//            getFirebaseMyScoreboardRef().child("quizRepeat").setValue(mQuizRepeat);
//        }
    }

    private void selectChoice() {
        CardView userView = findChoiceWithTag(mUserAnswer);
        userView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void hideFalseChoices(CardView correctView, CardView userView) {
        if (correctView != null) {
            if (userView == null) { // if nothing chose
                correctView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.red));
            } else {
                correctView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
                if (correctView != userView) {
                    userView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.red));
                }
            }
            if (mCvAnswerA != correctView && mCvAnswerA != userView)
                ObjectAnimator.ofFloat(mCvAnswerA, "alpha", 0.4f).setDuration(800).start();
            if (mCvAnswerB != correctView && mCvAnswerB != userView)
                ObjectAnimator.ofFloat(mCvAnswerB, "alpha", 0.4f).setDuration(800).start();
            if (mCvAnswerC != correctView && mCvAnswerC != userView)
                ObjectAnimator.ofFloat(mCvAnswerC, "alpha", 0.4f).setDuration(800).start();
            if (mCvAnswerD != correctView && mCvAnswerD != userView)
                ObjectAnimator.ofFloat(mCvAnswerD, "alpha", 0.4f).setDuration(800).start();
        }
    }

    private CardView findChoiceWithTag(String tag) {
        switch (tag) {
            case "a":
                return mCvAnswerA;
            case "b":
                return mCvAnswerB;
            case "c":
                return mCvAnswerC;
            case "d":
                return mCvAnswerD;
        }
        return null;
    }
}
