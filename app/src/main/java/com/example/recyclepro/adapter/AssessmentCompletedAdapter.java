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
import com.example.recyclepro.activities.Assessment.DetailEvaluationHistorySide;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.AssessmentCompleted;

import java.util.List;

public class AssessmentCompletedAdapter extends RecyclerView.Adapter<AssessmentCompletedAdapter.AssessmentCompletedListViewHolder>{
    private List<AssessmentCompleted> listAssessmentCompleted;

    private AssessmentCompletedAdapter.OnItemClickListener mListener;
    private DynamoDBManager dynamoDBManager;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(String id, String customerName, String productName, String time, String finalPrice, String avgRating, String typeOfRecycle);
    }
    public void setOnItemClickListener(AssessmentCompletedAdapter.OnItemClickListener listener) {
        mListener = listener;

    }
    public AssessmentCompletedAdapter(List<AssessmentCompleted> listAssessmentCompleted, DynamoDBManager dynamoDBManager, Context context) {
        this.listAssessmentCompleted=listAssessmentCompleted;
        this.dynamoDBManager=dynamoDBManager;
        this.context=context;
    }


    @NonNull
    @Override
    public AssessmentCompletedAdapter.AssessmentCompletedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluation_history, parent, false);
        return new AssessmentCompletedAdapter.AssessmentCompletedListViewHolder(view);
    }
    @Override
    public int getItemCount() {
        if(listAssessmentCompleted!=null)
        {
            return listAssessmentCompleted.size();
        }
        return 0;
    }
    @Override
    public void onBindViewHolder(@NonNull AssessmentCompletedAdapter.AssessmentCompletedListViewHolder holder, int position) {
        AssessmentCompleted assessmentCompleted =listAssessmentCompleted.get(position);
        if(assessmentCompleted==null){
            return;
        }
        holder.tvCustomerName.setText("Customer name:"+assessmentCompleted.getCustomerName());
        holder.tvProductName.setText("Product name:"+assessmentCompleted.getProductName());
        holder.tvTime.setText("Time:"+assessmentCompleted.getTime());
        String finalPrice= String.valueOf(assessmentCompleted.getFinalPrice());
        holder.tvFinalPrice.setText("Final price: "+finalPrice);
        String avgRating= String.valueOf(assessmentCompleted.getAvgRating());
        holder.tvAvgRating.setText("Avg rating: "+avgRating);
        holder.tvTypeOfRecycle.setText("Type: "+assessmentCompleted.getTypeOfRecycle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(assessmentCompleted.getId(), assessmentCompleted.getCustomerName(), assessmentCompleted.getProductName(), assessmentCompleted.getTime(), String.valueOf(assessmentCompleted.getFinalPrice()), String.valueOf(assessmentCompleted.getAvgRating()), assessmentCompleted.getTypeOfRecycle());
                }
            }
        });
        holder.setDynamoDBManager(dynamoDBManager);
        holder.setContext(context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamoDBManager.loadAProductPrices(assessmentCompleted.getId(), new DynamoDBManager.LoadAProductPriceListener() {
                    @Override
                    public void onLoadCompleted(String id, String customerName, String productName, double finalPrice, String time, double avgRating, String typeOfRecycle, String phone, String battery, String caseDescribe, String screen, String uptime, String state, String batteryCondition, String caseCondition, String uptimeCondition, String screenCondition, Double batteryRating, Double caseRating, Double uptimeRating, Double screenRating) {
                        Intent intent = new Intent(v.getContext(), DetailEvaluationHistorySide.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", assessmentCompleted.getId());
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


    public class AssessmentCompletedListViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvCustomerName, tvTime, tvFinalPrice, tvAvgRating, tvTypeOfRecycle;
        private DynamoDBManager dynamoDBManager;
        private Context context;
        public AssessmentCompletedListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName=itemView.findViewById(R.id.tvProductName);
            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvFinalPrice=itemView.findViewById(R.id.tvFinalPrice);
            tvAvgRating=itemView.findViewById(R.id.tvAvgRating);
            tvTypeOfRecycle=itemView.findViewById(R.id.tvTypeOfRecycle);
        }
        public void setDynamoDBManager(DynamoDBManager dynamoDBManager) {
            this.dynamoDBManager = dynamoDBManager;
        }
        public void setContext(Context context) {
            this.context = context;
        }
    }
}
