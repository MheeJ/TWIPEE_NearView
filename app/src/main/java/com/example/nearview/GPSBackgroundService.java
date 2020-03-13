package com.example.nearview;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;

public class GPSBackgroundService extends Service {
    public ArrayList<String> longitude_Array;
    public ArrayList<String> latitude_Array;

    public double longitude; //경도
    public double latitude; //위도
    public double altitude; //고도
    public float accuracy; //정확도
    public String provider; //위치제공자
    boolean sw = true;

    public String longitude_list = "";
    public String latitude_list = "";
    public String longitude_str = "";
    public String latitude_str = "";
    final static String MY_ACTION = "MY_ACTION";


    public GPSBackgroundService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        longitude_Array = new ArrayList<>();
        latitude_Array = new ArrayList<>();

        //서비스에서 가장 먼저 호출됨(최초에 한번만)
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        CurrentPosition();
        return super.onStartCommand(intent,flags,startId);
    }

    public void CurrentPosition() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                } else {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                            100, // 통지사이의 최소 시간간격 (miliSecond)
                            1, // 통지사이의 최소 변경거리 (m)
                            mLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                            100, // 통지사이의 최소 시간간격 (miliSecond)
                            1, // 통지사이의 최소 변경거리 (m)
                            mLocationListener);
                        }
                Looper.loop();
            }
            private final LocationListener mLocationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    //여기서 위치값이 갱신되면 이벤트가 발생한다.
                    //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
                    Log.d("test", "onLocationChanged, location:" + location);
                    longitude = location.getLongitude(); //경도
                    latitude = location.getLatitude();   //위도
                    altitude = location.getAltitude();   //고도
                    accuracy = location.getAccuracy();    //정확도
                    provider = location.getProvider();   //위치제공자
                    //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                    //Network 위치제공자에 의한 위치변화
                    //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
                    show_location();
                    //Toast.makeText(GPS_Background_Service.this, "경도 : " + longitude_list + "\n"+ "위도 : "+ latitude_list, Toast.LENGTH_LONG).show();
                }
                public void onProviderDisabled(String provider) {
                    // Disabled시
                    Log.d("test", "onProviderDisabled, provider:" + provider);
                }
                public void onProviderEnabled(String provider) {
                    // Enabled시
                    Log.d("test", "onProviderEnabled, provider:" + provider);
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // 변경시
                    Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
                }
            };
        }).start();
    }


    public void show_location() {
        longitude_str = String.valueOf(longitude);
        latitude_str = String.valueOf(latitude);
        longitude_Array.add(longitude_str);
        latitude_Array.add(latitude_str);
        longitude_list = String.join("#", longitude_Array);
        latitude_list = String.join("#", latitude_Array);
        sendData();
        //startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
    }

    public void sendData(){
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("ServiceData_longitudeList",longitude_list);
        intent.putExtra("ServiceData_latitudeList",latitude_list);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }




}
