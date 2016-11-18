package com.xugang.attractions.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ASUS on 2016-10-14.
 */
public class HttpUtil {
    private static final int APP_ID = 25293;
    private static final String APP_SIGN = "c7e4d497be244986b323aef7dc8f98d9";
    private static OkHttpClient okHttpClient;

    //根据省份的Id查找该省份中的城市
    public static String getCitiesListBean(int proId) {
        return query("https://route.showapi.com/268-3?proId=" + proId + "&showapi_appid=" + APP_ID + "&showapi_sign=" + APP_SIGN);
    }

    //根据景点的id查询天气
    public static String getWeatherBean(String attrid) {
        return query("https://route.showapi.com/9-6?area=&need3HourForcast=0&needAlarm=0&needHourData=0&needIndex=1&needMoreDay=0&showapi_appid=25293&showapi_timestamp=20161025114640&spotId=" + attrid + "&showapi_sign=8c0a5951cc27270355e8476c88060f6d");
    }

    //根据城市id查询酒店
    public static String getHotelBean(String cityid, String comeDate, String leaveDate) {
        return query("https://route.showapi.com/405-5?cityId=" + cityid + "&comeDate=" + comeDate + "&hbs=&keyword=&latitude=&leaveDate=" + leaveDate + "&longitude=&page=1&pageSize=100&sectionId=&showapi_appid=25293&showapi_timestamp=20161024190522&sortType=&starRatedId=&showapi_sign=2fe990f0d6bc838b11ee2756bc1a84d5");
    }

    //查询所有省份:名称和id
    public static String getProvinceBean() {
        return query("https://route.showapi.com/268-2?showapi_appid=" + APP_ID + "&showapi_sign=" + APP_SIGN);
    }

    public static String getAttractions(int cityId, int page, String attrName) {
        return query("https://route.showapi.com/268-1?areaId=&cityId=" + cityId + "&keyword=" + attrName + "&page=" + page + "&proId=&showapi_appid=" + APP_ID + "&showapi_sign=" + APP_SIGN);
    }

    public static String getAttractions() {
        return query("https://route.showapi.com/268-1?areaId=&cityId=&keyword=&page=&proId=3&showapi_appid=" + APP_ID + "&showapi_sign=" + APP_SIGN);
    }

    public static String getAttractionsByName(String attrName) {
        return query("https://route.showapi.com/268-1?areaId=&cityId=&keyword=" + attrName + "&page=&proId=&showapi_appid=" + APP_ID + "&showapi_sign=" + APP_SIGN);
    }

    private static String query(String uri) {
        String json = "";
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        Request build = new Request.Builder()
                .url(uri)
                .tag("tag")
                .build();

        try {
            Response execute = okHttpClient.newCall(build).execute();
            if (execute.isSuccessful()) {
                json = execute.body().string();
            } else {
                json = "download is not successful";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
