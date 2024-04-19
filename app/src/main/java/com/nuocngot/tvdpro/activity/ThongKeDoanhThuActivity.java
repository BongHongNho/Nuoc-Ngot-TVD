package com.nuocngot.tvdpro.activity;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.nuocngot.tvdpro.R;
import com.nuocngot.tvdpro.adapter.ThongKeAdapter;
import com.nuocngot.tvdpro.database.DatabaseHelper;
import com.nuocngot.tvdpro.model.ThongKeItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ThongKeDoanhThuActivity extends AppCompatActivity {

    private TextView tvSelectedStartDate, tvSelectedEndDate, tvTongDoanhThu;
    private Button btnStartDate, btnEndDate, btnThongKe;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private RecyclerView recycleViewTK;
    private ThongKeAdapter thongKeAdapter;
    private ArrayList<ThongKeItem> thongKeItemList;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_doanh_thu);
        recycleViewTK = findViewById(R.id.recycleViewTK);
        thongKeItemList = new ArrayList<>();
        tvTongDoanhThu = findViewById(R.id.tvTongDoanhThu);
        thongKeAdapter = new ThongKeAdapter(this, thongKeItemList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleViewTK.setLayoutManager(layoutManager);
        recycleViewTK.setAdapter(thongKeAdapter);
        toolbar = findViewById(R.id.toolbar);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Thống kế doanh thu");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvSelectedStartDate = findViewById(R.id.tvNgayBatDau);
        tvSelectedEndDate = findViewById(R.id.tvNgayKetThuc);
        btnStartDate = findViewById(R.id.btnChonNgayBatDau);
        btnEndDate = findViewById(R.id.btnChonNgayKetThuc);
        btnThongKe = findViewById(R.id.btnThongKe);
        recycleViewTK = findViewById(R.id.recycleViewTK);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        btnStartDate.setOnClickListener(v -> showDatePickerDialog(tvSelectedStartDate));
        btnEndDate.setOnClickListener(v -> showDatePickerDialog(tvSelectedEndDate));
        btnThongKe.setOnClickListener(v -> {
            String startDate = tvSelectedStartDate.getText().toString();
            String endDate = tvSelectedEndDate.getText().toString();
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(ThongKeDoanhThuActivity.this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date startDateObj = sdf.parse(startDate);
                Date endDateObj = sdf.parse(endDate);
                if (startDateObj.after(endDateObj)) {
                    Toast.makeText(ThongKeDoanhThuActivity.this, "Ngày bắt đầu không được lớn hơn ngày kết thúc", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            performThongKe(startDate, endDate);
        });

    }
    private void showDatePickerDialog(final TextView textView) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày")
                .setSelection(calendar.getTimeInMillis())
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            calendar.setTimeInMillis(selection);
            String selectedDate = dateFormat.format(calendar.getTime());
            textView.setText(selectedDate);
        });
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

    private void performThongKe(String startDate, String endDate) {
        thongKeItemList.clear();
        double tongDoanhThu = 0.0;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "tenSPDH",
                "SUM(soLuong) AS soLuong",
                "SUM(tongTien) AS tongTien"
        };
        String selection = "ngayMua BETWEEN ? AND ? AND maTTDH = ?";
        String[] selectionArgs = {startDate, endDate, "4"};
        Cursor cursor = db.query(
                "DonMua",
                projection,
                selection,
                selectionArgs,
                "tenSPDH",
                null,
                null
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String tenSPDH = cursor.getString(cursor.getColumnIndexOrThrow("tenSPDH"));
                    int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow("soLuong"));
                    double tongTien = cursor.getDouble(cursor.getColumnIndexOrThrow("tongTien"));
                    ThongKeItem thongKeItem = new ThongKeItem(tenSPDH, soLuong, tongTien);
                    thongKeItemList.add(thongKeItem);
                    tongDoanhThu += tongTien;
                }
            } finally {
                cursor.close();
            }
        }
        tvTongDoanhThu.setText(String.format(Locale.getDefault(), "Tổng doanh thu: %.2f VND", tongDoanhThu));
        db.close();
        dbHelper.close();
        thongKeAdapter.notifyDataSetChanged();
    }

}
