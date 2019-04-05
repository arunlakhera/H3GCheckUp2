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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomerOTPActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;
    private EditText mEditText_OTP;
    private Button mButton_Verify;

    private H3GHelper mHelper;
    private VolunteerModel mVolunteerData;
    private CustomerModel mCustomerData;
    private boolean mCheckFlag;
    private String mOTP;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_otp);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        mButton_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkUserData()){

                    mCustomerData.setmOTP(mOTP);
                    uploadUserData();
                }
            }
        });

        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startCustomerActivity(CustomerOTPActivity.this, CreateCustomerActivity.class, mVolunteerData, mCustomerData);
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), CustomerOTPActivity.this);

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
        mEditText_OTP = findViewById(R.id.edittext_OTP);
        mButton_Verify = findViewById(R.id.button_Verify);

        mVolunteerData = new VolunteerModel();
        mCustomerData = new CustomerModel();
        mHelper = new H3GHelper();
        pd = new ProgressDialog(CustomerOTPActivity.this);

    }

    /**
     * Function to Set the variables
     * */
    public void setValues(){

        mCheckFlag = false;
        mTextView_ScreenTitle.setText("OTP");
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        mCustomerData = getIntent().getParcelableExtra("customerData");
    }

    /**
     * Function to check user data
     */
    public boolean checkUserData(){

        mOTP = mEditText_OTP.getText().toString();

        if(mOTP.isEmpty()){
            mEditText_OTP.setError("Please Enter OTP received in SMS.");
            mCheckFlag= false;
        }else{
            mCheckFlag = true;
        }

        return mCheckFlag;
    }

    /**
     * Function to Upload User Data to server
     * */

    public void uploadUserData(){

        pd.setMessage("Creating Customer . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(CustomerOTPActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST, mHelper.CREATE_CUSTOMER_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                       pd.hide();

                        if(response.equals("Customer_Success")) {
                            Toast.makeText(getApplicationContext(),"Customer Created and Linked Successfully",Toast.LENGTH_LONG).show();
                            mHelper.startCustomerActivity(CustomerOTPActivity.this, CustomerWelcomeActivity.class, mVolunteerData, mCustomerData);

                        }else if(response.equals("Customer_Failed")){

                            Toast.makeText(getApplicationContext(),"Could not create Customer",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("ErrorResponse", error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("first_name", mCustomerData.getM_FirstName());
                params.put("last_name", mCustomerData.getM_LastName());
                params.put("customer_phone", mCustomerData.getM_Phone());
                params.put("gender", mCustomerData.getM_Gender());
                params.put("date_of_birth", mCustomerData.getM_DateOfBirth());
                params.put("address", mCustomerData.getM_Address());
                params.put("city", mCustomerData.getM_City());
                params.put("state", mCustomerData.getM_State());
                params.put("email_id", mCustomerData.getM_EmailId());
                params.put("blood_group", mCustomerData.getmBloodGroup());
                params.put("allergies", mCustomerData.getmAllergies());
                params.put("genetic_disorder", mCustomerData.getmGeneticDisorder());
                params.put("otp", mCustomerData.getmOTP());
                params.put("volunteer_phone", mVolunteerData.getM_Phone());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

}
