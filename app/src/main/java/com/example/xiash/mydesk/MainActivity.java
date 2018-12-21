package com.example.xiash.mydesk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Intent intent;

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private List<AppInfo> showList;
    private SimpleAdapter adapter;
    AppList appList=new AppList();
    PackageManager pm;
    long[] mHits = new long[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridApp);
        pm=this.getPackageManager();

        ImageView image = (ImageView) findViewById(R.id.title_img);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //给数组的最后赋当前时钟值
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                //当0出的值大于当前时间-500时 证明在500秒内点击了2次
                if(mHits[0] > SystemClock.uptimeMillis() - 1000){
                    //Toast.makeText(MainActivity.this, "被双击了", Toast.LENGTH_SHORT).show();
                    gotoSetting();
                }
            }
        });

        //更新list
        initApp();
        //gotoSetting();

        //显示app
        fillData();
//        if (!isAccessibilitySettingsOn(MainActivity.this, ListeningService.class.getCanonicalName())) {
//            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//            startActivity(intent);
//        }
//        else
//        {
//            intent = new Intent(MainActivity.this, ListeningService.class);
//            startService(intent);
//        }

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
        if (isAccessibilitySettingsOn(MainActivity.this, ListeningService.class.getCanonicalName())) {
            if (intent != null) {
                stopService(intent);
            }
            intent = new Intent(MainActivity.this, ListeningService.class);
            startService(intent);
        }
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

        gridview_adapter adapter = new gridview_adapter(this, showList);

        try {
            gridView.setAdapter(adapter);
        }
        catch (Exception e){
            String str= e.getMessage();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String packgeName = showList.get(arg2).getPackageName();
                if (packgeName != "") {
                    Intent intent = new Intent();
                    intent = showList.get(arg2).getIntent();
                    startActivity(intent);
                }
            }
        });
    }

    void initData() {
        dataList = new ArrayList<Map<String, Object>>();
        DatabaseUtil DbUtils = new DatabaseUtil();
        showList = DbUtils.getShowList(getApplicationContext());

        for(int t=0;t<showList.size();t++) {
            String packageName = showList.get(t).getPackageName();
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packageName,0);
                showList.get(t).setIntent(pm.getLaunchIntentForPackage(packageName));
                showList.get(t).setLogo(packageInfo.applicationInfo.loadIcon(pm));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public  void gotoSetting()
    {
        Intent intent = new Intent(this,AppSetting.class);
        startActivity(intent);
    }
    //初始化已安装
    private void initApp() {
        AppList list = new AppList();
        list.queryFilterAppInfo(pm);

        ((MyApplication) getApplication()).AppList = list.appList;
        DatabaseUtil dbUtils = new DatabaseUtil();
        //dbUtils.clearApplist(getApplicationContext());
        //清理数据库中新增和卸载的app项
        if(dbUtils.getAppcount(getApplicationContext())>0) {
            List<AppInfo> nList = clearList(dbUtils.getAppList(getApplicationContext()),list.appList);
            dbUtils.updateApplist(getApplicationContext(), nList);
        }
        else
            dbUtils.updateApplist(getApplicationContext(), list.appList);
    }

    //赋值intent和icon
    public List<AppInfo> clearList(List<AppInfo> sqlList, List<AppInfo> inList) {
        int sqlSize = sqlList.size();
        int inSize = inList.size();

        for(int y=0;y<inSize;y++) {
            boolean ischeck=false;
            for(int x=0;x<sqlSize;x++ ) {
                if(sqlList.get(x).getPackageName().equals(inList.get(y).getPackageName())
                        &&sqlList.get(x).getName().equals(inList.get(y).getName())) {
                    ischeck=true;
                    sqlList.get(x).installStage=-1;
                    break;
                }
            }
            if(!ischeck)
                sqlList.add(inList.get(y));
        }
        return sqlList;
    }
}
