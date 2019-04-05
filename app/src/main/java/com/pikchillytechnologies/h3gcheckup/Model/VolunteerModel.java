package com.pikchillytechnologies.h3gcheckup.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class VolunteerModel implements Parcelable {

    private String m_FirstName;
    private String m_LastName;
    private String m_Phone;
    private String m_Gender;
    private String m_DateOfBirth;
    private String m_Address;
    private String m_City;
    private String m_State;
    private String m_EmailId;
    private String m_VolunteerPhotoURL;
    private String m_BankApplicantName;
    private String m_BankName;
    private String m_AccountNumber;
    private String m_BankIFSCCode;
    private String mOTP;
    private String mPanNumber;
    private String mUserActiveFlag;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_FirstName);
        dest.writeString(m_LastName);
        dest.writeString(m_Phone);
        dest.writeString(m_Gender);
        dest.writeString(m_DateOfBirth);
        dest.writeString(m_Address);
        dest.writeString(m_City);
        dest.writeString(m_State);
        dest.writeString(m_EmailId);
        dest.writeString(m_VolunteerPhotoURL);
        dest.writeString(m_BankApplicantName);
        dest.writeString(m_BankName);
        dest.writeString(m_AccountNumber);
        dest.writeString(m_BankIFSCCode);
        dest.writeString(mOTP);
        dest.writeString(mPanNumber);
        dest.writeString(mUserActiveFlag);

    }

    private VolunteerModel(Parcel source){
        m_FirstName = source.readString();
        m_LastName = source.readString();
        m_Phone = source.readString();
        m_Gender = source.readString();
        m_DateOfBirth = source.readString();
        m_Address = source.readString();
        m_City = source.readString();
        m_State = source.readString();
        m_EmailId = source.readString();
        m_VolunteerPhotoURL = source.readString();
        m_BankApplicantName = source.readString();
        m_BankName = source.readString();
        m_AccountNumber = source.readString();
        m_BankIFSCCode = source.readString();
        mOTP = source.readString();
        mPanNumber = source.readString();
        mUserActiveFlag = source.readString();
    }

    public VolunteerModel(){

        this.m_FirstName = "";
        this.m_LastName = "";
        this.m_Phone = "";
        this.m_Gender = "";
        this.m_DateOfBirth = "";
        this.m_Address = "";
        this.m_City = "";
        this.m_State = "";
        this.m_EmailId = "";
        this.m_VolunteerPhotoURL = "";
        this.m_BankApplicantName = "";
        this.m_BankName = "";
        this.m_AccountNumber= "";
        this.m_BankIFSCCode = "";
        this.mOTP = "";
        this.mPanNumber = "";
        this.mUserActiveFlag = "";

    }

    public static final Parcelable.Creator<VolunteerModel> CREATOR = new Parcelable.Creator<VolunteerModel>(){
        @Override
        public VolunteerModel createFromParcel(Parcel source) {
            return new VolunteerModel(source);
        }

        @Override
        public VolunteerModel[] newArray(int size) {
            return new VolunteerModel[size];
        }
    };


    public String getM_FirstName() {
        return m_FirstName;
    }

    public void setM_FirstName(String m_FirstName) {
        this.m_FirstName = m_FirstName;
    }

    public String getM_LastName() {
        return m_LastName;
    }

    public void setM_LastName(String m_LastName) {
        this.m_LastName = m_LastName;
    }

    public String getM_Phone() {
        return m_Phone;
    }

    public void setM_Phone(String m_Phone) {
        this.m_Phone = m_Phone;
    }

    public String getM_Gender() {
        return m_Gender;
    }

    public void setM_Gender(String m_Gender) {
        this.m_Gender = m_Gender;
    }

    public String getM_DateOfBirth() {
        return m_DateOfBirth;
    }

    public void setM_DateOfBirth(String m_DateOfBirth) {
        this.m_DateOfBirth = m_DateOfBirth;
    }

    public String getM_Address() {
        return m_Address;
    }

    public void setM_Address(String m_Address) {
        this.m_Address = m_Address;
    }

    public String getM_City() {
        return m_City;
    }

    public void setM_City(String m_City) {
        this.m_City = m_City;
    }

    public String getM_State() {
        return m_State;
    }

    public void setM_State(String m_State) {
        this.m_State = m_State;
    }

    public String getM_EmailId() {
        return m_EmailId;
    }

    public void setM_EmailId(String m_EmailId) {
        this.m_EmailId = m_EmailId;
    }

    public String getM_VolunteerPhotoURL() {
        return m_VolunteerPhotoURL;
    }

    public void setM_VolunteerPhotoURL(String m_VolunteerPhotoURL) {
        this.m_VolunteerPhotoURL = m_VolunteerPhotoURL;
    }

    public String getM_BankApplicantName() {
        return m_BankApplicantName;
    }

    public void setM_BankApplicantName(String m_BankApplicantName) {
        this.m_BankApplicantName = m_BankApplicantName;
    }

    public String getM_BankName() {
        return m_BankName;
    }

    public void setM_BankName(String m_BankName) {
        this.m_BankName = m_BankName;
    }

    public String getM_AccountNumber() {
        return m_AccountNumber;
    }

    public void setM_AccountNumber(String m_AccountNumber) {
        this.m_AccountNumber = m_AccountNumber;
    }

    public String getM_BankIFSCCode() {
        return m_BankIFSCCode;
    }

    public void setM_BankIFSCCode(String m_BankIFSCCode) {
        this.m_BankIFSCCode = m_BankIFSCCode;
    }

    public String getmOTP() {
        return mOTP;
    }

    public void setmOTP(String mOTP) {
        this.mOTP = mOTP;
    }

    public String getmUserActiveFlag() {
        return mUserActiveFlag;
    }

    public void setmUserActiveFlag(String mUserActiveFlag) {
        this.mUserActiveFlag = mUserActiveFlag;
    }

    public String getmPanNumber() {
        return mPanNumber;
    }

    public void setmPanNumber(String mPanNumber) {
        this.mPanNumber = mPanNumber;
    }
}
