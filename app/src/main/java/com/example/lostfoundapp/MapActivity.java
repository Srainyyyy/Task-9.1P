package com.example.lostfoundapp;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dbHelper = new DatabaseHelper(this);

        // 初始化地图
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 获取所有物品的位置信息
        List<Item> itemList = dbHelper.getAllItems();

        // 添加标记并移动相机
        for (Item item : itemList) {
            LatLng location = new LatLng(item.getLatitude(), item.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(item.getLocation()));
        }

        // 将地图相机移动到第一个标记的位置
        if (!itemList.isEmpty()) {
            LatLng firstItemLocation = new LatLng(itemList.get(0).getLatitude(), itemList.get(0).getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemLocation, 15));
        }

        // Log 经纬度和地点名称以进行调试
        for (Item item : itemList) {
            Log.d("MapActivity", "Latitude: " + item.getLatitude() + ", Longitude: " + item.getLongitude());
            Log.d("MapActivity", "Location Name: " + item.getLocation());
        }
    }
}
