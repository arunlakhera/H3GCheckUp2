package com.pikchillytechnologies.h3gcheckup.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolunteerProfileActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;
    private H3GHelper mHelper;
    private VolunteerModel mVolunteerData;
    private EditText mEditText_FirstName;
    private EditText mEditText_LastName;
    private EditText mEditText_Phone;
    private EditText mEditText_Gender;
    private EditText mEditText_DateOfBirth;

    private LinearLayout mLayout_DateOfBirth;
    private EditText mEditText_Address;
    private EditText mEditText_City;
    private EditText mEditText_State;
    private EditText mEditText_EmailId;
    private EditText mEditText_PANNumber;
    private EditText mEditText_ApplicantName;
    private EditText mEditText_BankName;
    private EditText mEditText_AccountNumber;
    private EditText mEditText_IFSCCode;
    private ImageView mImageView_UserPhoto;

    // Variables to store user input
    private String mUserPhotoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        // Action to perform when back button is pressed
        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.startVolunteerActivity(getApplicationContext(), DashboardActivity.class, mVolunteerData);
            }
        });

        // Action to perform when Contact button is pressed
        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), VolunteerProfileActivity.this);

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
        mHelper = new H3GHelper();
        mVolunteerData = new VolunteerModel();

        mEditText_FirstName = findViewById(R.id.edittext_FirstName);
        mEditText_LastName = findViewById(R.id.edittext_LastName);
        mEditText_Phone = findViewById(R.id.edittext_Phone);
        mEditText_Gender = findViewById(R.id.edittext_Gender);
        mEditText_DateOfBirth = findViewById(R.id.edittext_DOB);
        mLayout_DateOfBirth = findViewById(R.id.layout_DateOfBirth);
        mEditText_Address = findViewById(R.id.edittext_Address);
        mEditText_City = findViewById(R.id.edittext_City);
        mEditText_State = findViewById(R.id.edittext_State);

        mEditText_EmailId = findViewById(R.id.edittext_Email);
        mEditText_PANNumber = findViewById(R.id.edittext_PanNumber);
        mEditText_ApplicantName = findViewById(R.id.edittext_ApplicantName);
        mEditText_BankName = findViewById(R.id.edittext_BankName);
        mEditText_AccountNumber = findViewById(R.id.edittext_AccountNumber);

        mEditText_IFSCCode = findViewById(R.id.edittext_IFSCCode);
        mImageView_UserPhoto = findViewById(R.id.imageView_UserPhoto);

    }

    /**
     * Function to Set Values
     */
    public void setValues() {

        mTextView_ScreenTitle.setText("My Profile");
        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        mUserPhotoURL = mVolunteerData.getM_VolunteerPhotoURL();
        mEditText_FirstName.setText(mVolunteerData.getM_FirstName());
        mEditText_LastName.setText(mVolunteerData.getM_LastName());
        mEditText_Phone.setText(mVolunteerData.getM_Phone());
        mEditText_Gender.setText(mVolunteerData.getM_Gender());
        mEditText_DateOfBirth.setText(mVolunteerData.getM_DateOfBirth());
        mEditText_Address.setText(mVolunteerData.getM_Address());
        mEditText_City.setText(mVolunteerData.getM_City());
        mEditText_State.setText(mVolunteerData.getM_State());
        mEditText_EmailId.setText(mVolunteerData.getM_EmailId());
        mEditText_PANNumber.setText(mVolunteerData.getmPanNumber());
        mEditText_ApplicantName.setText(mVolunteerData.getM_BankApplicantName());
        mEditText_BankName.setText(mVolunteerData.getM_BankName());
        mEditText_AccountNumber.setText(mVolunteerData.getM_AccountNumber());
        mEditText_IFSCCode.setText(mVolunteerData.getM_BankIFSCCode());

        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);

        try {
            Glide.with(this)
                    .load(mUserPhotoURL)
                    .apply(requestOptions)
                    .placeholder(R.drawable.h3g_icon)
                    .error(R.drawable.h3g_app_icon)
                    .into(mImageView_UserPhoto);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Could not Load image.." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
