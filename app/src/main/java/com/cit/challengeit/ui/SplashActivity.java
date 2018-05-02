package com.cit.challengeit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final int FIREBASE_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // if user is logged in
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startGame();
                    return;
                }

                setUI();
            }
        }, 2000);
    }

    private void startGame() {
        finish();
        StartOrJoinActivity.start(this);
    }

    private void setUI() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), FIREBASE_SIGN_IN);
        // TODO: 11.3.2018 we can change theme and add logo
        // https://firebase.google.com/docs/auth/android/firebaseui
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FIREBASE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                startGame();
            } else {
                // Sign in failed, check response for error code
                // TODO: 25.04.2018 kullanıcı login olmadı. Ne yapılmalı?
            }
        }
    }
}
