package com.example.recyclepro.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AssessmentAnalysis extends AppCompatActivity {
    private PieChart pieChart;
    private BarChart barChart, barChart1, boxPlot;
    private DynamoDBManager dynamoDBManager;
    private TextView tvResale, tvRecycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assessment_analysis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pieChart=findViewById(R.id.piechart);
        barChart=findViewById(R.id.barChart);
        barChart1=findViewById(R.id.barChart1);
        tvResale =findViewById(R.id.tvResale);
        tvRecycle =findViewById(R.id.tvRecycle);
        dynamoDBManager=new DynamoDBManager(getApplicationContext());
        dynamoDBManager.AverageRatingOfEachTypeOfReview(new DynamoDBManager.AverageRatingOfEachTypeOfReviewListener() {
            @Override
            public void onFound(double avgBatteryRating, double avgCaseRating, double avgScreenRating, double avgUptimeRating) {
                // Tạo danh sách dữ liệu cho biểu đồ BarChart
                ArrayList<BarModel> bars = new ArrayList<>();
                bars.add(new BarModel("Battery", (float) avgBatteryRating, Color.BLUE));
                bars.add(new BarModel("Case", (float) avgCaseRating, Color.GREEN));
                bars.add(new BarModel("Uptime", (float) avgUptimeRating, Color.YELLOW));
                bars.add(new BarModel("Screen", (float) avgScreenRating, Color.RED));
                // Cập nhật biểu đồ
                updateBarChart(bars);
            }
        });
        dynamoDBManager.CalculatePercentageTypeOfRecycle(new DynamoDBManager.CalculatePercentageTypeOfRecycleListener() {
            @Override
            public void onPercentageCalculated(double resale, double recycle) {
                updatePieChart(resale, recycle);
            }
        });
        dynamoDBManager.NumberOfHaveEvaluatedOverTimeByMonth(new DynamoDBManager.NumberOfHaveEvaluatedOverTimeByMonthListener() {
            @Override
            public void onFound(TreeMap<String, Integer> reviewCountMap) {
                for (String monthYear : reviewCountMap.keySet()) {
                    Log.d("DataCheck", "MonthYear: " + monthYear + " Count: " + reviewCountMap.get(monthYear));
                }
                ArrayList<BarModel> bars = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : reviewCountMap.entrySet()) {
                    String monthYear = entry.getKey();
                    int reviewCount = entry.getValue();
                    bars.add(new BarModel(monthYear, reviewCount, Color.parseColor("#FFC107")));
                }
                // Cập nhật biểu đồ
                updateBarChart1(bars);
            }

        });
        boxPlot=findViewById(R.id.boxplot);
        dynamoDBManager.FinalPriceDistribution(new DynamoDBManager.FinalPriceDistributionListener() {
            @Override
            public void onFound(List<Float> finalPrices) {
                Log.d("CheckBoxPlot", finalPrices.toString());
                createBoxPlot(finalPrices);
            }
        });
    }
    private void updatePieChart(double resale, double recycle) {
        pieChart.clearChart();

        String notAssessedLabel = String.format("Resale (%.1f%%)", resale);
        String assessedLabel = String.format("Recycle (%.1f%%)", recycle);
        tvRecycle.setText(notAssessedLabel);
        tvResale.setText(assessedLabel);
        pieChart.addPieSlice(new PieModel(notAssessedLabel, (float) resale, getResources().getColor(R.color.colorNotAssessed)));
        pieChart.addPieSlice(new PieModel(assessedLabel, (float) recycle, getResources().getColor(R.color.colorAssessed)));
        pieChart.startAnimation();
    }
    private void updateBarChart(ArrayList<BarModel> bars) {
        barChart.clearChart();
        for (BarModel bar : bars) {
            barChart.addBar(bar);
        }
        barChart.startAnimation();
    }
    private void updateBarChart1(ArrayList<BarModel> bars) {
        barChart1.clearChart();
        for (BarModel bar : bars) {
            barChart1.addBar(bar);
        }
        barChart1.startAnimation();
    }
    private void createBoxPlot(List<Float> finalPrices) {
        Log.d("CheckBoxPlot", finalPrices.toString());
        // Kiểm tra dữ liệu
        if (finalPrices == null || finalPrices.isEmpty()) {
            // Dữ liệu trống, không hiển thị biểu đồ
            return;
        }
        // Sắp xếp dữ liệu
        Collections.sort(finalPrices);
        // Tính toán các thông số của box plot
        float min = finalPrices.get(0);
        float max = finalPrices.get(finalPrices.size() - 1);
        float median = calculateMedian(finalPrices);
        float q1 = calculateMedian(finalPrices.subList(0, finalPrices.size() / 2));
        float q3 = calculateMedian(finalPrices.subList((finalPrices.size() + 1) / 2, finalPrices.size()));

        // Tạo dữ liệu cho biểu đồ
        ArrayList<BarModel> bars = new ArrayList<>();
        bars.add(new BarModel("Min", min, Color.BLUE));
        bars.add(new BarModel("Q1", q1, Color.GREEN));
        bars.add(new BarModel("Median", median, Color.RED));
        bars.add(new BarModel("Q3", q3, Color.YELLOW));
        bars.add(new BarModel("Max", max, Color.MAGENTA));
        // Cập nhật biểu đồ
        updateBoxPlot(bars);
    }
    private void updateBoxPlot(ArrayList<BarModel> bars) {
        boxPlot.clearChart();
        for (BarModel bar : bars) {
            boxPlot.addBar(bar);
        }
        boxPlot.startAnimation();
    }
    private float calculateMedian(List<Float> prices) {
        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2;
        } else {
            return prices.get(size / 2);
        }
    }

}