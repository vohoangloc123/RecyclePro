package com.example.recyclepro.activities;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView tvRating1, tvRating2, tvRating3, tvRating4, tvFinalPrice;
    private Button btnSave;
    private ImageButton btnBack;
    private EditText etPrice;
    private Double tiLeGiaPin;
    private Double tiLeGiaVo;
    private Double tiLeGiaHoatDong;
    private Double tiLeGiaManHinh;
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
        btnSave=view.findViewById(R.id.btnSave);
        btnBack=view.findViewById(R.id.btnBack);
        tvFinalPrice=view.findViewById(R.id.tvFinalPrice);
        sbBattery.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý sự kiện khi giá trị của slider thay đổi
                int rating = progress + 1;
                tiLeGiaPin = 0.0;
                switch (rating) {
                    case 1:
                        tiLeGiaPin = 0.1;
                        break;
                    case 2:
                        tiLeGiaPin = 0.15;
                        break;
                    case 3:
                        tiLeGiaPin = 0.05;
                        break;
                    case 4:
                        tiLeGiaPin = 0.0;
                        break;
                    case 5:
                        tiLeGiaPin = 0.0;
                        break;
                }
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
                tiLeGiaVo = 0.0;
                switch (rating) {
                    case 1:
                        tiLeGiaVo = 0.2;
                        break;
                    case 2:
                        tiLeGiaVo = 0.1;
                        break;
                    case 3:
                        tiLeGiaVo = 0.05;
                        break;
                    case 4:
                        tiLeGiaVo = 0.1;
                        break;
                    case 5:
                        tiLeGiaVo = 0.0;
                        break;
                }
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
                tiLeGiaHoatDong = 0.0;
                switch (rating) {
                    case 1:
                        tiLeGiaHoatDong = 0.2;
                        break;
                    case 2:
                        tiLeGiaHoatDong = 0.1;
                        break;
                    case 3:
                        tiLeGiaHoatDong = 0.2;
                        break;
                    case 4:
                        tiLeGiaHoatDong = 0.0;
                        break;
                    case 5:
                        tiLeGiaHoatDong = 0.0;
                        break;
                }
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
                tiLeGiaManHinh = 0.0;
                switch (rating) {
                    case 1:
                        tiLeGiaManHinh = 0.3;
                        break;
                    case 2:
                        tiLeGiaManHinh = 0.1;
                        break;
                    case 3:
                        tiLeGiaManHinh = 0.1;
                        break;
                    case 4:
                        tiLeGiaManHinh = 0.2;
                        break;
                    case 5:
                        tiLeGiaManHinh = 0.0;
                        break;
                }
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
        btnSave.setOnClickListener(v -> {
            String giaNiemYet = etPrice.getText().toString();
            // Kiểm tra nếu có bất kỳ seekbar nào chưa được kéo
            if (tiLeGiaPin == null || tiLeGiaVo == null || tiLeGiaHoatDong == null || tiLeGiaManHinh == null) {
                Toast.makeText(getContext(), "Bạn chưa đánh giá đủ các tiêu chí", Toast.LENGTH_SHORT).show();
            } else if (giaNiemYet.isEmpty()) {
                Toast.makeText(getContext(), "Bạn chưa nhập giá", Toast.LENGTH_SHORT).show();
            } else {
                double giaCoBan = Double.parseDouble(giaNiemYet) * 0.15;
                double tongTiLe = tiLeGiaPin + tiLeGiaVo + tiLeGiaHoatDong + tiLeGiaManHinh;
                // Tính giá cuối cùng
                double finalPrice = giaCoBan * (1 + tongTiLe); // Tổng tỷ lệ được cộng vào giá niêm yết
                // In ra giá cuối cùng để kiểm tra
                Log.d(TAG, "Final Price: " + finalPrice);
                tvFinalPrice.setText(String.valueOf(finalPrice));
            }
        });
        btnBack.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }

}