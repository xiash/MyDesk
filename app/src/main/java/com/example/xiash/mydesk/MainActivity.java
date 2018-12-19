package com.example.xiash.mydesk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Intent intent;

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    AppList appList=new AppList();
    PackageManager pm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = (GridView) findViewById(R.id.gridApp);
        pm=this.getPackageManager();
        appList.queryFilterAppInfo(pm);
       // fillData();


//        if (!isAccessibilitySettingsOn(MainActivity.this, ListeningService.class.getCanonicalName())) {
//            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//            startActivity(intent);
//        }
//        else
//        {
//            intent = new Intent(MainActivity.this, ListeningService.class);
//            startService(intent);
//        }
        gotoSetting();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        //sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    @Override
    public  void onResume()
    {
//        if (isAccessibilitySettingsOn(MainActivity.this, ListeningService.class.getCanonicalName())) {
//            if (intent != null) {
//                stopService(intent);
//            }
//            intent = new Intent(MainActivity.this, ListeningService.class);
//            startService(intent);
//        }
        super.onResume();
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //exit();
            return false;
        }
        else
        return super.onKeyDown(keyCode,event);
    }


    /////////////////////////////////////////////////////////////////////
    /**
     * 检测辅助功能是否开启
     *
     * @param mContext
     * @return boolean
     */
    private boolean isAccessibilitySettingsOn(Context mContext, String serviceName) {
        int accessibilityEnabled = 0;
        // 对应的服务
        final String service = getPackageName() + "/" + serviceName;
        //Log.i(TAG, "service:" + service);
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    private void fillData() {
        //初始化数据
        initData();

        String[] from = {"img", "text"};

        int[] to = new int[]{R.id.img, R.id.text};

        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_item, from, to);

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
            if(appInfo.icon==0) {
                continue;
            }
            String str_name = appInfo.loadLabel(pm).toString();
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", appInfo.icon);
            map.put("text", str_name);

            dataList.add(map);
        }
    }

    public  void gotoSetting()
    {
        Intent intent = new Intent(this,AppSetting.class);
        startActivity(intent);
    }
}
