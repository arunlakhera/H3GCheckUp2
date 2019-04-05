package com.pikchillytechnologies.h3gcheckup.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

public class VolunteerBankDetailActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;
    private EditText mEditText_ApplicantName;
    private EditText mEditText_BankName;
    private EditText mEditText_AccountNumber;
    private EditText mEditText_IFSCCode;
    private Button mButton_Submit;
    private H3GHelper mHelper;
    private VolunteerModel mVolunteerData;
    private String mBankApplicantName;
    private String mBankName;
    private String mAccountNumber;
    private String mBankIFSCCode;
    private boolean mCheckFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_bank_detail);

        // Initialize
        startInitialize();

        // set values
        mTextView_ScreenTitle.setText("Bank Details");
        mCheckFlag = false;
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");

        // Action to perform when submit button is pressed
        mButton_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkUserData()){

                    mVolunteerData.setM_BankApplicantName(mBankApplicantName);
                    mVolunteerData.setM_BankName(mBankName);
                    mVolunteerData.setM_AccountNumber(mAccountNumber);
                    mVolunteerData.setM_BankIFSCCode(mBankIFSCCode);

                    mHelper.startVolunteerActivity(VolunteerBankDetailActivity.this,VolunteerOTPActivity.class, mVolunteerData);

                }

            }
        });

        // Action to perform when Back button is clicked
        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.startVolunteerActivity(VolunteerBankDetailActivity.this,VolunteerPhotoActivity.class, mVolunteerData);

            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(),VolunteerBankDetailActivity.this);

            }
        });
    }

    /**
     * Function to initialize the variables
     * */
    public void startInitialize(){

        mTextView_ScreenTitle = findViewById(R.id.textview_Title);
        mButton_Back = findViewById(R.id.button_Back);
        mButton_Contact = findViewById(R.id.button_Contact);
        mEditText_ApplicantName = findViewById(R.id.edittext_ApplicantName);
        mEditText_BankName = findViewById(R.id.edittext_BankName);
        mEditText_AccountNumber = findViewById(R.id.edittext_AccountNumber);
        mEditText_IFSCCode = findViewById(R.id.edittext_IFSCCode);
        mButton_Submit = findViewById(R.id.button_Submit);
        mVolunteerData = new VolunteerModel();
        mHelper = new H3GHelper();
    }

    /**
     * Function to check user data
     */
    public boolean checkUserData(){

        mBankApplicantName = mEditText_ApplicantName.getText().toString();
        mBankName = mEditText_BankName.getText().toString();
        mAccountNumber = mEditText_AccountNumber.getText().toString();
        mBankIFSCCode = mEditText_IFSCCode.getText().toString();

        if(mBankApplicantName.isEmpty()){
            mEditText_ApplicantName.setError("Please Enter Applicant name");
            mCheckFlag = false;
        }else if(mBankName.isEmpty()){
            mEditText_BankName.setError("Please provide Bank Name");
            mCheckFlag = false;
        }else if(mAccountNumber.isEmpty()){
            mEditText_AccountNumber.setError("Please provide Bank Account Number");
            mCheckFlag = false;
        }else if(mBankIFSCCode.isEmpty()){
            mEditText_IFSCCode.setError("Please provide Bank IFSC Number");
            mCheckFlag = false;
        }else{
            mCheckFlag = true;
        }

        return mCheckFlag;
    }

}
