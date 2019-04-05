package com.pikchillytechnologies.h3gcheckup.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pikchillytechnologies.h3gcheckup.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Splash screen timer
        int SPLASH_TIME_OUT = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Show Splash Screen with Customer Logo
                // Start Main Activity
                Intent destinationIntent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(destinationIntent);

                // Close the activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
