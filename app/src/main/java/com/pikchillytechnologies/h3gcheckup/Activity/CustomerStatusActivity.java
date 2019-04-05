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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pikchillytechnologies.h3gcheckup.Adapter.CustomerStatusAdapter;
import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerStatusActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;
    private VolunteerModel mVolunteerData;
    private CustomerModel mCustomerData;
    private H3GHelper mHelper;

    private RecyclerView mRecyclerview_CustomerList;
    private List<CustomerModel> m_Customers_List;
    private CustomerStatusAdapter m_Customer_Adapter;
    private RecyclerView.LayoutManager m_Layout_Manager;

    private Bundle mBundle_Status;
    private String mCustomerStatus;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_status);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        // Check customer status
        getCustomerData();

        // Action to perform when Back button is pressed
        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startVolunteerActivity(CustomerStatusActivity.this, DashboardActivity.class, mVolunteerData);
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), CustomerStatusActivity.this);

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
        mCustomerData = new CustomerModel();
        mHelper = new H3GHelper();

        m_Customers_List = new ArrayList<>();
        mRecyclerview_CustomerList = findViewById(R.id.recyclerview_CustomerList);
        m_Customer_Adapter = new CustomerStatusAdapter(getApplicationContext(), m_Customers_List);
    }

    /**
     * Function to Set the variables
     */
    public void setValues() {

        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        mBundle_Status = getIntent().getExtras();
        mCustomerStatus = mBundle_Status.getString("status");
        pd = new ProgressDialog(CustomerStatusActivity.this);
        mTextView_ScreenTitle.setText(mCustomerStatus);

        m_Layout_Manager = new LinearLayoutManager(getApplicationContext());
        mRecyclerview_CustomerList.setHasFixedSize(true);
        mRecyclerview_CustomerList.setLayoutManager(m_Layout_Manager);
        mRecyclerview_CustomerList.setAdapter(m_Customer_Adapter);

    }

    /**
     * Function to check the type os status requested
     */
    public void getCustomerData() {

        pd.setMessage("Getting Data . . .");
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(CustomerStatusActivity.this);
        String response = null;

        StringRequest postRequest = new StringRequest(Request.Method.POST, mHelper.GET_CUSTOMER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("No_Data_Found")) {

                            Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_LONG).show();
                        } else {

                            try {

                                JSONObject customerJSON = new JSONObject(response);
                                JSONArray customerArray = customerJSON.getJSONArray("volunteer_customer");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < customerArray.length(); i++) {

                                    //getting the json object of the particular index inside the array
                                    JSONObject customerObject = customerArray.getJSONObject(i);

                                    if (mCustomerStatus.equals("All")) {

                                        CustomerModel customer = new CustomerModel();

                                        customer.setM_FirstName(customerObject.getString("first_name"));
                                        customer.setM_LastName(customerObject.getString("last_name"));
                                        customer.setM_Phone(customerObject.getString("phone"));
                                        customer.setM_Gender(customerObject.getString("gender"));
                                        customer.setM_DateOfBirth(customerObject.getString("date_of_birth"));
                                        customer.setM_Address(customerObject.getString("address"));
                                        customer.setM_City(customerObject.getString("city"));
                                        customer.setM_State(customerObject.getString("state"));
                                        customer.setM_EmailId(customerObject.getString("email_id"));
                                        customer.setmBloodGroup(customerObject.getString("blood_group"));
                                        customer.setmAllergies(customerObject.getString("allergies"));
                                        customer.setmGeneticDisorder(customerObject.getString("genetic_disorder"));
                                        customer.setmCustomerActiveFlag(customerObject.getString("customer_active_flag"));
                                        m_Customers_List.add(customer);
                                    } else if (customerObject.getString("customer_active_flag").equals(mCustomerStatus)) {

                                        //creating a tutorial object and giving them the values from json object
                                        CustomerModel customer = new CustomerModel();

                                        customer.setM_FirstName(customerObject.getString("first_name"));
                                        customer.setM_LastName(customerObject.getString("last_name"));
                                        customer.setM_Phone(customerObject.getString("phone"));
                                        customer.setM_Gender(customerObject.getString("gender"));
                                        customer.setM_DateOfBirth(customerObject.getString("date_of_birth"));
                                        customer.setM_Address(customerObject.getString("address"));
                                        customer.setM_City(customerObject.getString("city"));
                                        customer.setM_State(customerObject.getString("state"));
                                        customer.setM_EmailId(customerObject.getString("email_id"));
                                        customer.setmBloodGroup(customerObject.getString("blood_group"));
                                        customer.setmAllergies(customerObject.getString("allergies"));
                                        customer.setmGeneticDisorder(customerObject.getString("genetic_disorder"));
                                        customer.setmCustomerActiveFlag(customerObject.getString("customer_active_flag"));
                                        m_Customers_List.add(customer);
                                    }

                                }

                                m_Customer_Adapter.notifyDataSetChanged();
                                pd.dismiss();

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


/*
        if(mCustomerStatus.equals("In Progress")){
            mHelper.show_Toast(getApplicationContext(), "In Progress", Toast.LENGTH_SHORT);

        }else if (mCustomerStatus.equals("Card Issued")){
            mHelper.show_Toast(getApplicationContext(), "Card Issued", Toast.LENGTH_SHORT);
        }
*/
    }

}
