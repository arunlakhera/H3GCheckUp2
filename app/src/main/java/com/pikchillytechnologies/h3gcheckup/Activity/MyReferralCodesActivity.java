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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pikchillytechnologies.h3gcheckup.Adapter.MyReferralCodesAdapter;
import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Helper.SessionHandler;
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.MyReferralModel;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReferralCodesActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Menu;
    private Button mButton_Contact;
    private VolunteerModel mVolunteerData;
    private CustomerModel mCustomerData;
    private H3GHelper mHelper;

    private RecyclerView mRecyclerview_MyReferralCodeList;
    private List<MyReferralModel> m_MyReferral_List;
    private MyReferralCodesAdapter m_ReferralCodes_Adapter;
    private RecyclerView.LayoutManager m_Layout_Manager;

    private ProgressDialog pd;

    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_referral_codes);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        // Get Customer Data
        getMyReferralCodes();

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
                        mHelper.startVolunteerActivity(getApplicationContext(), DashboardActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_createCustomer:
                        mHelper.startVolunteerActivity(getApplicationContext(), CreateCustomerActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_customerList:
                        String status = "All";
                        mHelper.startVolunteerActivity(getApplicationContext(),CustomerStatusActivity.class, mVolunteerData,status);
                        return true;
                    case R.id.item_myEarnings:
                        mHelper.startVolunteerActivity(getApplicationContext(), MyEarningsActivity.class, mVolunteerData);
                        return true;
                    case R.id.item_myReferralCodes:
                        mDrawerLayout.closeDrawers();
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
                        mHelper.startVolunteerActivity(getApplicationContext(),SignInActivity.class);
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

                mHelper.checkPermission(getApplicationContext(),MyReferralCodesActivity.this);

            }
        });
    }

    /**
     * Function to initialize the variables
     * */
    public void startInitialize(){

        mTextView_ScreenTitle = findViewById(R.id.textview_Title);
        mButton_Menu = findViewById(R.id.button_Menu);
        mButton_Contact = findViewById(R.id.button_Contact);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mRecyclerview_MyReferralCodeList = findViewById(R.id.recyclerview_MyReferralCodes);

        mVolunteerData = new VolunteerModel();
        mCustomerData = new CustomerModel();
        mHelper = new H3GHelper();
        session = new SessionHandler(getApplicationContext());
        m_MyReferral_List = new ArrayList<>();
        m_ReferralCodes_Adapter = new MyReferralCodesAdapter(getApplicationContext(),m_MyReferral_List);
        pd = new ProgressDialog(MyReferralCodesActivity.this);
        m_Layout_Manager = new LinearLayoutManager(getApplicationContext());
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
    }

    /**
     * Function to Set the variables
     * */
    public void setValues(){

        mTextView_ScreenTitle.setText(getResources().getString(R.string.my_referral));

        mRecyclerview_MyReferralCodeList.setHasFixedSize(true);
        mRecyclerview_MyReferralCodeList.setLayoutManager(m_Layout_Manager);
        mRecyclerview_MyReferralCodeList.setAdapter(m_ReferralCodes_Adapter);
    }

    /**
     * Function to get Referral Codes
     * */
    public void getMyReferralCodes(){

        pd.setMessage("Getting Data . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(MyReferralCodesActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST, mHelper.GET_MY_REFERRALCODES_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();

                        if(response.equals("No_Data_Found")){

                            Toast.makeText(getApplicationContext(),"No Data Available",Toast.LENGTH_LONG).show();
                        }else{

                            try{
                                JSONObject referralJSON = new JSONObject(response);
                                JSONArray referralArray = referralJSON.getJSONArray("volunteer_referral");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < referralArray.length(); i++) {

                                    //getting the json object of the particular index inside the array
                                    JSONObject referralObject = referralArray.getJSONObject(i);

                                    //creating a tutorial object and giving them the values from json object
                                    MyReferralModel myReferral = new MyReferralModel(referralObject.getString("referral_code"),referralObject.getString("volunteer_phone"),referralObject.getString("total_uses"),referralObject.getString("active_flag"));

                                    //adding data to list
                                    m_MyReferral_List.add(myReferral);
                                }

                                m_ReferralCodes_Adapter.notifyDataSetChanged();
                                pd.dismiss();

                            }catch (Exception e){

                                Log.e("Error:", e.getMessage());
                            }
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
                params.put("volunteer_phone", mVolunteerData.getM_Phone());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
}
