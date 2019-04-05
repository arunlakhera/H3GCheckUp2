package com.pikchillytechnologies.h3gcheckup.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import com.pikchillytechnologies.h3gcheckup.Activity.CreateCustomerActivity;
import com.pikchillytechnologies.h3gcheckup.Activity.CustomerOTPActivity;
import com.pikchillytechnologies.h3gcheckup.Activity.DashboardActivity;
import com.pikchillytechnologies.h3gcheckup.Activity.MyEarningsActivity;
import com.pikchillytechnologies.h3gcheckup.Activity.MyReferralCodesActivity;
import com.pikchillytechnologies.h3gcheckup.Activity.ReferralCodeActivity;
import com.pikchillytechnologies.h3gcheckup.Activity.SignInActivity;
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.VolunteerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class H3GHelper {

    private Context m_Context;

    public String CREATE_REFERRAL_URL = "https://pikchilly.com/h3g_api/create_referral_code.php";
    public String CREATE_CUSTOMER_URL = "https://pikchilly.com/h3g_api/create_customer.php";
    public String CREATE_VOL_LOGIN_URL = "https://pikchilly.com/h3g_api/login.php";
    public String CREATE_VOLUNTEER_URL = "https://pikchilly.com/h3g_api/create_volunteer.php";
    public String UPDATE_VOLUNTEER_URL = "https://pikchilly.com/h3g_api/update_volunteer.php";
    public String GET_VOLUNTEER_URL = "https://pikchilly.com/h3g_api/login_otp.php";
    public String GET_CUSTOMER_URL = "https://pikchilly.com/h3g_api/get_customers.php";
    public String GET_MY_REFERRALCODES_URL = "https://pikchilly.com/h3g_api/get_my_referralcodes.php";

    private String MY_APP_LINK = "https://play.google.com/store/apps/details?id=com.pikchillytechnologies.h3gcheckup";

    private int MY_PERMISSIONS_REQUEST_CALL= 1;

    /**
     * Function to Start Volunteer Intent
     */
    public void startVolunteerActivity(Context context, Class destClass){
        this.m_Context = context;
        Intent destDetailIntent = new Intent(m_Context, destClass);
        this.m_Context.startActivity(destDetailIntent);
    }

    /**
     * Function to Start Volunteer Intent
     */
    public void startVolunteerActivity(Context context, Class destClass, VolunteerModel volunteerData){
        this.m_Context = context;
        Intent destDetailIntent = new Intent(m_Context, destClass);
        destDetailIntent.putExtra("volunteerData",volunteerData);
        this.m_Context.startActivity(destDetailIntent);
    }

    /**
     * Function to Start Volunteer Intent
     */
    public void startVolunteerActivity(Context context, Class destClass, VolunteerModel volunteerData, String status){
        this.m_Context = context;
        Intent destDetailIntent = new Intent(m_Context, destClass);
        destDetailIntent.putExtra("volunteerData",volunteerData);
        destDetailIntent.putExtra("status",status);
        this.m_Context.startActivity(destDetailIntent);
    }

    /**
     * Function to Start Customer Intent
     */
    public void startCustomerActivity(Context context, Class destClass, VolunteerModel volunteerData, CustomerModel customerData){
        this.m_Context = context;
        Intent destDetailIntent = new Intent(m_Context, destClass);
        destDetailIntent.putExtra("volunteerData",volunteerData);
        destDetailIntent.putExtra("customerData",customerData);
        this.m_Context.startActivity(destDetailIntent);
    }


    /**
     * Function to Show Toast Message
     * */
    public void show_Toast(Context context, String msg, int duration){
        this.m_Context = context;
        Toast.makeText(m_Context,msg,duration).show();
    }

    /**
     * Function to Check valid email
     * */
    public boolean isValidEmail(String emaildId){

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emaildId);

        return matcher.matches();
    }

    /**
     * Function to share app link
     * */
    public void shareApp(Context context){

        this.m_Context = context;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "H3G Check Up");
        String message = "\nLet me recommend you this application " + MY_APP_LINK;

        i.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(i, "Choose one"));
    }

    /**
     * Function to Check Permission
     * */
    public void checkPermission(Context context,Activity activity){

        this.m_Context = context;

        if (ContextCompat.checkSelfPermission(m_Context, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + m_Context.getResources().getString(R.string.contact_phone)));
            context.startActivity(intent);

        }else if (ContextCompat.checkSelfPermission(m_Context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL);
        }
    }

    /**
     * Function to share referral code
     */
    public void shareReferralCode(Context context, String referralcode) {
        this.m_Context = context;

        String msgBody = "You can use the referral code " + referralcode + " in H3G Check Up affiliated Diagnostic Center's to get additional Discount.";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msgBody);
        sendIntent.setType("text/plain");
        m_Context.startActivity(Intent.createChooser(sendIntent, "TEST"));

    }


}
