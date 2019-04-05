package com.pikchillytechnologies.h3gcheckup.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pikchillytechnologies.h3gcheckup.Helper.H3GHelper;
import com.pikchillytechnologies.h3gcheckup.Model.CustomerModel;
import com.pikchillytechnologies.h3gcheckup.Model.MyReferralModel;
import com.pikchillytechnologies.h3gcheckup.R;

import java.util.List;

public class MyReferralCodesAdapter extends RecyclerView.Adapter<MyReferralCodesAdapter.MyViewHolder>{

    private List<MyReferralModel> m_MyReferral_List;
    private Context mContext;
    private H3GHelper mHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView_ReferralCode;
        private TextView mTextView_TotalUsesLeft;
        private Button mButton_Share;

        public MyViewHolder(View view){
            super(view);

            mTextView_ReferralCode = view.findViewById(R.id.textview_ReferralCode);
            mTextView_TotalUsesLeft = view.findViewById(R.id.textview_TotalUses);
            mButton_Share = view.findViewById(R.id.button_Share);
            mHelper = new H3GHelper();
        }
    }

    public MyReferralCodesAdapter(Context context, List<MyReferralModel> referralList){

        this.mContext = context;
        this.m_MyReferral_List = referralList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_referral_codes_listview,parent,false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final MyReferralModel myReferral = m_MyReferral_List.get(position);

        final String referral_code = myReferral.getmReferralCode();
        String uses_left = myReferral.getmTotalUses();

        holder.mTextView_ReferralCode.setText(referral_code);
        holder.mTextView_TotalUsesLeft.setText(uses_left);

        holder.mButton_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,referral_code,Toast.LENGTH_SHORT).show();
                mHelper.shareReferralCode(mContext,referral_code);

            }
        });

    }

    @Override
    public int getItemCount() {
        return m_MyReferral_List.size();
    }
}
