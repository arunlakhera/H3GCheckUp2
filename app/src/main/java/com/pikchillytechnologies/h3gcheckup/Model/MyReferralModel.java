package com.pikchillytechnologies.h3gcheckup.Model;

public class MyReferralModel {

    private String mReferralCode;
    private String mVolunteerPhone;
    private String mTotalUses;
    private String mActiveFlag;

    public MyReferralModel(String referralcode, String volunteerphone, String totaluses, String activeflag){
        this.mReferralCode = referralcode;
        this.mVolunteerPhone = volunteerphone;
        this.mTotalUses = totaluses;
        this.mActiveFlag = activeflag;
    }

    public String getmReferralCode() {
        return mReferralCode;
    }

    public void setmReferralCode(String mReferralCode) {
        this.mReferralCode = mReferralCode;
    }

    public String getmVolunteerPhone() {
        return mVolunteerPhone;
    }

    public void setmVolunteerPhone(String mVolunteerPhone) {
        this.mVolunteerPhone = mVolunteerPhone;
    }

    public String getmTotalUses() {
        return mTotalUses;
    }

    public void setmTotalUses(String mTotalUses) {
        this.mTotalUses = mTotalUses;
    }

    public String getmActiveFlag() {
        return mActiveFlag;
    }

    public void setmActiveFlag(String mActiveFlag) {
        this.mActiveFlag = mActiveFlag;
    }
}
