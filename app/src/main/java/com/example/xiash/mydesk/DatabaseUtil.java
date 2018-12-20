package com.example.xiash.mydesk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    //更新本地database
    //id 应用id
    //appName 应用名称
    //packageName 应用包名
    //versionName 应用版本
    //versionCode 应用版本内部识别号
    //showType 显示类型，0隐藏，1显示
    //更新,与原有列表判断，去除删除的app
    //id,appName,packageName,versionName,versionCode,showType
    public void updateApplist(Context context, List<AppInfo> list) {
        if(getAppcount(context)>0) {
            int count = list.size();
            for(int t=0;t<count;t++) {
                if(list.get(t).getInstall()==0) {
                    //删除对应记录
                    deleteAppInfo(context,list.get(t));
                } else if(list.get(t).getInstall()==1) {
                    //插入对应记录
                    addAppInfo(context,list.get(t));
                }
            }
        } else {
            DbHelper dbHelper = new DbHelper(context,"app");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql="";
            sql = " insert into app(id, appName, packageName, versionName, versionCode,showType)";
            for(int t=0; t<list.size(); t++) {
                AppInfo apps = list.get(t);
                if(t==0)
                    sql+=" select "+apps.getId()+",'"+apps.getName()+"','"+apps.getPackageName()+"','"+apps.getVersionName()+"','"+apps.getVersionCode()+"',1";
                else
                    sql+=" union all select "+apps.getId()+",'"+apps.getName()+"','"+apps.getPackageName()+"','"+apps.getVersionName()+"','"+apps.getVersionCode()+"',1";
            }

            db.execSQL(sql);
            db.close();
        }
    }

    private void addAppInfo(Context context, AppInfo appInfo) {
        DbHelper dbHelper = new DbHelper(context,"app");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into app(id, appName, packageName, versionName, versionCode,showType) values(?,?,?,?,?,1);",
                new Object[]{appInfo.getId(),appInfo.getName(),appInfo.getPackageName(),appInfo.getVersionName(),appInfo.getVersionCode()});
        db.close();
    }

    private void deleteAppInfo(Context context, AppInfo appInfo) {
        DbHelper dbHelper = new DbHelper(context,"app");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from app where appName=? and packageName=?", new Object[]{appInfo.getName(), appInfo.getPackageName()});
        db.close();
    }

    //删除
    public void deleteApplist(Context context) {
        DbHelper dbHelper = new DbHelper(context,"app");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from app");
        db.close();
    }

    //修改显示状态
    public void setVisible(Context context, String appName, String packageName, int showType) {
        DbHelper dbHelper = new DbHelper(context,"app");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update app set showType=? where appName=? and packageName=?;", new Object[]{showType, appName, packageName});
        db.close();
    }
    //取数量
    public long getAppcount(Context context) {
        DbHelper dbHelper = new DbHelper(context,"app");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from app;",null);
        cursor.moveToFirst();
        Long count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count;
    }
    //获取显示的app
    public List<AppInfo> getShowList(Context context) {
        List<AppInfo> showList=new ArrayList<>();

        DbHelper dbHelper = new DbHelper(context,"app");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor curCount = db.rawQuery("select count(*) from app where showType=1;",null);
        curCount.moveToFirst();
        Long count = curCount.getLong(0);
        curCount.close();

        if(count>0) {
            Cursor cursor;
            String sql="select id,appName,packageName,versionName,versionCode,showType from app where showType=1;";
            cursor = db.rawQuery(sql,null);
            while (cursor.moveToNext()) {
                AppInfo appInfo = new AppInfo();
                appInfo.setId(cursor.getInt(0));
                appInfo.setName(cursor.getString(1));
                appInfo.setPackageName(cursor.getString(2));
                appInfo.setVersionName(cursor.getString(3));
                appInfo.setVersionCode(cursor.getInt(4));
                appInfo.setIsShow(cursor.getInt(5));

                showList.add(appInfo);
            }
        }
        return showList;
    }
    //获取所有
    public List<AppInfo> getAppList(Context context) {
        List<AppInfo> showList=new ArrayList<AppInfo>();

        DbHelper dbHelper = new DbHelper(context,"app");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor curCount = db.rawQuery("select count(*) from app;",null);
        curCount.moveToFirst();
        Long count = curCount.getLong(0);
        curCount.close();

        if(count>0) {
            Cursor cursor;
            String sql="select id,appName,packageName,versionName,versionCode,showType from app;";
            cursor = db.rawQuery(sql,null);
            while (cursor.moveToNext()) {
                AppInfo appInfo = new AppInfo();
                appInfo.setId(cursor.getInt(0));
                appInfo.setName(cursor.getString(1));
                appInfo.setPackageName(cursor.getString(2));
                appInfo.setVersionName(cursor.getString(3));
                appInfo.setVersionCode(cursor.getInt(4));
                appInfo.setIsShow(cursor.getInt(5));

                showList.add(appInfo);
            }
        }
        return showList;
    }
}
