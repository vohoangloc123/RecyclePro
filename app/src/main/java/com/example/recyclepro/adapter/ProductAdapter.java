package com.example.recyclepro.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclepro.R;
import com.example.recyclepro.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductListViewHolder>{
    private List<Product> listProducts;

    private ProductAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(String productID, String customerName, String phone, String productName, String battery, String caseDescribe, String purchasedDate, String screen);
    }
    public void setOnItemClickListener(ProductAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductAdapter(List<Product> listProducts) {
        this.listProducts = listProducts;
    }


    @NonNull
    @Override
    public ProductAdapter.ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductListViewHolder(view);
    }
    @Override
    public int getItemCount() {
        if(listProducts!=null)
        {
            return listProducts.size();
        }
        return 0;
    }
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductListViewHolder holder, int position) {
        Product product =listProducts.get(position);
        if(product==null){
            return;
        }
        Log.d("sequence506", product.toString());
        holder.tvCustomerName.setText("Customer name: "+product.getCustomerName());
        holder.tvProductName.setText("Product name: "+product.getProductName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(product.getProductID(),product.getCustomerName(), product.getPhone(), product.getProductName(), product.getBattery(), product.getCaseDescribe(), product.getPurchasedDate(), product.getScreen());
                }
            }
        });

    }



    public class ProductListViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvCustomerName;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            tvProductName= itemView.findViewById(R.id.tvProductName);
        }
    }

}
