package com.example.nearview;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Near_Search extends AppCompatActivity implements View.OnClickListener {
    private Button btn_tourist, btn_custom, btn_festival, btn_course, btn_le_sp, btn_stay, btn_shopping, btn_eatery;
    private TextView N_Address;

    private String TourEndPoint = "https://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=";
    private String TourApiKey = "ylPAcl35%2BGBVHc9Vu8JeRWzU6korjtCIhpnbmq3zH7rl6EKGzS8UmSTz1dtl2Ainw96J%2Fmqb2oK%2F2DNuhHN0vw%3D%3D";
    private String contentTypeId = "&contentTypeId=12";
    private String mLongtitudeX = "&mapX=", mLatitudeY = "&mapY=", mPositionTail = "&radius=20000&listYN=Y&arrange=A";
    private String mPosition =  mLongtitudeX + mLatitudeY + mPositionTail;
    private String TourEndPointTail = "&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=" + R.string.app_name;
    private String url = TourEndPoint + TourApiKey + contentTypeId + mPosition + TourEndPointTail;
    private ArrayList<ContentTypeID> mListContentTypeIDS = new ArrayList<>();
    private  String lo = "", la = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_search);


        initView();
        getPosition();
        setContentTypeId();
    }

    private void getPosition(){
        if(getIntent().getExtras() !=null) {
            Intent intent = getIntent();
            lo = intent.getStringExtra("My_longitude"); //경도
            la = intent.getStringExtra("My_latitude");  //위도
            mLongtitudeX = mLongtitudeX + lo;
            mLatitudeY = mLatitudeY + la;
            mPosition = mLongtitudeX + mLatitudeY + mPositionTail;
        }

        final Geocoder geocoder = new Geocoder(this);
        List<Address> list = null;
        if(lo != null && la != null){
            try {
                double d1 = Double.parseDouble(lo);
                double d2 = Double.parseDouble(la);
                list = geocoder.getFromLocation(
                        d2, // 위도
                        d1, // 경도
                        2); // 얻어올 값의 개수
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
            }

            if (list != null) {
                if (list.size()==0) {
                    N_Address.setText("해당되는 주소 정보는 없습니다");
                } else {
                    N_Address.setText(list.get(0).toString());
                    String cut[] = list.get(0).toString().split(" ");
                    for(int i=0; i<cut.length; i++){
                        System.out.println("cut["+i+"] : " + cut[i]);
                    }
                    N_Address.setText(cut[1] + " " + cut[2] + " " + cut[3]);
                }
            }
        }
        else {
            N_Address.setText("여행시작 버튼을 눌러주세요");
        }


    }


    @Override
    public void onClick(View view) {
        Button btn = (Button) findViewById(view.getId());
        String btntext = btn.getText().toString();
        contentTypeId = findUri(btntext);
        url = TourEndPoint + TourApiKey + contentTypeId + mPosition + TourEndPointTail;
        Intent intent = new Intent(Near_Search.this, Near_Tourist.class);
        intent.putExtra("URI", url);
        intent.putExtra("ContentType", btntext);
        startActivity(intent);
    }

    private String findUri(String contentType)
    {
        for (int i = 0; i<mListContentTypeIDS.size(); i++)
        {
            if (mListContentTypeIDS.get(i).ContentType.equals(contentType))
                return mListContentTypeIDS.get(i).Url;
        }
        return "";
    }

    private void setContentTypeId()
    {
        ContentTypeID contentTypeID = new ContentTypeID("","");
        for (int i = 0; i<contentTypeID.ContentTypes.length; i++)
        {
            contentTypeID = new ContentTypeID(contentTypeID.ContentTypes[i], contentTypeID.ContentTypeIDs[i]);
            mListContentTypeIDS.add(contentTypeID);
        }
    }

    private void initView()
    {
        btn_tourist = findViewById(R.id.btn_tourist);
        btn_custom = findViewById(R.id.btn_custom);
        btn_festival = findViewById(R.id.btn_festival);
        btn_course = findViewById(R.id.btn_course);
        btn_le_sp = findViewById(R.id.btn_le_sp);
        btn_stay = findViewById(R.id.btn_stay);
        btn_shopping = findViewById(R.id.btn_shopping);
        btn_eatery = findViewById(R.id.btn_eatery);

        btn_tourist.setOnClickListener(this);
        btn_custom.setOnClickListener(this);
        btn_festival.setOnClickListener(this);
        btn_course.setOnClickListener(this);
        btn_le_sp.setOnClickListener(this);
        btn_stay.setOnClickListener(this);
        btn_shopping.setOnClickListener(this);
        btn_eatery.setOnClickListener(this);

        N_Address = (TextView) findViewById(R.id.n_address);
    }

    public class ContentTypeID{
        public String ContentType = "";
        public String TypeID = "";
        public String Url = "&contentTypeId=";
        public String[] ContentTypes = {"관광지", "문화시설", "행사/공연/축제", "여행코스", "레포츠", "숙박", "쇼핑", "음식점"};
        public String[] ContentTypeIDs = {"12", "14", "15", "25", "28", "32", "38", "39"};

        public ContentTypeID(String contentType, String typeID)
        {
            this.ContentType = contentType;
            this.TypeID = typeID;
            this.Url = Url + typeID;
        }
    }

}
