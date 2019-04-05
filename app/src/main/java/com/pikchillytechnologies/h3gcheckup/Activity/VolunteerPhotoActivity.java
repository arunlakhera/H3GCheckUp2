package com.pikchillytechnologies.h3gcheckup.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class VolunteerPhotoActivity extends AppCompatActivity {

    private TextView mTextView_ScreenTitle;
    private Button mButton_Back;
    private Button mButton_Contact;

    private ImageView mImageView_UserPhoto;
    private Button mButton_Camera;
    private Button mButton_Proceed;
    private H3GHelper mHelper;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri filePath;
    private Bitmap bitmap;
    private VolunteerModel mVolunteerData;
    private String mUserUpdatedImage;
    Bitmap lastBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_photo);

        // Initialize
        startInitialize();

        // Set Values
        setValues();

        // Action to perform when Camera button is clicked
        mButton_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUserPhoto();
            }
        });

        // Action to perform when Proceed Button is clicked
        mButton_Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUserUpdatedImage != null && !mUserUpdatedImage.isEmpty() && mUserUpdatedImage.length() > 1) {

                    //encoding image to string
                    mVolunteerData.setM_VolunteerPhotoURL(mUserUpdatedImage);
                    mHelper.startVolunteerActivity(VolunteerPhotoActivity.this, VolunteerBankDetailActivity.class, mVolunteerData);

                } else {

                    mHelper.show_Toast(getApplicationContext(), "Please Upload your photo.", Toast.LENGTH_SHORT);
                }

            }
        });

        // Action to perform when Back button is clicked
        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVolunteerData.setM_VolunteerPhotoURL(mUserUpdatedImage);
                mHelper.startVolunteerActivity(VolunteerPhotoActivity.this, VolunteerDetailActivity.class, mVolunteerData);

            }
        });

        mButton_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper.checkPermission(getApplicationContext(), VolunteerPhotoActivity.this);

            }
        });
    }

    /**
     * Function to set Values
     */
    public void setValues() {

        mVolunteerData = getIntent().getParcelableExtra("volunteerData");
        mTextView_ScreenTitle.setText("Selfie");
        mUserUpdatedImage = mVolunteerData.getM_VolunteerPhotoURL();

        setUserImage();

    }

    /**
     * Function to initialize the variables
     */
    public void startInitialize() {

        mTextView_ScreenTitle = findViewById(R.id.textview_Title);
        mButton_Back = findViewById(R.id.button_Back);
        mButton_Contact = findViewById(R.id.button_Contact);
        mImageView_UserPhoto = findViewById(R.id.imageview_UserPhoto);
        mButton_Proceed = findViewById(R.id.button_Proceed);
        mButton_Camera = findViewById(R.id.button_Camera);
        mHelper = new H3GHelper();
        mVolunteerData = new VolunteerModel();
    }

    /**
     * Function to show Camera to user
     */
    public void updateUserPhoto() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Function to Set photo taken by user from camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            try {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView_UserPhoto.setImageBitmap(imageBitmap);

                Bitmap lastBitmap = null;
                lastBitmap = imageBitmap;

                //encoding image to string
                mUserUpdatedImage = getStringImage(lastBitmap);

            } catch (Exception e) {
                Log.e("Camera Error:", e.getMessage());
            }
        }
    }

    /**
     * Function to Encode Image to String
     */
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedUserImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedUserImage;
    }

    /**
     * Function to Decode Image From String
     */
    public void setUserImage() {

        if (mUserUpdatedImage != null && !mUserUpdatedImage.isEmpty() && mUserUpdatedImage.length() > 1) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            imageBytes = Base64.decode(mUserUpdatedImage, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            mImageView_UserPhoto.setImageBitmap(decodedImage);
        }
    }
}
