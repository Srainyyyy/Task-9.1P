package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressSearchActivity extends FragmentActivity {

    private PlacesClient placesClient;
    private AutoCompleteTextView searchLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);

        // 初始化 UI 组件
        searchLocation = findViewById(R.id.searchLocation);

        // 初始化 Places API
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        // 设置搜索框文本变化监听器
        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // 根据输入的文本搜索地点
                searchPlaces(s.toString());
            }
        });
    }

    private void searchPlaces(String query) {
        // 使用 Places API 搜索地点并显示在列表中
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse response) {
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                List<String> placeNames = new ArrayList<>();
                for (AutocompletePrediction prediction : predictions) {
                    placeNames.add(prediction.getPrimaryText(null).toString());
                }
                // 使用适配器将地点名称显示在 AutoCompleteTextView 下拉列表中
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddressSearchActivity.this,
                        android.R.layout.simple_dropdown_item_1line, placeNames);
                searchLocation.setAdapter(adapter);

                // 设置列表项点击事件
                searchLocation.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedPlace = (String) parent.getItemAtPosition(position);
                    // 从预测结果中获取地点的经纬度并处理
                    AutocompletePrediction prediction = predictions.get(position);
                    handlePlaceSelection(prediction);
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 处理搜索失败情况
                Toast.makeText(AddressSearchActivity.this, "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlePlaceSelection(AutocompletePrediction prediction) {
        // 从预测结果中提取地点信息
        String placeId = prediction.getPlaceId();

        // 定义需要获取的字段
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // 创建 FetchPlaceRequest
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        // 从地点预测中获取经纬度
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;

                    // 处理地点选择
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("locationName", place.getName());
                    returnIntent.putExtra("latitude", latitude);
                    returnIntent.putExtra("longitude", longitude);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(AddressSearchActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 处理获取地点失败的情况
                Toast.makeText(AddressSearchActivity.this, "Failed to fetch place: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
