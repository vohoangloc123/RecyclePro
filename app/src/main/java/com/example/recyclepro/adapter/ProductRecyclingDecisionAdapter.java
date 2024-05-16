package com.example.recyclepro.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.models.ProductRecyclingDecision;

import java.util.List;

public class ProductRecyclingDecisionAdapter extends RecyclerView.Adapter<ProductRecyclingDecisionAdapter.ProductRecyclingDecisionListViewHolder>{
    private List<ProductRecyclingDecision> listProductRecyclingDecision;

    private ProductRecyclingDecisionAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(String productID, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen, String time);
    }
    public void setOnItemClickListener(ProductRecyclingDecisionAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductRecyclingDecisionAdapter(List<ProductRecyclingDecision> listProductRecyclingDecision) {
        this.listProductRecyclingDecision = listProductRecyclingDecision;
    }


    @NonNull
    @Override
    public ProductRecyclingDecisionAdapter.ProductRecyclingDecisionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_recycling_decision, parent, false);
        return new ProductRecyclingDecisionListViewHolder(view);
    }
    @Override
    public int getItemCount() {
        if(listProductRecyclingDecision !=null)
        {
            return listProductRecyclingDecision.size();
        }
        return 0;
    }
    @Override
    public void onBindViewHolder(@NonNull ProductRecyclingDecisionAdapter.ProductRecyclingDecisionListViewHolder holder, int position) {
        ProductRecyclingDecision productRecyclingDecision = listProductRecyclingDecision.get(position);
        if(productRecyclingDecision==null){
            return;
        }
        holder.tvCustomerName.setText("Customer name: "+productRecyclingDecision.getCustomerName());
        holder.tvProductName.setText("Product name: "+productRecyclingDecision.getProductName());
        holder.tvTime.setText("Time: "+productRecyclingDecision.getTime());
        holder.tvEmail.setText("Email: "+productRecyclingDecision.getEmail());
        holder.tvPhone.setText("Phone: "+productRecyclingDecision.getPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(productRecyclingDecision.getId(), productRecyclingDecision.getCustomerName(),
                            productRecyclingDecision.getProductName(), productRecyclingDecision.getEmail(), productRecyclingDecision.getPhone(),
                            productRecyclingDecision.getCustomerAddress(),productRecyclingDecision.getBranchAddress(), productRecyclingDecision.getDescription(),productRecyclingDecision.getTime());
                }
            }
        });

    }
    public class ProductRecyclingDecisionListViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvCustomerName, tvTime, tvPhone, tvEmail;

        public ProductRecyclingDecisionListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            tvProductName= itemView.findViewById(R.id.tvProductName);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvEmail=itemView.findViewById(R.id.tvEmail);
            tvPhone=itemView.findViewById(R.id.tvPhone);
        }
    }

}
