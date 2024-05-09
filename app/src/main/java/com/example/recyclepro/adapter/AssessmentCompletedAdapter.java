package com.example.recyclepro.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.models.AssessmentCompleted;

import java.util.List;

public class AssessmentCompletedAdapter extends RecyclerView.Adapter<AssessmentCompletedAdapter.AssessmentCompletedListViewHolder>{
    private List<AssessmentCompleted> listAssessmentCompleted;

    private AssessmentCompletedAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(String id, String customerName, String productName, String time, String finalPrice, String avgRating);
    }
    public void setOnItemClickListener(AssessmentCompletedAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    public AssessmentCompletedAdapter(List<AssessmentCompleted> listAssessmentCompleted) {
        this.listAssessmentCompleted=listAssessmentCompleted;
    }


    @NonNull
    @Override
    public AssessmentCompletedAdapter.AssessmentCompletedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment_completed, parent, false);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(assessmentCompleted.getId(), assessmentCompleted.getCustomerName(), assessmentCompleted.getProductName(), assessmentCompleted.getTime(), String.valueOf(assessmentCompleted.getFinalPrice()), String.valueOf(assessmentCompleted.getAvgRating()));
                }
            }
        });
    }


    public class AssessmentCompletedListViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvCustomerName, tvTime, tvFinalPrice, tvAvgRating;

        public AssessmentCompletedListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName=itemView.findViewById(R.id.tvProductName);
            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvFinalPrice=itemView.findViewById(R.id.tvFinalPrice);
            tvAvgRating=itemView.findViewById(R.id.tvAvgRating);
        }
    }
}
