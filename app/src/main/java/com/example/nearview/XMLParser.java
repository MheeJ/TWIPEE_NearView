package com.example.nearview;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class XMLParser implements Runnable {
    private Handler mHandler = null;
    private String mUrl = null;
    private TourInformation mTourInformation = null;
    private ArrayList<TourInformation> listTourInfo = new ArrayList<>();
    private HandlerParameter param = new HandlerParameter();
    BufferedReader br = null;

    @Override
    public void run() {
        try {
            parseURL();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = Message.obtain();
        msg.arg1 = param.XMLTOURINFO;
        msg.obj = listTourInfo;
        mHandler.sendMessage(msg);
    }

    public XMLParser (Handler handler, String url)
    {
        this.mHandler = handler;
        this.mUrl = url;
        mTourInformation = new TourInformation();
    }

    private void parseURL() throws Exception{
        URL url1 = new URL(mUrl);
        XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserCreator.newPullParser();
        InputStream is = url1.openStream();
        parser.setInput(is, null);
        int parserEvent = parser.getEventType();
        String tagName = "";
        boolean isItem = false;
        while (parserEvent != XmlPullParser.END_DOCUMENT){
            switch (parserEvent){
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();
                    checkItem(tagName);
                    if (tagName.equals("item")) isItem = true;
                    break;
                case XmlPullParser.TEXT:
                    if (isItem)
                        parsingData(parser.getText());
                    break;
                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if (tagName.equals("item")){
                        listTourInfo.add(mTourInformation);
                        mTourInformation = new TourInformation();
                        isItem = false;
                    }
                    break;
            }
            parserEvent = parser.next();
        }
    }

    private void parsingData(String content)
    {
        for (int i = 0; i < mTourInformation.checkItems.length; i++)
        {
            if (mTourInformation.checkItems[i])
            {
                mTourInformation.informations[i] = content;
                mTourInformation.checkItems[i] = false;
            }
        }
    }


    private void checkItem(String tag)
    {
        for (int i = 0; i < mTourInformation.checkItems.length; i++)
        {
            if(mTourInformation.elements[i].equals(tag))
                mTourInformation.checkItems[i] = true;
        }
    }

}
