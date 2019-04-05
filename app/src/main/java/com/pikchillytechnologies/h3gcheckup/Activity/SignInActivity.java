package com.pikchillytechnologies.h3gcheckup.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private TextView mTextView_WhyPartner;
    private EditText mEditText_Phone;
    private Button mButton_SignIn;
    private CheckBox mCheckBox_AcceptTerms;
    private H3GHelper mHelper;
    private String mUserPhoneNumber;
    private String mTermsConditions;
    private String mWhyPartnerDetail;
    private boolean checkFlag;
    private boolean mTermConditions_Flag;
    private VolunteerModel mVolunteerData;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize
        startInitialize();

        // Action to perform when Why Partner with us is pressed
        mTextView_WhyPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWhyPartnerWithUs();
            }
        });

        // Action to perform when Terms and Conditions checkbox is pressed
        mCheckBox_AcceptTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // If user accepted Terms and Conditions then update flag and and the checkbox to true
                if (!mTermConditions_Flag) {

                    mTermConditions_Flag = true;
                    mCheckBox_AcceptTerms.setChecked(true);
                } else {

                    // Function to show terms and conditions to user
                    showTermConditions();
                }

            }
        });

        mButton_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check Required Values
                if (startCheck()) {

                    // Call API to check user in Database
                    checkUserData();

                }
            }
        });

    }

    /**
     * Function to initialize the variables
     */
    public void startInitialize() {

        mButton_SignIn = findViewById(R.id.button_SignIn);
        mTextView_WhyPartner = findViewById(R.id.textview_WhyPartner);
        mEditText_Phone = findViewById(R.id.edittext_Phone);
        mCheckBox_AcceptTerms = findViewById(R.id.checkbox_AcceptTerms);
        mHelper = new H3GHelper();
        checkFlag = false;
        mTermConditions_Flag = false;
        mVolunteerData = new VolunteerModel();

        pd = new ProgressDialog(SignInActivity.this);

        mWhyPartnerDetail = "\n 1. We provide excellent payment opportunity to the volunteers who join with us.";
        mWhyPartnerDetail += "\n 2. Volunteer gets paid for every customer that joins us through them";
        mWhyPartnerDetail += "\n 3. Volunteer can also earn through referral code that is generated in the app while registering user.";
        mWhyPartnerDetail += "\n 4. If customer shares the referral code provided by volunteer more earning is added to the volunteer account.";
        mWhyPartnerDetail += "\n 5. Money is transferred directly to the volunteer's bank account provided to us.";
        mWhyPartnerDetail += "\n 6. Volunteer can keep track of their customers and earnings through the app.";
        mWhyPartnerDetail += "\n 7. Customer gets discount at the designated diagnostic center using the card provided by the company.";

        mTermsConditions = "\n 1. The App requires you to share your personal information with company for this application purpose.";
        mTermsConditions += "\n 2. You allow us to store your bank related information to allow company to make online payment.";
        mTermsConditions += "\n 3. You allow app to access camera to take your photo and customers photo and sharing them with the company";

    }

    /**
     * Function to show Why Partner with us
     */
    public void showWhyPartnerWithUs() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.why_partner_title);
        alertBuilder.setMessage(mWhyPartnerDetail);
        alertBuilder.setPositiveButton(getResources().getString(R.string.btn_ok), null);
        alertBuilder.create();
        alertBuilder.show();

    }

    /**
     * Function to show Terms and conditions
     */
    public void showTermConditions() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.terms_cond_title);
        alertBuilder.setMessage(mTermsConditions);
        alertBuilder.setPositiveButton(getResources().getString(R.string.btn_agree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTermConditions_Flag = true;
                mCheckBox_AcceptTerms.setChecked(true);
            }
        });

        alertBuilder.setNegativeButton(getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTermConditions_Flag = false;
                mCheckBox_AcceptTerms.setChecked(false);

            }
        });

        alertBuilder.create();
        alertBuilder.show();

    }

    /**
     * Function to check required values
     */
    public boolean startCheck() {

        // Try to store the phone number entered in variable
        try {
            mUserPhoneNumber = mEditText_Phone.getText().toString();
        } catch (Exception e) {
            Log.e("PhoneNumber:", e.getMessage());
        }

        // Show message to user if phone number is not entered or if phone number is not of 10 digits
        if (mUserPhoneNumber.isEmpty()) {

            mEditText_Phone.setError(getResources().getString(R.string.phone_num_error));
            checkFlag = false;

        } else if (mUserPhoneNumber.length() < 10) {

            mEditText_Phone.setError(getResources().getString(R.string.phone_num_valid_error));
            checkFlag = false;

        } else if (!mCheckBox_AcceptTerms.isChecked()) {

            mCheckBox_AcceptTerms.setError("Error");
            mHelper.show_Toast(getApplicationContext(), getResources().getString(R.string.term_cond_error), Toast.LENGTH_LONG);
            checkFlag = false;

        } else {

            checkFlag = true;
        }

        return checkFlag;
    }

    /**
     * Function to check if the user exists in the database
     */
    public void checkUserData() {

        // Check User in Database and Get user data if user exists

        pd.setMessage("Signing In . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST, mHelper.CREATE_VOL_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();
                        mVolunteerData.setM_Phone(mUserPhoneNumber);

                        if (response.equals("Sign_In_Passed")) {

                            mVolunteerData.setmUserActiveFlag("Yes");
                            mHelper.startVolunteerActivity(getApplicationContext(), VolunteerOTPActivity.class, mVolunteerData);

                        } else if (response.equals("Sign_In_Failed")) {

                            mVolunteerData.setmUserActiveFlag("No");
                            mHelper.startVolunteerActivity(getApplicationContext(), VolunteerDetailActivity.class, mVolunteerData);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("ErrorResponse", error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("volunteer_phone", mUserPhoneNumber);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);


        // If User is New navigate to volunteer detail activity
/*
        mVolunteerData.setM_Phone(mUserPhoneNumber);
        mVolunteerData.setmUserActiveFlag("No");
        Intent destIntent = new Intent(getApplicationContext(),VolunteerDetailActivity.class);
        destIntent.putExtra("volunteerData", mVolunteerData);
        startActivity(destIntent);
*/
    }

}
