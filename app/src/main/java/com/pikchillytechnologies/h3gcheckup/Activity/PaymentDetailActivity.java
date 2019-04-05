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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Helper.SessionHandler;
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

public class PaymentDetailActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Menu;
    private Button mButton_Contact;

    private Button mButton_Done;
    private VolunteerModel mVolunteerData;
    private CustomerModel mCustomerData;
    private H3GHelper mHelper;

    private TextView mTextView_CustName;
    private TextView mTextView_CustDOB;
    private TextView mTextView_CustPhone;
    private TextView mTextView_CustEmailId;
    private TextView mTextView_Address;
    private TextView mTextView_Amount;
    private TextView mTextView_Status;

    //Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        mButton_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.startCustomerActivity(PaymentDetailActivity.this, ReferralCodeActivity.class, mVolunteerData, mCustomerData);
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), PaymentDetailActivity.this);
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
    }

    /**
     * Function to initialize the variables
     */
    public void startInitialize() {

        mTextView_ScreenTitle = findViewById(R.id.textview_Title);
        mButton_Menu = findViewById(R.id.button_Menu);
        mButton_Contact = findViewById(R.id.button_Contact);
        mButton_Done = findViewById(R.id.button_Done);

        mTextView_CustName = findViewById(R.id.textview_CustName);
        mTextView_CustDOB = findViewById(R.id.textview_CustDOB);
        mTextView_CustPhone = findViewById(R.id.textview_CustPhone);
        mTextView_CustEmailId = findViewById(R.id.textview_CustEmailId);
        mTextView_Address = findViewById(R.id.textview_Address);
        mTextView_Amount = findViewById(R.id.textview_Amount);
        mTextView_Status = findViewById(R.id.textview_Status);
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

        mTextView_ScreenTitle.setText("Payment");

        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        mCustomerData = getIntent().getParcelableExtra("customerData");

        mTextView_CustName.setText(mCustomerData.getM_FirstName() + " " + mCustomerData.getM_LastName());
        mTextView_CustDOB.setText(mCustomerData.getM_DateOfBirth());
        mTextView_CustPhone.setText(mCustomerData.getM_Phone());
        mTextView_CustEmailId.setText(mCustomerData.getM_EmailId());
        mTextView_Address.setText(mCustomerData.getM_Address());
        mTextView_Amount.setText("Rs. 300");
        mTextView_Address.setText("Paid");

    }
}
