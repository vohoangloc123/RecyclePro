package com.example.recyclepro.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recyclepro.R;
import com.example.recyclepro.dynamoDB.DynamoDBManager;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CustomerAnalysis extends AppCompatActivity {
    private PieChart pieChart;
    private BarChart barChart;
    private DynamoDBManager dynamoDBManager;
    private TextView tvAssessed, tvNotAssessed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_analysis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", ""); // Lấy email từ SharedPreferences
        pieChart=findViewById(R.id.piechart);
        barChart=findViewById(R.id.barChart);
        tvAssessed=findViewById(R.id.tvAssessed);
        tvNotAssessed=findViewById(R.id.tvNotAssessed);
        dynamoDBManager=new DynamoDBManager(getApplicationContext());
        dynamoDBManager.NumberOfReviewOverTimeByMonth(new DynamoDBManager.NumberOfReviewOverTimeByMonthListener() {
            @Override
            public void onFound(TreeMap<String, Integer> reviewCountMap) {
                ArrayList<BarModel> bars = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : reviewCountMap.entrySet()) {
                    String monthYear = entry.getKey();
                    int reviewCount = entry.getValue();
                    bars.add(new BarModel(monthYear, reviewCount, Color.parseColor("#FFC107")));
                }

                // Cập nhật biểu đồ
                updateBarChart(bars);
            }
        });
        dynamoDBManager.calculatePercentageDeviceStatus(email, new DynamoDBManager.CalculatePercentageDeviceStatusListener() {
            @Override
            public void onPercentageCalculated(double notAssessedPercentage, double assessedPercentage) {
                updatePieChart(notAssessedPercentage, assessedPercentage);
            }
        });


    }
    private void updatePieChart(double notAssessedPercentage, double assessedPercentage) {
        pieChart.clearChart();

        String notAssessedLabel = String.format("Not Assessed Yet (%.1f%%)", notAssessedPercentage);
        String assessedLabel = String.format("Assessed (%.1f%%)", assessedPercentage);
        tvNotAssessed.setText(notAssessedLabel);
        tvAssessed.setText(assessedLabel);
        pieChart.addPieSlice(new PieModel(notAssessedLabel, (float) notAssessedPercentage, getResources().getColor(R.color.colorNotAssessed)));
        pieChart.addPieSlice(new PieModel(assessedLabel, (float) assessedPercentage, getResources().getColor(R.color.colorAssessed)));
        pieChart.startAnimation();
    }
    private void updateBarChart(ArrayList<BarModel> bars) {
        barChart.clearChart();
        for (BarModel bar : bars) {
            barChart.addBar(bar);
        }
        barChart.startAnimation();
    }
}