package com.xugang.attractions.util;

import com.google.gson.Gson;
import com.xugang.attractions.modle.AttractionInfo;
import com.xugang.attractions.modle.CityAttractions;
import com.xugang.attractions.modle.CityNameAndIdBean;
import com.xugang.attractions.modle.HotelBean;
import com.xugang.attractions.modle.ProNameAndIdBean;
import com.xugang.attractions.modle.WeatherBean;

/**
 * Created by ASUS on 2016-10-14.
 */
public class JsonUtil {

    public static CityAttractions parseCityAttractionsBean(String json) {
        return new Gson().fromJson(json, CityAttractions.class);
    }

    public static WeatherBean parseWeatherBean(String json) {
        return new Gson().fromJson(json, WeatherBean.class);
    }

    public static HotelBean parseHotelBean(String json) {
        return new Gson().fromJson(json, HotelBean.class);
    }

    public static AttractionInfo parseAttractionInfo(String json) {
        return new Gson().fromJson(json, AttractionInfo.class);
    }

    public static ProNameAndIdBean parseProNameAndIdBean(String json) {
        return new Gson().fromJson(json, ProNameAndIdBean.class);
    }

    public static CityNameAndIdBean parseCityNameAndIdBean(String json) {
        return new Gson().fromJson(json, CityNameAndIdBean.class);
    }
}
