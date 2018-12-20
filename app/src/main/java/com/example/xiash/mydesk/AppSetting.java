package com.example.xiash.mydesk;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppSetting extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private List<AppInfo> appList;
    private SimpleAdapter adapter;
    PackageManager pm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        gridView = (GridView) findViewById(R.id.gridview);
        //pm=this.getPackageManager();
        //appList.queryFilterAppInfo(pm);
        fillData();
    }

    private void fillData() {
        //初始化数据
        //initData();

        DatabaseUtil dbUtils = new DatabaseUtil();
        appList = dbUtils.getAppList(getApplicationContext());
        setListInfo(((MyApplication)getApplication()).AppList);

        gridview_button_adapter adapter = new gridview_button_adapter(this, appList);

        try {
            gridView.setAdapter(adapter);
        }
        catch (Exception e){
            String str= e.getMessage();
        }
    }

    //赋值intent和icon
    public void setListInfo(List<AppInfo> oldList) {
        int newSize = appList.size();
        int oldSize = oldList.size();
        for(int x=0;x<newSize;x++ ) {
            for(int y=0;y<oldSize;y++) {
                AppInfo oldInfo = oldList.get(y);
                if(appList.get(x).getPackageName().equals(oldInfo.getPackageName())) {
                    appList.get(x).setLogo(oldInfo.getLogo());
                    break;
                }
            }
        }
    }
}
