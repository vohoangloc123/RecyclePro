package com.example.recyclepro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recyclepro.R;
import com.example.recyclepro.models.Price;

import java.util.List;

public class ProductPriceAdapter extends RecyclerView.Adapter<ProductPriceAdapter.ProductPriceListViewHolder>{
    private List<Price> listProductPrice;

    private ProductPriceAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(String id, String productName, Double price, String branchName);
    }
    public void setOnItemClickListener(ProductPriceAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductPriceAdapter(List<Price> listProducts) {
        this.listProductPrice = listProducts;
    }

    @NonNull
    @Override
    public ProductPriceAdapter.ProductPriceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_price, parent, false);
        return new ProductPriceListViewHolder(view);
    }
    @Override
    public int getItemCount() {
        if(listProductPrice !=null)
        {
            return listProductPrice.size();
        }
        return 0;
    }
    @Override
    public void onBindViewHolder(@NonNull ProductPriceAdapter.ProductPriceListViewHolder holder, int position) {
        Price price = listProductPrice.get(position);
        if(price==null){
            return;
        }
        holder.tvProductName.setText("Product: "+price.getProductName());
        holder.tvPrice.setText("Price: "+price.getPrice());
        holder.tvBranchName.setText("Branch: "+price.getBranchName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(price.getId(), price.getProductName(), price.getPrice(),price.getBranchName());

                }
            }
        });


    }
    public class ProductPriceListViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvPrice, tvBranchName;

        public ProductPriceListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvProductName= itemView.findViewById(R.id.tvProductName);
            tvBranchName=itemView.findViewById(R.id.tvBranch);
        }
    }

}
