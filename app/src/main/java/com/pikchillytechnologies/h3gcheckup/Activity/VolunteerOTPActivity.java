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
import com.pikchillytechnologies.h3gcheckup.Helper.SessionHandler;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolunteerOTPActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;
    private TextView mTextView_OTP_Title;
    private EditText mEditText_OTP;
    private Button mButton_Verify;
    private H3GHelper mHelper;
    private VolunteerModel mVolunteerData;
    private boolean mCheckFlag;
    private String mOTP;

    private ProgressDialog pd;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_otp);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        mButton_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkUserData()) {

                    mVolunteerData.setmOTP(mOTP);
                    if (mVolunteerData.getmUserActiveFlag().equals("No")) {

                        uploadUserData();
                    } else {

                        getUserData();
                    }
                }

            }
        });

        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startVolunteerActivity(VolunteerOTPActivity.this, SignInActivity.class, mVolunteerData);
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), VolunteerOTPActivity.this);

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
        mTextView_OTP_Title = findViewById(R.id.textview_OTP_Title);
        mEditText_OTP = findViewById(R.id.edittext_OTP);
        mButton_Verify = findViewById(R.id.button_Verify);
        mVolunteerData = new VolunteerModel();
        mHelper = new H3GHelper();
        session = new SessionHandler(getApplicationContext());
        pd = new ProgressDialog(VolunteerOTPActivity.this);
    }

    /**
     * Function to Set the variables
     */
    public void setValues() {

        mTextView_ScreenTitle.setText("OTP");
        mCheckFlag = false;
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
    }

    /**
     * Function to check user data
     */
    public boolean checkUserData() {

        mOTP = mEditText_OTP.getText().toString();

        if (mOTP.isEmpty()) {
            mEditText_OTP.setError("Please Enter OTP received in SMS.");
            mCheckFlag = false;
        } else {
            mCheckFlag = true;
        }

        return mCheckFlag;
    }

    /**
     * Function to Upload User Data to server
     */

    public void uploadUserData() {

        pd.setMessage("Signing Up . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(VolunteerOTPActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST, mHelper.CREATE_VOLUNTEER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();

                        if (response.equals("SignUp_Success")) {

                            session.loginUser(mVolunteerData.getM_Phone());

                            mHelper.startVolunteerActivity(VolunteerOTPActivity.this, VolunteerWelcomeActivity.class, mVolunteerData);

                        } else if (response.equals("SignUp_Failed")) {

                            Toast.makeText(getApplicationContext(), "Could not Sign Up", Toast.LENGTH_LONG).show();
                        } else if (response.equals("UsernameExists")) {
                            Toast.makeText(getApplicationContext(), "User with this email already exists!", Toast.LENGTH_LONG).show();
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

                //String mPhotoName = mVolunteerData.getM_Phone() + ".jpg";

                String photoName = mVolunteerData.getM_Phone() + ".jpg";
                String mUserUpdatedImage = mVolunteerData.getM_VolunteerPhotoURL();

                params.put("first_name", mVolunteerData.getM_FirstName());
                params.put("last_name", mVolunteerData.getM_LastName());
                params.put("phone", mVolunteerData.getM_Phone());
                params.put("gender", mVolunteerData.getM_Gender());
                params.put("date_of_birth", mVolunteerData.getM_DateOfBirth());
                params.put("address", mVolunteerData.getM_Address());
                params.put("city", mVolunteerData.getM_City());
                params.put("state", mVolunteerData.getM_State());
                params.put("email_id", mVolunteerData.getM_EmailId());
                params.put("photoname", photoName);
                params.put("userphotostring", mUserUpdatedImage);
                params.put("bank_applicant_name", mVolunteerData.getM_BankApplicantName());
                params.put("bank_name", mVolunteerData.getM_BankName());
                params.put("account_number", mVolunteerData.getM_AccountNumber());
                params.put("ifsc_code", mVolunteerData.getM_AccountNumber());
                params.put("otp", mVolunteerData.getmOTP());
                params.put("pan_number", mVolunteerData.getmPanNumber());


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    /**
     * Function to get volunteer data on successful sign in
     */
    public void getUserData() {

        pd.setMessage("Signing In . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(VolunteerOTPActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST, mHelper.GET_VOLUNTEER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();
                        if (response.equals("No_Data")) {

                            Toast.makeText(getApplicationContext(), "OTP does not match", Toast.LENGTH_LONG).show();
                        } else {

                            try {
                                JSONObject userJSON = new JSONObject(response);

                                JSONArray volunteerArray = userJSON.getJSONArray("volunteer_data");
                                JSONObject volunteerObject = volunteerArray.getJSONObject(0);

                                mVolunteerData.setM_FirstName(volunteerObject.getString("first_name"));
                                mVolunteerData.setM_LastName(volunteerObject.getString("last_name"));
                                mVolunteerData.setM_Phone(volunteerObject.getString("phone"));
                                mVolunteerData.setM_Gender(volunteerObject.getString("gender"));
                                mVolunteerData.setM_DateOfBirth(volunteerObject.getString("date_of_birth"));
                                mVolunteerData.setM_Address(volunteerObject.getString("address"));
                                mVolunteerData.setM_City(volunteerObject.getString("city"));
                                mVolunteerData.setM_State(volunteerObject.getString("state"));
                                mVolunteerData.setM_EmailId(volunteerObject.getString("email_id"));
                                mVolunteerData.setM_VolunteerPhotoURL(volunteerObject.getString("photo"));
                                mVolunteerData.setM_BankApplicantName(volunteerObject.getString("bank_applicant_name"));
                                mVolunteerData.setM_BankName(volunteerObject.getString("bank_name"));
                                mVolunteerData.setM_AccountNumber(volunteerObject.getString("account_number"));
                                mVolunteerData.setM_BankIFSCCode(volunteerObject.getString("ifsc_code"));
                                mVolunteerData.setmPanNumber(volunteerObject.getString("pan_number"));

                                session.loginUser(volunteerObject.getString("phone"));
                                mHelper.startVolunteerActivity(VolunteerOTPActivity.this, DashboardActivity.class, mVolunteerData);

                            } catch (Exception e) {

                                Log.e("Error:", e.getMessage());
                            }
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

                params.put("volunteer_phone", mVolunteerData.getM_Phone());
                params.put("otp", mVolunteerData.getmOTP());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

}
