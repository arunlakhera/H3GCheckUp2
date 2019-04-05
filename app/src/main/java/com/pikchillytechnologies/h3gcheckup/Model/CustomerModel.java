package com.pikchillytechnologies.h3gcheckup.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerModel implements Parcelable {

    private String m_FirstName;
    private String m_LastName;
    private String m_Phone;
    private String m_Gender;
    private String m_DateOfBirth;
    private String m_Address;
    private String m_City;
    private String m_State;
    private String m_EmailId;
    private String mBloodGroup;
    private String mAllergies;
    private String mGeneticDisorder;
    private String mOTP;
    private String mCustomerActiveFlag;

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
        dest.writeString(mBloodGroup);
        dest.writeString(mAllergies);
        dest.writeString(mGeneticDisorder);
        dest.writeString(mOTP);
        dest.writeString(mCustomerActiveFlag);
    }

    private CustomerModel(Parcel source) {
        m_FirstName = source.readString();
        m_LastName = source.readString();
        m_Phone = source.readString();
        m_Gender = source.readString();
        m_DateOfBirth = source.readString();
        m_Address = source.readString();
        m_City = source.readString();
        m_State = source.readString();
        m_EmailId = source.readString();
        mBloodGroup = source.readString();
        mAllergies = source.readString();
        mGeneticDisorder = source.readString();
        mOTP = source.readString();
        mCustomerActiveFlag = source.readString();
    }

    public CustomerModel() {

        this.m_FirstName = "";
        this.m_LastName = "";
        this.m_Phone = "";
        this.m_Gender = "";
        this.m_DateOfBirth = "";
        this.m_Address = "";
        this.m_City = "";
        this.m_State = "";
        this.m_EmailId = "";
        this.mBloodGroup = "";
        this.mAllergies = "";
        this.mGeneticDisorder = "";
        this.mCustomerActiveFlag = "";

    }

    public static final Parcelable.Creator<CustomerModel> CREATOR = new Parcelable.Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel source) {
            return new CustomerModel(source);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
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

    public String getmBloodGroup() {
        return mBloodGroup;
    }

    public void setmBloodGroup(String mBloodGroup) {
        this.mBloodGroup = mBloodGroup;
    }

    public String getmAllergies() {
        return mAllergies;
    }

    public void setmAllergies(String mAllergies) {
        this.mAllergies = mAllergies;
    }

    public String getmGeneticDisorder() {
        return mGeneticDisorder;
    }

    public void setmGeneticDisorder(String mGeneticDisorder) {
        this.mGeneticDisorder = mGeneticDisorder;
    }

    public String getmOTP() {
        return mOTP;
    }

    public void setmOTP(String mOTP) {
        this.mOTP = mOTP;
    }

    public String getmCustomerActiveFlag() {
        return mCustomerActiveFlag;
    }

    public void setmCustomerActiveFlag(String mCustomerActiveFlag) {
        this.mCustomerActiveFlag = mCustomerActiveFlag;
    }
}