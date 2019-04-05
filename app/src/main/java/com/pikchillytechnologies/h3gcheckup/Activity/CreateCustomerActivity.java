package com.pikchillytechnologies.h3gcheckup.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateCustomerActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;
    private VolunteerModel mVolunteerData;
    private H3GHelper mHelper;

    private EditText mEditText_FirstName;
    private EditText mEditText_LastName;
    private EditText mEditText_Phone;
    private Button mButton_Male;
    private Button mButton_Female;
    private LinearLayout mLayout_DateOfBirth;
    private TextView mTextView_DateOfBirth;
    private EditText mEditText_Address;
    private EditText mEditText_City;
    private Spinner mSpinner_State;
    private EditText mEditText_EmailId;

    private Spinner mSpinner_BloodGroup;
    private EditText mEditText_Allergies;
    private Spinner mSpinner_GeneticDisorder;
    private Button mButton_Submit;
    private LinearLayout mLayout_GeneticDisorder;

    // Variables to store user input
    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;
    private String mGender;
    private String mDOB;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mEmailId;
    private String mBloodGroup;
    private String mGeneticDisorder;
    private String mAllergies;
    private boolean checkFlag;

    private DatePicker datePicker;
    private Calendar calendar;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    private List<String> mStatesList;
    private ArrayAdapter<CharSequence> mStateAdapter;

    private List<String> mBloodGroupList;
    private ArrayAdapter<CharSequence> mBloodGroupAdapter;

    private List<String> mGeneticDisorderList;
    private ArrayAdapter<CharSequence> mGeneticDisorderAdapter;

    private CustomerModel mCustomerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        mButton_Male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "Male";
                mButton_Male.setTextColor(getResources().getColor(R.color.colorTextWhite));
                mButton_Male.setBackgroundResource(R.drawable.button_pink_round_s);
                mButton_Female.setTextColor(getResources().getColor(R.color.colorTextBlack));
                mButton_Female.setBackgroundResource(R.drawable.button_flat_round_s);

            }
        });

        mButton_Female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGender = "Female";
                mButton_Female.setTextColor(getResources().getColor(R.color.colorTextWhite));
                mButton_Female.setBackgroundResource(R.drawable.button_pink_round_s);
                mButton_Male.setTextColor(getResources().getColor(R.color.colorTextBlack));
                mButton_Male.setBackgroundResource(R.drawable.button_flat_round_s);
            }
        });

        //Action to perform when Select Date is pressed
        mLayout_DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        // Action to perform when state spinner is selected
        mSpinner_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                String itemSelected = parent.getItemAtPosition(position).toString();
                mState = itemSelected;
                mSpinner_State.setPrompt(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinner_State.setPrompt(getResources().getString(R.string.select_state));
                mState = getResources().getString(R.string.select_state);
            }
        });

        // Action to perform when Blood Group is selected
        mSpinner_BloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String itemSelected = parent.getItemAtPosition(position).toString();
                mBloodGroup = itemSelected;
                mSpinner_BloodGroup.setPrompt(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinner_BloodGroup.setPrompt(getResources().getString(R.string.select_bloodgroup));
                mBloodGroup = getResources().getString(R.string.select_bloodgroup);
            }
        });

        // Action to perform when Genetic Disorder is selected
        mSpinner_GeneticDisorder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String itemSelected = parent.getItemAtPosition(position).toString();
                mGeneticDisorder = itemSelected;
                mSpinner_GeneticDisorder.setPrompt(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinner_GeneticDisorder.setPrompt(getResources().getString(R.string.select_geneticdisorder));
                mGeneticDisorder = getResources().getString(R.string.select_geneticdisorder);
            }
        });

        // Action to perform when Submit button is pressed
        mButton_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if all the required fields are entered
                if (startCheck()) {
                    mCustomerData.setM_FirstName(mFirstName);
                    mCustomerData.setM_LastName(mLastName);
                    mCustomerData.setM_Phone(mPhoneNumber);
                    mCustomerData.setM_Gender(mGender);
                    mCustomerData.setM_DateOfBirth(mDOB);
                    mCustomerData.setM_Address(mAddress);
                    mCustomerData.setM_City(mCity);
                    mCustomerData.setM_State(mState);
                    mCustomerData.setM_EmailId(mEmailId);
                    mCustomerData.setmBloodGroup(mBloodGroup);
                    mCustomerData.setmGeneticDisorder(mGeneticDisorder);
                    mCustomerData.setmAllergies(mAllergies);

                    mHelper.startCustomerActivity(CreateCustomerActivity.this, CustomerOTPActivity.class, mVolunteerData, mCustomerData);

                }

            }
        });

        // Action to perform when Back button is pressed
        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startVolunteerActivity(CreateCustomerActivity.this, DashboardActivity.class, mVolunteerData);
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), CreateCustomerActivity.this);

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
        mEditText_FirstName = findViewById(R.id.edittext_FirstName);
        mEditText_LastName = findViewById(R.id.edittext_LastName);
        mEditText_Phone = findViewById(R.id.edittext_Phone);
        mButton_Male = findViewById(R.id.button_Male);
        mButton_Female = findViewById(R.id.button_Female);
        mLayout_DateOfBirth = findViewById(R.id.layout_DateOfBirth);
        mTextView_DateOfBirth = findViewById(R.id.textview_DateOfBirth);
        mEditText_Address = findViewById(R.id.edittext_Address);
        mEditText_City = findViewById(R.id.edittext_City);
        mSpinner_State = findViewById(R.id.spinner_State);
        mEditText_EmailId = findViewById(R.id.edittext_Email);
        mSpinner_BloodGroup = findViewById(R.id.spinner_BloodGroup);
        mEditText_Allergies = findViewById(R.id.edittext_Allergies);
        mSpinner_GeneticDisorder = findViewById(R.id.spinner_GeneticDisorder);
        mButton_Submit = findViewById(R.id.button_Submit);
        mStatesList = new ArrayList<>();
        calendar = Calendar.getInstance();

        mVolunteerData = new VolunteerModel();
        mCustomerData = new CustomerModel();
        mHelper = new H3GHelper();

    }

    /**
     * Function to Set the variables
     */
    public void setValues() {

        mTextView_ScreenTitle.setText("Customer Detail");
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        checkFlag = false;

        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentMonth = calendar.get(Calendar.MONTH);
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // State Spinner
        mStateAdapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        mStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_State.setAdapter(mStateAdapter);

        // Blood Group Spinner
        mBloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodgroup_array, android.R.layout.simple_spinner_item);
        mBloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_BloodGroup.setAdapter(mBloodGroupAdapter);

        // Genetic Disorder Spinner
        mGeneticDisorderAdapter = ArrayAdapter.createFromResource(this,
                R.array.geneticdisorder_array, android.R.layout.simple_spinner_item);
        mGeneticDisorderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_GeneticDisorder.setAdapter(mGeneticDisorderAdapter);

    }

    /**
     * Function to Set Date
     */
    public void setDate() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String selectedDate = dayOfMonth + "/" + (monthOfYear) + "/" + year;
                        mTextView_DateOfBirth.setText(selectedDate);

                    }
                }, mCurrentYear, mCurrentMonth, mCurrentDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Function to check if the required values are filled by the user
     */
    public boolean startCheck() {

        mFirstName = mEditText_FirstName.getText().toString();
        mLastName = mEditText_LastName.getText().toString();
        mPhoneNumber = mEditText_Phone.getText().toString();
        mDOB = mTextView_DateOfBirth.getText().toString();
        mAddress = mEditText_Address.getText().toString();
        mCity = mEditText_City.getText().toString();
        mEmailId = mEditText_EmailId.getText().toString();
        mAllergies = mEditText_Allergies.getText().toString();

        if (mFirstName.isEmpty()) {
            mEditText_FirstName.setError("Please Enter First Name");
            mEditText_FirstName.setFocusable(true);
            checkFlag = false;
        } else if (mLastName.isEmpty()) {
            mEditText_LastName.setError("Please Enter Last Name");
            mEditText_LastName.setFocusable(true);
            checkFlag = false;
        } else if (mPhoneNumber.isEmpty() || (mPhoneNumber.length() < 10)) {
            mEditText_Phone.setError("Please Enter valid Phone Number");
            mEditText_Phone.setFocusable(true);
            checkFlag = false;
        } else if (mGender.isEmpty()) {
            mHelper.show_Toast(getApplicationContext(), "Please select Gender.", Toast.LENGTH_LONG);
            checkFlag = false;
        } else if (mDOB.equals(getResources().getString(R.string.select_date))) {
            mHelper.show_Toast(getApplicationContext(), "Please select Date Of Birth.", Toast.LENGTH_LONG);
            mTextView_DateOfBirth.setTextColor(Color.RED);
            checkFlag = false;
        } else if (mAddress.isEmpty()) {
            mEditText_Address.setError("Please Enter Address");
            mEditText_Address.setFocusable(true);
            checkFlag = false;
        } else if (mCity.isEmpty()) {
            mEditText_City.setError("Please Enter City");
            mEditText_City.setFocusable(true);
            checkFlag = false;
        } else if (mState.equals(getResources().getString(R.string.select_state))) {
            mHelper.show_Toast(getApplicationContext(), "Please select State", Toast.LENGTH_LONG);
            checkFlag = false;
        } else if (mEmailId.isEmpty() || !mHelper.isValidEmail(mEmailId)) {
            mEditText_EmailId.setError("Please Enter valid Email Id");
            mEditText_EmailId.setFocusable(true);
            checkFlag = false;
        } else {
            mTextView_DateOfBirth.setTextColor(Color.GRAY);
            checkFlag = true;
        }

        return checkFlag;
    }
}
