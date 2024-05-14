package com.example.recyclepro.activities.Assessment;

import static com.example.recyclepro.services.PriceCalculationService.convertRatingToPercentage;
import static com.example.recyclepro.services.PriceCalculationService.costingPrice;

import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.recyclepro.R;
import com.example.recyclepro.activities.LiveData.MyViewModel;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.example.recyclepro.models.ConfigCondition;
import com.example.recyclepro.models.ConfigRate;
import com.example.recyclepro.models.Rating;
import com.example.recyclepro.services.Condition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RecyclingAssessmentFragment extends Fragment {
    public static final String TAG= RecyclingAssessmentFragment.class.getName();
    private TextView tvInformation1, tvInformation2;
    private String productID;
    private String customerName;
    private String phone;
    private String productName;
    private String productBattery;
    private String productCaseDescribe;
    private String productPurchasedDate;
    private String productScreen, time, email;
    private SeekBar sbBattery;
    private SeekBar sbCase;
    private SeekBar sbPurchaseDate;
    private SeekBar sbScreen;
    private TextView tvRating1, tvRating2, tvRating3, tvRating4, tvFinalPrice,
            tvCondition1, tvCondition2, tvCondition3, tvCondition4;
    private Button btnCalculator;
    private ImageButton btnBack;
    private EditText etPrice;
    private Rating tiLeGiaPin;
    private Rating tiLeGiaVo;
    private Rating tiLeGiaHoatDong;
    private Rating tiLeGiaManHinh;
    private Condition condition;
    private ImageButton btnSendEmail, btnImages;
    private DynamoDBManager dynamoDBManager;
    public int batteryRating, caseRating, uptimeRating, screenRating;
    public String batteryCondition, caseCondition, uptimeCondition, screenCondition;
    public double finalPrice;
    private MyViewModel viewModel;
    private String urlFrontOfDevice;
    private String urlBackOfDevice;
    private WaitingProductListSide mainActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recycling_assessment, container, false);
        tvInformation1=view.findViewById(R.id.tvInformation1);
        tvInformation2=view.findViewById(R.id.tvInformation2);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
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
        productScreen = bundleReceive.getString("productScreen");
        Log.d("CheckBundle", "product screen: "+ productScreen);
        time = bundleReceive.getString("time");
        Log.d("CheckBundle", "product screen: "+ productScreen);
        email = bundleReceive.getString("email");
        Log.d("CheckBundle", "product email: "+email);
        tvInformation1.setText("product ID: " + productID + "\n" +
                "customer name: " + customerName + "\n" +
                "phone: " + phone + "\n" +
                "Time: " + time + "\n"+
                "email: " + email + "\n"
        );
        tvInformation2.setText("product ID: " + productID + "\n" +
                "customer name: " + customerName + "\n" +
                "phone: " + phone + "\n" +
                "product name: " + productName + "\n" +
                "product battery: " + productBattery + "\n" +
                "product case describe: " + productCaseDescribe + "\n" +
                "product purchased date: " + productPurchasedDate + "\n" +
                "product screen: " + productScreen);
        sbBattery=view.findViewById(R.id.sbBattery);
        sbCase=view.findViewById(R.id.sbCase);
        sbPurchaseDate=view.findViewById(R.id.sbPurchasedDate);
        sbScreen =view.findViewById(R.id.sbScreen);
        tvRating1=view.findViewById(R.id.rating1);
        tvRating2=view.findViewById(R.id.rating2);
        tvRating3=view.findViewById(R.id.rating3);
        tvRating4=view.findViewById(R.id.rating4);
        tvCondition1=view.findViewById(R.id.condition1);
        tvCondition2=view.findViewById(R.id.condition2);
        tvCondition3=view.findViewById(R.id.condition3);
        tvCondition4=view.findViewById(R.id.condition4);
        etPrice=view.findViewById(R.id.etPrice);
        btnCalculator=view.findViewById(R.id.btnCalculator);
        btnBack=view.findViewById(R.id.btnBack);
        btnSendEmail=view.findViewById(R.id.btnSendEmail);
        btnImages=view.findViewById(R.id.btnImages);
        tvFinalPrice=view.findViewById(R.id.tvFinalPrice);
        dynamoDBManager=new DynamoDBManager(getContext());
        condition=new Condition();
        dynamoDBManager.loadImagesOfProduct(productID, new DynamoDBManager.LoadImagesOfProductListener() {
            @Override
            public void onFound(String id, String frontOfDevice, String backOfDevice) {
                urlFrontOfDevice=frontOfDevice;
                urlBackOfDevice=backOfDevice;
            }
        });
        btnImages.setOnClickListener(v->{
            Bundle bundle=new Bundle();
            bundle.putString("frontOfDevice", urlFrontOfDevice);
            bundle.putString("backOfDevice", urlBackOfDevice);
            goToViewImagesOfProductFragment(bundle);
        });
        sbBattery.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý sự kiện khi giá trị của slider thay đổi
                batteryRating = progress + 1;
                //Tự lấy dữ liệu từ sqlite đưa vào
                ConfigRate configRateBattery = new ConfigRate(
                        -0.1, //Nếu không có pin thì tăng 10% của 20% (battery: 0.2)
                        0.6, //Nếu pin cháy nổ, phồng giảm 60% của 20% (battery: 0.2)
                        0.3, //Nếu pin chai, phồng giảm 60% của 20% (battery: 0.2)
                        -0.4, // Nếu pin còn trên hoạt động tốt trên mức 80, tăng 60% của 20% (battery: 0.2)
                        -1 // Nếu pin còn hoạt động tốt như mới, tăng 100% của 20% (battery: 0.2)
                );

                tiLeGiaPin = convertRatingToPercentage(batteryRating, configRateBattery, 0.2 /* Dua theo ti set mac dinh hoac tuy chon mien sao tong cua cac moc la 1 */);
                // Cập nhật giá trị hiển thị
                tvRating1.setText("Rating: " + batteryRating);
                ConfigCondition configConditionBattery = new ConfigCondition(
                        "No battery",
                        "Explosion, swollen battery",
                        "Battery swollen",
                        "Operates well over 80%",
                        "Like new"
                );
                batteryCondition = condition.getConditionText(batteryRating, configConditionBattery);
                tvCondition1.setText("Condition: "+batteryCondition);
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
                caseRating = progress + 1;
                //Tự lấy dữ liệu từ sqlite đưa vào
                ConfigRate configRateCase = new ConfigRate(
                        1, // Nếu bị nát - thủng - không còn, giảm 100% của 10% (case: 0.1)
                        0.5, // Nếu vỏ bị xước nhiều, qua sửa chửa, giảm 50% của 10% (case: 0.1)
                        0.3, // Nếu vỏ xước ít, đã qua sửa chửa, giảm 20% của 10% (case: 0.1)
                        -0.6, // Nếu vỏ có xước ít không qua sửa chửa, tăng 20% của 10% (case: 0.1)
                        -1 //Như mới, tăng 100% của 10% (case: 0.1)
                );
                tiLeGiaVo = convertRatingToPercentage(caseRating, configRateCase, 0.1 /* Dua theo ti set mac dinh hoac tuy chon mien sao tong cua cac moc la 1 */);
                // Cập nhật giá trị hiển thị
                tvRating2.setText("Rating: " + caseRating);
                ConfigCondition configConditionBattery = new ConfigCondition(
                        "Damaged, punctured, not functional",
                        "Heavily scratched, repaired multiple times",
                        "Lightly scratched, repaired once",
                        "Lightly scratched, never repaired",
                        "Like new"
                );
                caseCondition = condition.getConditionText(caseRating, configConditionBattery);
                tvCondition2.setText("Condition: "+caseCondition);
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
                uptimeRating = progress + 1;
                //Tự lấy dữ liệu từ sqlite đưa vào
                ConfigRate configRateStatus = new ConfigRate(
                        1, //Nếu không hoạt động, vào nước , cháy nổ, biến dạng không nhận dạng được, giảm 100% của 40% (status: 0.4)
                        0.6, //Nếu bị lỗi, không lên nguồn, hoạt động gì hết, giảm 60% của 40%  (status: 0.4)
                        0.5, //Nếu gặp lỗi treo máy, panic, thiếu linh kiện, giảm 50% của 40%  (status: 0.4)
                        -0.6, //Hoạt động bình thường, một vài chức năng nhỏ không hoạt không đáng kể, tăng 50% của 40% (status: 0.4)
                        -1 //Hoạt động bình thường như mới, tăng 100% của 40% (status: 0.4)
                );
                tiLeGiaHoatDong = convertRatingToPercentage(uptimeRating, configRateStatus, 0.4 /* Dua theo ti set mac dinh hoac tuy chon mien sao tong cua cac moc la 1 */);


                // Cập nhật giá trị hiển thị
                tvRating3.setText("Rating: " + uptimeRating);
                ConfigCondition configConditionBattery = new ConfigCondition(
                        "Not working, water damage, fire damage, deformation",
                        "Does not power on",
                        "Freezing, missing components...",
                        "Operational, few errors",
                        "Operational like new"
                );
                uptimeCondition = condition.getConditionText(uptimeRating, configConditionBattery);
                tvCondition3.setText("Condition: "+uptimeCondition);
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
        sbScreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý sự kiện khi giá trị của slider thay đổi
                screenRating = progress + 1;
                ConfigRate configRateMonitor = new ConfigRate(
                        1, //Nếu không có màn hình, vỡ, thủng, giảm 100% của 30%  (monitor: 0.3)
                        0.4, //Nếu màn hình bị sọc, bị liệt, bóng mờ, xước nhiều, qua sửa chữa, giảm 40% của 30%  (monitor: 0.3)
                        -0.2, //Nếu màn hình xước nhiều, chưa qua sửa chữa, tăng 20% của 30% (monitor: 0.3)
                        -0.6, //Bị xước ít như mới, tăng 60% của 30% (monitor: 0.3)
                        -1 //Như mới, tăng 100% của 30% (monitor: 0.3)
                );
                tiLeGiaManHinh = convertRatingToPercentage(screenRating, configRateMonitor, 0.3 /* Dua theo ti set mac dinh hoac tuy chon mien sao tong cua cac moc la 1 */);
                // Cập nhật giá trị hiển thị
                tvRating4.setText("Rating: " + screenRating);
                ConfigCondition configConditionBattery = new ConfigCondition(
                        "No display, cracked, punctured",
                        "Striped, paralyzed",
                        "Heavily scratched",
                        "Lightly like new",
                        "Like new"
                );
                screenCondition = condition.getConditionText(screenRating, configConditionBattery);
                tvCondition4.setText("Condition: "+screenCondition);
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
        btnCalculator.setOnClickListener(v -> {
            String giaNiemYet = etPrice.getText().toString();
            // Kiểm tra nếu có bất kỳ seekbar nào chưa được kéo
            if (tiLeGiaPin == null || tiLeGiaVo == null || tiLeGiaHoatDong == null || tiLeGiaManHinh == null) {
                Toast.makeText(getContext(), "Bạn chưa đánh giá đủ các tiêu chí", Toast.LENGTH_SHORT).show();
            } else if (giaNiemYet.isEmpty()) {
                Toast.makeText(getContext(), "Bạn chưa nhập giá", Toast.LENGTH_SHORT).show();
            } else {
                //Tính ra giá cuối cùng thông qua hàm
                finalPrice = costingPrice(Double.parseDouble(giaNiemYet), 0.15 /* Gia chi tra co ban */, tiLeGiaPin, tiLeGiaVo, tiLeGiaManHinh, tiLeGiaHoatDong);

                // In ra giá cuối cùng để kiểm tra
                Log.d(TAG, "Final Price: " + finalPrice);
                tvFinalPrice.setText(String.valueOf(finalPrice));
            }
        });
        btnSendEmail.setOnClickListener(v->{
           String price=tvFinalPrice.getText().toString();
            if (tiLeGiaPin == null || tiLeGiaVo == null || tiLeGiaHoatDong == null || tiLeGiaManHinh == null) {
                Toast.makeText(getContext(), "Bạn chưa đánh giá đủ các tiêu chí", Toast.LENGTH_SHORT).show();
            }else if (price.isEmpty()) {
                Toast.makeText(getContext(), "Bạn chưa có giá chót", Toast.LENGTH_SHORT).show();
            }else
            {
                String currentTime=getCurrentDateTime();
                Random rand = new Random();
                int number = rand.nextInt(1000000);
                String id = String.format("%06d", number);
                double avgRating=(Double.parseDouble(String.valueOf(batteryRating))+
                        Double.parseDouble(String.valueOf(caseRating))+
                        Double.parseDouble(String.valueOf(uptimeRating))+
                        Double.parseDouble(String.valueOf(screenRating)))/4;
                String typeOfRecycle=null;
                if(avgRating>3)
                {
                    typeOfRecycle="Resale";
                }else{
                    typeOfRecycle="Recycling";
                }
                dynamoDBManager.SubmitProductPrice(productID, productID, customerName, phone,
                        productName, productCaseDescribe, productPurchasedDate,
                        productBattery, productScreen, "reviewed",
                        String.valueOf(batteryRating), batteryCondition,
                        String.valueOf(caseRating), caseCondition,
                        String.valueOf(uptimeRating), uptimeCondition,
                        String.valueOf(screenRating), screenCondition,
                        String.valueOf(finalPrice),
                        currentTime, String.valueOf(avgRating),
                        email, typeOfRecycle);
                dynamoDBManager.updateStateOfProduct(productID, "assessed");
                Toast.makeText(getContext(), "Đã gửi email"+email, Toast.LENGTH_SHORT).show();
            }
        });
        btnBack.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().popBackStack();
        });
        return view;
    }
    public void goToViewImagesOfProductFragment(Bundle bundle) {
        ViewImagesOfProductFragment viewImagesOfProductFragment = new ViewImagesOfProductFragment();
        viewImagesOfProductFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main, viewImagesOfProductFragment, viewImagesOfProductFragment.TAG);
        fragmentTransaction.addToBackStack(viewImagesOfProductFragment.TAG);
        fragmentTransaction.commit();
    }
    public static String getCurrentDateTime() {
        // Định dạng cho ngày tháng năm và giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Lấy thời gian hiện tại
        Date currentTime = new Date();
        // Định dạng thời gian hiện tại thành chuỗi
        String formattedDateTime = dateFormat.format(currentTime);
        return formattedDateTime;
    }
    private void changeData() {
        viewModel.setData("New data");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        changeData(); // Cập nhật LiveData ở đây
        Log.d("Detach", "onDestroyView: ");
    }
}

