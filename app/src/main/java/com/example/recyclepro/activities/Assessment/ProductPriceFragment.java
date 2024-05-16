package com.example.recyclepro.activities.Assessment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.recyclepro.R;
import com.example.recyclepro.adapter.ProductPriceAdapter;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.Price;

import java.util.ArrayList;

public class ProductPriceFragment extends Fragment {
    public static final String TAG= ProductPriceFragment.class.getName();
    private RecyclerView rcvProductPrice;
    private ArrayList<Price> listProductPrice;
    private DynamoDBManager dynamoDBManager;

    private ProductPriceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_price, container, false);
        rcvProductPrice=view.findViewById(R.id.rcvProductPrice);
        dynamoDBManager=new DynamoDBManager(getContext());
        listProductPrice=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvProductPrice.setLayoutManager(linearLayoutManager);
        adapter= new ProductPriceAdapter(listProductPrice);
        rcvProductPrice.setAdapter(adapter);
        listProductPrice.clear();
        dynamoDBManager.loadProductPrice(new DynamoDBManager.loadProductPriceListener() {
            @Override
            public void onFound(String id, String productName, Double price, String brandName) {
                Price priceItem=new Price(id, productName, price, brandName);
                listProductPrice.add(priceItem);
                adapter.notifyDataSetChanged();
            }
        });
        ImageButton btnBack=view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().popBackStack();
        });

        adapter.setOnItemClickListener(new ProductPriceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, String productName, Double price, String branchName) {
                // Gọi phương thức chuyển sang RecyclingAssessmentFragment và truyền thông tin giá sản phẩm
                goToRecyclingAssessmentFragment(price);
            }
        });
        return view;
    }

    private void goToRecyclingAssessmentFragment(Double price) {
        RecyclingAssessmentFragment assessmentFragment = new RecyclingAssessmentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("price", String.valueOf(price));
        // Các xử lý khác...
        assessmentFragment.setArguments(bundle);
        // Thực hiện chuyển Fragment
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main, assessmentFragment, assessmentFragment.TAG);
        fragmentTransaction.addToBackStack(assessmentFragment.TAG);
        fragmentTransaction.commit();
    }
}