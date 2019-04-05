package com.pikchillytechnologies.h3gcheckup.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
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
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReferralCodeActivity extends AppCompatActivity {

    private Button mButton_Menu;
    private Button mButton_Contact;

    private TextView mTextView_Referral_Title;
    private TextView mTextView_ReferralCode;
    private Button mButton_Share;
    private Button mButton_Dashboard;

    private VolunteerModel mVolunteerData;
    private CustomerModel mCustomerData;
    private H3GHelper mHelper;

    private String mReferralCode;
    private ProgressDialog pd;

    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_code);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        // Function to update User Referral Code
        updateUserReferralCode();

        // Action to perform when Share button is pressed
        mButton_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.shareReferralCode(getApplicationContext(), mReferralCode);
            }
        });

        // Action to perform when Dashboard Button is pressed
        mButton_Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startVolunteerActivity(ReferralCodeActivity.this, DashboardActivity.class, mVolunteerData);
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), ReferralCodeActivity.this);

            }
        });

        mButton_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(navigationView);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.item_dashboard:
                        mHelper.startVolunteerActivity(getApplicationContext(), DashboardActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_createCustomer:
                        mHelper.startVolunteerActivity(getApplicationContext(), CreateCustomerActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_customerList:
                        String status = "All";
                        mHelper.startVolunteerActivity(getApplicationContext(), CustomerStatusActivity.class, mVolunteerData, status);
                        return true;
                    case R.id.item_myEarnings:
                        mHelper.startVolunteerActivity(getApplicationContext(), MyEarningsActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_myReferralCodes:
                        mHelper.startVolunteerActivity(getApplicationContext(), MyReferralCodesActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_generateReferral:
                        mDrawerLayout.closeDrawers();
                        return true;
                    case R.id.item_updateProfile:
                        mHelper.startVolunteerActivity(getApplicationContext(), VolunteerProfileActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_shareApp:
                        mHelper.shareApp(getApplicationContext());
                        return true;
                    case R.id.item_logout:
                        session.logoutUser();
                        mHelper.startVolunteerActivity(getApplicationContext(), SignInActivity.class);
                        return true;
                    default:
                        mDrawerLayout.closeDrawers();
                        return true;
                }
            }
        });

    }

    /**
     * Function to initialize the variables
     */
    public void startInitialize() {

        mButton_Menu = findViewById(R.id.button_Menu);
        mButton_Contact = findViewById(R.id.button_Contact);
        mTextView_Referral_Title = findViewById(R.id.textview_Title);
        mTextView_ReferralCode = findViewById(R.id.textview_ReferralCode);
        mButton_Share = findViewById(R.id.button_Share);
        mButton_Dashboard = findViewById(R.id.button_Dashboard);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mVolunteerData = new VolunteerModel();
        mCustomerData = new CustomerModel();
        mHelper = new H3GHelper();
        session = new SessionHandler(getApplicationContext());
    }

    /**
     * Function to Set the variables
     */
    public void setValues() {

        mTextView_Referral_Title.setText("Code");
        pd = new ProgressDialog(ReferralCodeActivity.this);
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        mCustomerData = getIntent().getParcelableExtra("customerData");
        generateCode();
    }

    /**
     * Function to generate unique referral code
     */
    public void generateCode() {
        String unique_Id1;
        String unique_id2;

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        try {
            unique_Id1 = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            mReferralCode = unique_Id1 + today.monthDay + today.month + today.year + today.format("%k%M%S");
            mTextView_ReferralCode.setText(mReferralCode);
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }

    }

    /**
     * Function to store referral code in server database
     */
    public void updateUserReferralCode() {

        pd.setMessage("Saving Referral Code . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(ReferralCodeActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST,
                mHelper.CREATE_REFERRAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();

                        if (response.equals("Referral_Success")) {

                            //Toast.makeText(getApplicationContext(), "Referral Code Saved..", Toast.LENGTH_LONG).show();
                            pd.dismiss();

                        } else if (response.equals("Referral_Failed")) {

                            Toast.makeText(getApplicationContext(), "Could not save Referral Code", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Occured while Saving", Toast.LENGTH_LONG).show();
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

                params.put("referral_code", mReferralCode);
                params.put("volunteer_phone", mVolunteerData.getM_Phone());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

}
