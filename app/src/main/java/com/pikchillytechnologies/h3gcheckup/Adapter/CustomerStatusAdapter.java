package com.pikchillytechnologies.h3gcheckup.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.List;

public class CustomerStatusAdapter extends RecyclerView.Adapter<CustomerStatusAdapter.MyViewHolder>{

    private List<CustomerModel> m_Customers_List;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView_CustomerName;
        private TextView mTextView_CustomerDOB;
        private TextView mTextView_CustomerPhone;
        private TextView mTextView_CustomerEmail;
        private TextView mTextView_CustomerStatus;

        public MyViewHolder(View view){
            super(view);

            mTextView_CustomerName = view.findViewById(R.id.textview_CustomerName);
            mTextView_CustomerDOB = view.findViewById(R.id.textview_CustomerDOB);
            mTextView_CustomerPhone = view.findViewById(R.id.textview_CustomerPhone);
            mTextView_CustomerEmail = view.findViewById(R.id.textview_CustomerEmailId);
            mTextView_CustomerStatus = view.findViewById(R.id.textview_CustomerStatus);
        }
    }

    public CustomerStatusAdapter(Context context, List<CustomerModel> customersList){

        this.mContext = context;
        this.m_Customers_List = customersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_status_listview,parent,false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CustomerModel customer = m_Customers_List.get(position);

        String cust_Name = customer.getM_FirstName() + " " + customer.getM_LastName();
        String cust_DOB = customer.getM_DateOfBirth();
        String cust_Phone = customer.getM_Phone();
        String cust_Email = customer.getM_EmailId();
        String cust_Status = customer.getmCustomerActiveFlag();

        holder.mTextView_CustomerName.setText(cust_Name);
        holder.mTextView_CustomerDOB.setText(cust_DOB);
        holder.mTextView_CustomerPhone.setText(cust_Phone);
        holder.mTextView_CustomerEmail.setText(cust_Email);
        holder.mTextView_CustomerStatus.setText(cust_Status);

    }

    @Override
    public int getItemCount() {
        return m_Customers_List.size();
    }
}
