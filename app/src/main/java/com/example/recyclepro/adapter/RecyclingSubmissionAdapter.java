package com.example.recyclepro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.activities.Customer.DetailRecycleSubmissionHistorySide;
import com.example.recyclepro.models.DynamoDBManager;
import com.example.recyclepro.models.RecyclingSubmission;

import java.util.List;

public class RecyclingSubmissionAdapter extends RecyclerView.Adapter<RecyclingSubmissionAdapter.RecyclingSubmissionListViewHolder>{
    private List<RecyclingSubmission> listRecyclingSubmission;
    private RecyclingSubmissionAdapter.OnItemClickListener mListener;
    private DynamoDBManager dynamoDBManager;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(String id, String productName, String time, String state);
    }
    public void setOnItemClickListener(RecyclingSubmissionAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    public RecyclingSubmissionAdapter(List<RecyclingSubmission> listRecyclingSubmission, DynamoDBManager dynamoDBManager, Context context) {
        this.listRecyclingSubmission=listRecyclingSubmission;
        this.dynamoDBManager=dynamoDBManager;
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclingSubmissionAdapter.RecyclingSubmissionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycling_submission, parent, false);
        return new RecyclingSubmissionAdapter.RecyclingSubmissionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclingSubmissionAdapter.RecyclingSubmissionListViewHolder holder, int position) {
        RecyclingSubmission recyclingSubmission =listRecyclingSubmission.get(position);
        if(recyclingSubmission==null){
            return;
        }
        holder.tvProductName.setText("Product name:"+recyclingSubmission.getProductName());
        holder.tvTime.setText("Time:"+recyclingSubmission.getTime());
        holder.tvState.setText("State: "+recyclingSubmission.getState());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(recyclingSubmission.getId(), recyclingSubmission.getProductName(),recyclingSubmission.getTime(), recyclingSubmission.getState());
                }
            }
        });
        holder.setDynamoDBManager(dynamoDBManager);
        holder.setContext(context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamoDBManager.loadAProductPrices(recyclingSubmission.getId(), new DynamoDBManager.LoadAProductPriceListener() {
                    @Override
                    public void onLoadCompleted(String id, String customerName, String productName, double finalPrice, String time, double avgRating, String typeOfRecycle, String phone, String battery, String caseDescribe, String screen, String uptime, String state, String batteryCondition, String caseCondition, String uptimeCondition, String screenCondition, Double batteryRating, Double caseRating, Double uptimeRating, Double screenRating) {
                        Intent intent = new Intent(v.getContext(), DetailRecycleSubmissionHistorySide.class);
                                                Bundle bundle = new Bundle();
                        bundle.putString("id", recyclingSubmission.getId());
                        bundle.putString("customerName", customerName);
                        bundle.putString("productName", productName);
                        bundle.putDouble("finalPrice", finalPrice);
                        bundle.putString("time", time);
                        bundle.putDouble("avgRating", avgRating);
                        bundle.putString("typeOfRecycle", typeOfRecycle);
                        bundle.putString("phone", phone);
                        bundle.putString("battery", battery);
                        bundle.putString("caseDescribe", caseDescribe);
                        bundle.putString("screen", screen);
                        bundle.putString("uptime", uptime);
                        bundle.putString("state", state);
                        bundle.putString("batteryCondition", batteryCondition);
                        bundle.putString("caseCondition", caseCondition);
                        bundle.putString("uptimeCondition", uptimeCondition);
                        bundle.putString("screenCondition", screenCondition);
                        bundle.putDouble("batteryRating", batteryRating);
                        bundle.putDouble("caseRating", caseRating);
                        bundle.putDouble("uptimeRating", uptimeRating);
                        bundle.putDouble("screenRating", screenRating);
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }
                    @Override
                    public void onNotFound(String error) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        if(listRecyclingSubmission!=null)
        {
            return listRecyclingSubmission.size();
        }
        return 0;
    }
    public class RecyclingSubmissionListViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvState, tvTime;
        private DynamoDBManager dynamoDBManager;
        private Context context;

        public RecyclingSubmissionListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName=itemView.findViewById(R.id.tvProductName);
            tvState=itemView.findViewById(R.id.tvState);
            tvTime=itemView.findViewById(R.id.tvTime);
        }
        public void setDynamoDBManager(DynamoDBManager dynamoDBManager) {
            this.dynamoDBManager = dynamoDBManager;
        }
        public void setContext(Context context) {
            this.context = context;
        }
    }


}
