package com.pikchillytechnologies.h3gcheckup.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.ArrayList;

public class MyEarningsActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;
    private VolunteerModel mVolunteerData;
    private H3GHelper mHelper;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earnings);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        getVolunteerEarnings();

        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startVolunteerActivity(MyEarningsActivity.this, DashboardActivity.class, mVolunteerData);
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), MyEarningsActivity.this);

            }
        });

    }

    /**
     * Function to initialize the variables
     */
    public void startInitialize() {

        mTextView_ScreenTitle = findViewById(R.id.textview_Title);
        mButton_Back = findViewById(R.id.button_Back);
        mButton_Contact = findViewById(R.id.button_Contact);
        mVolunteerData = new VolunteerModel();
        mHelper = new H3GHelper();

    }

    /**
     * Function to Set the variables
     */
    public void setValues() {

        mTextView_ScreenTitle.setText(getResources().getString(R.string.my_earnings));
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        pd = new ProgressDialog(MyEarningsActivity.this);

    }

    /**
     * Function to get the Volunteer Earnings
     */
    public void getVolunteerEarnings() {

    }

}
