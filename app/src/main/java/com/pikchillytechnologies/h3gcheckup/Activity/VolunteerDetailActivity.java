package com.pikchillytechnologies.h3gcheckup.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VolunteerDetailActivity extends AppCompatActivity{

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;

    private EditText mEditText_FirstName;
    private EditText mEditText_LastName;
    private EditText mEditText_Phone;
    private Button mButton_Male;
    private Button mButton_Female;
    private LinearLayout mLayout_DateOfBirth;
    private TextView mTextView_DateOfBirth;
    private EditText mEditText_Address;
    private EditText mEditText_City;
    private TextView mTextView_State;
    private Spinner mSpinner_State;
    private EditText mEditText_EmailId;
    private EditText mEditText_PANNumber;
    private Button mButton_Proceed;

    private LinearLayout mLayout_State;

    private Bundle mUserBundle;
    private String mUserPhone;
    private H3GHelper mHelper;
    private boolean checkFlag;

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
    private String mPANNumber;

    private DatePicker datePicker;
    private Calendar calendar;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private VolunteerModel mVolunteerData;

    private List<String> mStatesList;
    private ArrayAdapter<CharSequence> mStateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_detail);

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

        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent destIntent = new Intent(VolunteerDetailActivity.this, SignInActivity.class);
                    startActivity(destIntent);
            }
        });

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

        // Action to perform when Proceed button is pressed
        mButton_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if all the required fields are entered
                if(startCheck()){
                    mVolunteerData.setM_FirstName(mFirstName);
                    mVolunteerData.setM_LastName(mLastName);
                    mVolunteerData.setM_Phone(mPhoneNumber);
                    mVolunteerData.setM_Gender(mGender);
                    mVolunteerData.setM_DateOfBirth(mDOB);
                    mVolunteerData.setM_Address(mAddress);
                    mVolunteerData.setM_City(mCity);
                    mVolunteerData.setM_State(mState);
                    mVolunteerData.setM_EmailId(mEmailId);
                    mVolunteerData.setmPanNumber(mPANNumber);

                    String mVolunteerImageData = mVolunteerData.getM_VolunteerPhotoURL();
                    if(mVolunteerImageData != null && !mVolunteerImageData.isEmpty() && mVolunteerImageData.length() > 1){
                        mVolunteerData.setM_VolunteerPhotoURL(mVolunteerImageData);
                    }else{
                        mVolunteerData.setM_VolunteerPhotoURL("");
                    }

                    mVolunteerData.setM_BankApplicantName("");
                    mVolunteerData.setM_BankName(mFirstName);
                    mVolunteerData.setM_AccountNumber(mFirstName);
                    mVolunteerData.setM_BankIFSCCode(mFirstName);
                    mHelper.startVolunteerActivity(VolunteerDetailActivity.this,VolunteerPhotoActivity.class, mVolunteerData);

                }
            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(),VolunteerDetailActivity.this);

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

        mEditText_FirstName = findViewById(R.id.edittext_FirstName);
        mEditText_LastName = findViewById(R.id.edittext_LastName);
        mEditText_Phone = findViewById(R.id.edittext_Phone);
        mLayout_DateOfBirth = findViewById(R.id.layout_DateOfBirth);
        mTextView_DateOfBirth = findViewById(R.id.textview_DateOfBirth);
        mButton_Male = findViewById(R.id.button_Male);
        mButton_Female = findViewById(R.id.button_Female);
        mEditText_Address = findViewById(R.id.edittext_Address);
        mEditText_City = findViewById(R.id.edittext_City);
        mSpinner_State = findViewById(R.id.spinner_State);
        mEditText_EmailId = findViewById(R.id.edittext_Email);
        mEditText_PANNumber= findViewById(R.id.edittext_PanNumber);
        mButton_Proceed = findViewById(R.id.button_Proceed);
        mHelper = new H3GHelper();
        mStatesList = new ArrayList<>();
        calendar = Calendar.getInstance();
        mLayout_State = findViewById(R.id.layout_State);
        mVolunteerData = new VolunteerModel();

    }

    /**
     * Function to Set Values
     * */
    public void setValues(){

        mTextView_ScreenTitle.setText("Basic Information");
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");

        checkFlag = false;

        // State Spinner
        mStateAdapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        mStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_State.setAdapter(mStateAdapter);

        try{
            mUserBundle = getIntent().getExtras();
            mUserPhone = mUserBundle != null ? mUserBundle.getString("phone","") : "NA";
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }

        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentMonth = calendar.get(Calendar.MONTH);
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        mEditText_FirstName.setText(mVolunteerData.getM_FirstName());
        mEditText_LastName.setText(mVolunteerData.getM_LastName());
        mEditText_Phone.setText(mVolunteerData.getM_Phone());

        if(mVolunteerData.getM_DateOfBirth().equals("")){
            mTextView_DateOfBirth.setText(getResources().getString(R.string.select_date));
        }else{
            mTextView_DateOfBirth.setText(mVolunteerData.getM_DateOfBirth());
        }

        mGender = mVolunteerData.getM_Gender();

        if(mGender.equals("Male")){

            mButton_Male.setTextColor(getResources().getColor(R.color.colorTextWhite));
            mButton_Male.setBackgroundResource(R.drawable.button_pink_round_s);
        }else if(mGender.equals("Female")){
            mButton_Female.setTextColor(getResources().getColor(R.color.colorTextWhite));
            mButton_Female.setBackgroundResource(R.drawable.button_pink_round_s);
        }

        mState = mVolunteerData.getM_State();
        mSpinner_State.setSelection(getIndex(mSpinner_State, mState));

        mSpinner_State.setPrompt(mState);
        mEditText_Address.setText(mVolunteerData.getM_Address());
        mEditText_City.setText(mVolunteerData.getM_City());
        mEditText_EmailId.setText(mVolunteerData.getM_EmailId());
        mEditText_PANNumber.setText(mVolunteerData.getmPanNumber());

    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    /**
     * Function to Set Date
     * */
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
     * */
    public boolean startCheck(){

        mFirstName = mEditText_FirstName.getText().toString();
        mLastName = mEditText_LastName.getText().toString();
        mPhoneNumber = mEditText_Phone.getText().toString();
        mDOB = mTextView_DateOfBirth.getText().toString();
        mAddress = mEditText_Address.getText().toString();
        mCity = mEditText_City.getText().toString();
        mEmailId = mEditText_EmailId.getText().toString();
        mPANNumber = mEditText_PANNumber.getText().toString();

        if(mFirstName.isEmpty()){
            mEditText_FirstName.setError("Please Enter First Name");
            mEditText_FirstName.setFocusable(true);
            checkFlag = false;
        }else if(mLastName.isEmpty()){
            mEditText_LastName.setError("Please Enter Last Name");
            mEditText_LastName.setFocusable(true);
            checkFlag = false;
        }else if(mPhoneNumber.isEmpty() || (mPhoneNumber.length() < 10)){
            mEditText_Phone.setError("Please Enter valid Phone Number");
            mEditText_Phone.setFocusable(true);
            checkFlag = false;
        }else if(mGender.isEmpty()){
            mHelper.show_Toast(getApplicationContext(),"Please select Gender.",Toast.LENGTH_LONG);
            checkFlag = false;
        }else if(mDOB.equals(getResources().getString(R.string.select_date))){
            mHelper.show_Toast(getApplicationContext(),"Please select Date Of Birth.",Toast.LENGTH_LONG);
            mTextView_DateOfBirth.setTextColor(Color.RED);
            checkFlag = false;
        }else if(mAddress.isEmpty()){
            mEditText_Address.setError("Please Enter Address");
            mEditText_Address.setFocusable(true);
            checkFlag = false;
        }else if(mCity.isEmpty()){
            mEditText_City.setError("Please Enter City");
            mEditText_City.setFocusable(true);
            checkFlag = false;
        }else if(mState.equals(getResources().getString(R.string.select_state))){
            mHelper.show_Toast(getApplicationContext(),"Please select State",Toast.LENGTH_LONG);
            checkFlag = false;
        }else if(mEmailId.isEmpty() || !mHelper.isValidEmail(mEmailId)){
            mEditText_EmailId.setError("Please Enter valid Email Id");
            mEditText_EmailId.setFocusable(true);
            checkFlag = false;
        } else{
            mTextView_DateOfBirth.setTextColor(Color.GRAY);
            checkFlag = true;
        }

        return checkFlag;
    }


}
