package com.xugang.attractions.util;

import android.database.sqlite.SQLiteDatabase;

import com.xugang.attractions.MyApp;
import com.xugang.attractions.dao.DaoMaster;
import com.xugang.attractions.dao.DaoSession;

/**
 * Created by idea on 2016/10/21.
 */
public class GreenDaoUtils {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static GreenDaoUtils greenDaoUtils;

    private GreenDaoUtils() {
        initGreenDao();
    }

    public static GreenDaoUtils getSingleTon() {
        if (greenDaoUtils == null) {
            greenDaoUtils = new GreenDaoUtils();
        }
        return greenDaoUtils;
    }

    private void initGreenDao() {
        mHelper = new DaoMaster.DevOpenHelper(MyApp.getApplication(), "mydb", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

}
