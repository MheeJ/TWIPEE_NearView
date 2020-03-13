package com.example.nearview;


public class TourInformation {
    public final int INFOSIZE = 7;
    public final int ADDR1 = 0, DIST = 1, FIRSTIMAGE = 2, MAPX = 3, MAPY = 4, TEL = 5, TITLE = 6;
    public final boolean[] checkItems = {false, false, false, false, false, false, false};
    public final String[] elements = {"addr1", "dist", "firstimage", "mapx", "mapy", "tel", "title"};
    public String[] informations = new String[INFOSIZE];

    public TourInformation() {
        informations = elements;
    }
}