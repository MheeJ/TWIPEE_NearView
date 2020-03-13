package com.example.nearview;

import android.graphics.Bitmap;

public class NearToDTO {
    private Bitmap resld_tourist;
    private String nearposition;
    private String nearposition_detail;
    private String distance;

    public Bitmap getResld_tourist() {
        return resld_tourist;
    }

    public void setResld_tourist(Bitmap resld_tourist) {
        this.resld_tourist = resld_tourist;
    }

    public String getNearposition() {
        return nearposition;
    }

    public void setNearposition(String nearposition) {
        this.nearposition = nearposition;
    }

    public String getNearposition_detail() {
        return nearposition_detail;
    }

    public void setNearposition_detail(String nearposition_detail) {
        this.nearposition_detail = nearposition_detail;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        float dist = Float.parseFloat(distance);
        dist = dist / 1000;
        this.distance = Float.toString(dist) + "km";
    }
}
