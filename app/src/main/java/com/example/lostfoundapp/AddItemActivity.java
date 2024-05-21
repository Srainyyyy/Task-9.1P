package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText itemNameEditText, descriptionEditText, dateEditText, locationEditText;
    private Button saveButton, getLocationButton;

    private static final int REQUEST_CODE_ADDRESS = 1;
    private String selectedLocationName;
    private double selectedLatitude;
    private double selectedLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // 初始化视图
        itemNameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        locationEditText = findViewById(R.id.locationEditText);
        saveButton = findViewById(R.id.saveButton);
        getLocationButton = findViewById(R.id.getLocationButton);

        // 获取位置按钮点击事件
        getLocationButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddItemActivity.this, AddressSearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADDRESS);
        });

        // 保存按钮点击事件
        saveButton.setOnClickListener(v -> {
            // 获取输入值
            String itemName = itemNameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String location = locationEditText.getText().toString();

            // 将物品保存到数据库
            DatabaseHelper databaseHelper = new DatabaseHelper(AddItemActivity.this);
            databaseHelper.insertItem(itemName, description, date, location, selectedLatitude, selectedLongitude);

            // 结束当前页面
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADDRESS && resultCode == RESULT_OK && data != null) {
            selectedLocationName = data.getStringExtra("locationName");
            selectedLatitude = data.getDoubleExtra("latitude", 0);
            selectedLongitude = data.getDoubleExtra("longitude", 0);
            locationEditText.setText(selectedLocationName);
        }
    }
}
