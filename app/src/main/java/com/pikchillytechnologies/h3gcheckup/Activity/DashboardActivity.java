package com.pikchillytechnologies.h3gcheckup.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Menu;
    private Button mButton_Contact;
    private VolunteerModel mVolunteerData;
    private CustomerModel mCustomerData;
    private H3GHelper mHelper;

    private TextView mTextView_TotalCustomers;
    private Button mButton_Refresh;
    private LinearLayout mLayout_InProgress;
    private LinearLayout mLayout_CardIssued;
    private TextView mTextView_InProgress;
    private TextView mTextView_CardIssued;

    private Button mButton_CreateCustomer;
    private Button mButton_MyEarnings;
    private ProgressDialog pd;

    private int mTotalCustomers;
    private int mTotalInProgress;
    private int mTotalCardIssued;
    private List<CustomerModel> m_Customers_List;

    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        // Get Customer Data
        getCustomerData();

        mButton_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTotalCustomers = 0;
                mTotalInProgress = 0;
                mTotalCardIssued = 0;
                m_Customers_List.clear();
                getCustomerData();
                updateDashboard();
            }
        });

        // Action to perform when Total Customers is pressed
        mTextView_TotalCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status = "All";
                mHelper.startVolunteerActivity(getApplicationContext(), CustomerStatusActivity.class, mVolunteerData, status);

            }
        });

        // Action to perform when layout in progress is pressed
        mLayout_InProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status = "In Progress";
                mHelper.startVolunteerActivity(getApplicationContext(), CustomerStatusActivity.class, mVolunteerData, status);

            }
        });

        // Action to perform when layout card issued is pressed
        mLayout_CardIssued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status = "Card Issued";
                mHelper.startVolunteerActivity(getApplicationContext(), CustomerStatusActivity.class, mVolunteerData, status);

            }
        });

        // Action to perform when Create Customer is pressed
        mButton_CreateCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startVolunteerActivity(DashboardActivity.this, CreateCustomerActivity.class, mVolunteerData);
            }
        });

        // Action to perform when My Earnings is pressed
        mButton_MyEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startVolunteerActivity(DashboardActivity.this, MyEarningsActivity.class, mVolunteerData);
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

                // set item as selected to persist highlight
                menuItem.setChecked(true);

                switch (menuItem.getItemId()) {

                    case R.id.item_dashboard:
                        mDrawerLayout.closeDrawers();
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
                        mHelper.startVolunteerActivity(getApplicationContext(), ReferralCodeActivity.class, mVolunteerData);
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

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), DashboardActivity.this);

            }
        });

    }

    /**
     * Function to initialize the variables
     */
    public void startInitialize() {

        mTextView_ScreenTitle = findViewById(R.id.textview_Title);
        mButton_Menu = findViewById(R.id.button_Menu);
        mButton_Contact = findViewById(R.id.button_Contact);
        mTextView_TotalCustomers = findViewById(R.id.textview_TotalCustomers);
        mButton_Refresh = findViewById(R.id.button_Refresh);
        mLayout_InProgress = findViewById(R.id.layout_In_Progress);
        mLayout_CardIssued = findViewById(R.id.layout_Card_Issued);
        mTextView_InProgress = findViewById(R.id.textview_InProgress);
        mTextView_CardIssued = findViewById(R.id.textview_CardIssued);
        mButton_CreateCustomer = findViewById(R.id.button_CreateCustomer);
        mButton_MyEarnings = findViewById(R.id.button_MyEarnings);
        mVolunteerData = new VolunteerModel();
        mCustomerData = new CustomerModel();
        mHelper = new H3GHelper();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        session = new SessionHandler(getApplicationContext());
    }

    /**
     * Function to Set the variables
     */
    public void setValues() {

        mTextView_ScreenTitle.setText(getResources().getString(R.string.dashboard));
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        pd = new ProgressDialog(DashboardActivity.this);
        m_Customers_List = new ArrayList<>();
        mTotalCustomers = 0;
        mTotalInProgress = 0;
        mTotalCardIssued = 0;
    }

    /**
     * Function to get Customer Data
     */
    public void getCustomerData() {

        pd.setMessage("Getting Data . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(DashboardActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST, mHelper.GET_CUSTOMER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();

                        if (response.equals("No_Data_Found")) {

                            Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_LONG).show();
                        } else {

                            try {
                                JSONObject customerJSON = new JSONObject(response);
                                JSONArray customerArray = customerJSON.getJSONArray("volunteer_customer");

                                mTotalCustomers = customerArray.length();

                                //now looping through all the elements of the json array
                                for (int i = 0; i < customerArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject customerObject = customerArray.getJSONObject(i);

                                    //creating a tutorial object and giving them the values from json object
                                    CustomerModel customer = new CustomerModel();

                                    if (customerObject.getString("customer_active_flag").equals("In Progress")) {
                                        mTotalInProgress = mTotalInProgress + 1;
                                    }

                                    if (customerObject.getString("customer_active_flag").equals("Card Issued")) {
                                        mTotalCardIssued = mTotalCardIssued + 1;
                                    }

                                    //adding data to list
                                    m_Customers_List.add(customer);
                                }

                                updateDashboard();

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
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    public void updateDashboard() {

        String mTotal_Customer_Message = getResources().getString(R.string.total_customer) + ": " + m_Customers_List.size();
        String mTotal_In_Progress_Message = getResources().getString(R.string.in_progress) + ": " + mTotalInProgress;
        String mTotal_Card_Issued_Message = getResources().getString(R.string.card_issued) + ": " + mTotalCardIssued;

        mTextView_TotalCustomers.setText(mTotal_Customer_Message);
        mTextView_InProgress.setText(mTotal_In_Progress_Message);
        mTextView_CardIssued.setText(mTotal_Card_Issued_Message);

    }

}
