package com.example.nearview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btn;
    ToggleButton TripOnOff_Btn;
    MyReceiver myReceiver;
    public String MyAddress_Longitude, MyAddress_Latitude;
    private String ServiceData_LongitudeList, ServiceData_LatitudeList, ServiceData_Longitude, ServiceData_Latitude;
    List<String> MyLocation_LongitudeList;
    private Handler mURIParsingHandler;
    List<String>  MyLocation_LatitudeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(this);
        TripOnOff_Btn = (ToggleButton)findViewById(R.id.trip_onoff);
        TripOnOff_Btn.setOnClickListener(this);
    }
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, Near_Search.class);
                intent.putExtra("My_longitude",MyAddress_Longitude);
                intent.putExtra("My_latitude",MyAddress_Latitude);
                startActivity(intent);
                break;

            case R.id.trip_onoff:
                if (TripOnOff_Btn.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Service 시작", Toast.LENGTH_SHORT).show();
                    //아래 세개 문장은 꼭 필요
                    prepareBackgroundService();
                    Intent intent1 = new Intent(MainActivity.this, GPSBackgroundService.class);
                    startService(intent1);
                   } else {
                    Toast.makeText(getApplicationContext(), "Service 끝", Toast.LENGTH_SHORT).show();
                    //아래 네개 문장은 꼭 필요
                    ServiceData_LongitudeList = "";
                    ServiceData_LatitudeList = "";
                    MyAddress_Latitude = null;
                    MyAddress_Longitude = null;
                    Intent intent1 = new Intent(MainActivity.this, GPSBackgroundService.class);
                    stopService(intent1);
                    break;
                }
        }
    }

    //서비스 시작하기전 준비
    private void prepareBackgroundService(){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GPSBackgroundService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
    }
    //서비스에서 데이터 가져오는 부분
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            //사용자가 이동한 전체 위치
            ServiceData_LongitudeList = arg1.getStringExtra("ServiceData_longitudeList");
            ServiceData_LatitudeList = arg1.getStringExtra("ServiceData_latitudeList");

            //UI 셋팅 부분. 토스트 출력 or setText 해서 TextView 에서 확인하는 구간(필요없으면 삭제하면 됨)
            set_UI();
        }
    }



    //사용자의 현재 위치 MyAddress_Longitude, MyAddress_Latitude 에 저장해놓는 구문임.
    public void getMyLocation(){
        MyLocation_LongitudeList = Arrays.asList(ServiceData_LongitudeList.split("#"));
        if(! MyLocation_LongitudeList.isEmpty()){
            MyAddress_Longitude =  MyLocation_LongitudeList.get( MyLocation_LongitudeList.size()-1);
        }
        MyLocation_LatitudeList = Arrays.asList(ServiceData_LatitudeList.split("#"));
        if(! MyLocation_LatitudeList.isEmpty()){
            MyAddress_Latitude =  MyLocation_LatitudeList.get(MyLocation_LatitudeList.size()-1);
        }
    }

    //서비스에서 가져온 데이터 UI에 표시
    public void set_UI(){
        getMyLocation();
        Toast.makeText(this,"위도"+ MyAddress_Longitude + "\n경도"+ MyAddress_Latitude, Toast.LENGTH_LONG).show();
    }
}
