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
    private SimpleAdapter adapter;
    AppList appList=new AppList();
    PackageManager pm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        gridView = (GridView) findViewById(R.id.gridApp);
        pm=this.getPackageManager();
        appList.queryFilterAppInfo(pm);
        fillData();
    }

    private void fillData() {
        //初始化数据
        initData();

        String[] from = {"img", "text"};

        int[] to = new int[]{R.id.img, R.id.text};

        gridview_button_adapter adapter = new gridview_button_adapter(this, dataList);

//        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_button_item, from, to);
//
//        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, String arg2) {
//                if(view instanceof ImageView && data instanceof Drawable){
//                    ImageView iv = (ImageView)view;
//                    iv.setImageDrawable((Drawable)data);
//                    return true;
//                }else{
//                    return false;
//                }
//            }
//        });
        try {
            gridView.setAdapter(adapter);
        }
        catch (Exception e){
            String str= e.getMessage();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String name = dataList.get(arg2).get("text").toString();
                String packgeName = appList.getPackgeName(name);
                if (packgeName != "") {
                    Intent intent = new Intent();
                    intent = pm.getLaunchIntentForPackage(packgeName);
                    startActivity(intent);
                }
            }
        });
    }

    void initData() {
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <appList.applicationInfos.size(); i++) {
            ApplicationInfo appInfo=appList.applicationInfos.get(i);
            if(appInfo.icon==0)
                continue;
            String str_name = appInfo.loadLabel(pm).toString();
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", appInfo.icon);
            map.put("text", str_name);

            dataList.add(map);
        }
    }



}
