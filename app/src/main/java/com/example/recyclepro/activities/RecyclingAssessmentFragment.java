package com.example.recyclepro.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.recyclepro.R;

public class RecyclingAssessmentFragment extends Fragment {
    public static final String TAG= RecyclingAssessmentFragment.class.getName();
    private TextView tvInformation;
    private String productID;
    private String customerName;
    private String phone;
    private String productName;
    private String productBattery;
    private String productCaseDescribe;
    private String productPurchasedDate;
    private String productDescribe;
    private SeekBar sbBattery;
    private SeekBar sbCase;
    private SeekBar sbPurchaseDate;
    private SeekBar sbDescription;
    private TextView tvRating1;
    private TextView tvRating2;
    private TextView tvRating3;
    private TextView tvRating4;
    private EditText etPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recycling_assessment, container, false);
        tvInformation=view.findViewById(R.id.tvInformation);
        Bundle bundleReceive=getArguments();
        productID = bundleReceive.getString("productID");
        Log.d("CheckBundle", "product ID: "+productID);
        customerName= bundleReceive.getString("customerName");
        Log.d("CheckBundle", "customer name: "+customerName);
        phone= bundleReceive.getString("phone");
        Log.d("CheckBundle", "phone: "+phone);
        productName = bundleReceive.getString("productName");
        Log.d("CheckBundle", "product name: "+productName);
        productBattery= bundleReceive.getString("productBattery");
        Log.d("CheckBundle", "product battery: "+productBattery);
        productCaseDescribe= bundleReceive.getString("productCaseDescribe");
        Log.d("CheckBundle", "product case describe: "+productCaseDescribe);
        productPurchasedDate= bundleReceive.getString("productPurchasedDate");
        Log.d("CheckBundle", "product purchased date: "+ productPurchasedDate);
        productDescribe= bundleReceive.getString("productDescribe");
        Log.d("CheckBundle", "product describe: "+ productDescribe);
        tvInformation.setText("product ID: " + productID + "\n" +
                "customer name: " + customerName + "\n" +
                "phone: " + phone + "\n" +
                "product name: " + productName + "\n" +
                "product battery: " + productBattery + "\n" +
                "product case describe: " + productCaseDescribe + "\n" +
                "product purchased date: " + productPurchasedDate + "\n" +
                "product describe: " + productDescribe);
        sbBattery=view.findViewById(R.id.sbBattery);
        sbCase=view.findViewById(R.id.sbCase);
        sbPurchaseDate=view.findViewById(R.id.sbPurchasedDate);
        sbDescription=view.findViewById(R.id.sbDescribe);
        tvRating1=view.findViewById(R.id.rating1);
        tvRating2=view.findViewById(R.id.rating2);
        tvRating3=view.findViewById(R.id.rating3);
        tvRating4=view.findViewById(R.id.rating4);
        etPrice=view.findViewById(R.id.etPrice);
        sbBattery.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý sự kiện khi giá trị của slider thay đổi
                int rating = progress + 1;
                // Cập nhật giá trị hiển thị
                tvRating1.setText("Rating: " + rating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Xử lý khi bắt đầu chạm vào slider
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Xử lý khi kết thúc chạm vào slider
            }
        });
        sbCase.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý sự kiện khi giá trị của slider thay đổi
                int rating = progress + 1;
                // Cập nhật giá trị hiển thị
                tvRating2.setText("Rating: " + rating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Xử lý khi bắt đầu chạm vào slider
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Xử lý khi kết thúc chạm vào slider
            }
        });
        sbPurchaseDate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý sự kiện khi giá trị của slider thay đổi
                int rating = progress + 1;
                // Cập nhật giá trị hiển thị
                tvRating3.setText("Rating: " + rating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Xử lý khi bắt đầu chạm vào slider
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Xử lý khi kết thúc chạm vào slider
            }
        });
        sbDescription.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý sự kiện khi giá trị của slider thay đổi
                int rating = progress + 1;
                // Cập nhật giá trị hiển thị
                tvRating4.setText("Rating: " + rating);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Xử lý khi bắt đầu chạm vào slider
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Xử lý khi kết thúc chạm vào slider
            }
        });
        return view;
    }
}