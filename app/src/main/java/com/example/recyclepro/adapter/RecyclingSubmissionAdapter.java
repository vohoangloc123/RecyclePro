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
import com.example.recyclepro.models.RecyclingSubmission;

import java.util.List;

public class RecyclingSubmissionAdapter extends RecyclerView.Adapter<RecyclingSubmissionAdapter.RecyclingSubmissionListViewHolder>{
    private List<RecyclingSubmission> listRecyclingSubmission;
    private RecyclingSubmissionAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(String id, String productName, String time, String state);
    }
    public void setOnItemClickListener(RecyclingSubmissionAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    public RecyclingSubmissionAdapter(List<RecyclingSubmission> listRecyclingSubmission) {
        this.listRecyclingSubmission=listRecyclingSubmission;
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

        public RecyclingSubmissionListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName=itemView.findViewById(R.id.tvProductName);
            tvState=itemView.findViewById(R.id.tvState);
            tvTime=itemView.findViewById(R.id.tvTime);
        }
    }


}
