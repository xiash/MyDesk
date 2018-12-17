package com.example.xiash.mydesk;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isAccessibilitySettingsOn(MainActivity.this, ListeningService.class.getCanonicalName())) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }

        {
            intent = new Intent(MainActivity.this, ListeningService.class);
            startService(intent);
        }

//        if (intent != null) {
//            stopService(intent);
//        }
    }
    //if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && accessibilityEvent.getPackageName().equals("com.android.systemui")&&(accessibili‌​tyEvent.getClassName().equals("com.android.systemui.statusbar.phone.PhoneStatusBa‌​r$ExpandedDialog")|| accessibilityEvent.getClassName().equals("android.widget.FrameLayout")||acc‌​essibilityEvent.getClassName().equals("com.android.systemui.statusbar.StatusBarSe‌​rvice$ExpandedDialog"))){
    //    // 你的代码
    //}
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        //sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }
    //禁止下拉
//    private void prohibitDropDown() {
//        manager = ((WindowManager) getApplicationContext()
//                .getSystemService(Context.WINDOW_SERVICE));
//        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
//        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        localLayoutParams.gravity = Gravity.TOP;
//        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//                // this is to enable the notification to recieve touch events
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                // Draws over status bar
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        localLayoutParams.height = (int) (50 * getResources()
//                .getDisplayMetrics().scaledDensity);
//        localLayoutParams.format = PixelFormat.TRANSPARENT;
//        view = new android.view.View(this);
//        manager.addView(view, localLayoutParams);
//    }
//
//    //允许下拉
//    private void allowDropDown() {
//     //   manager.removeView(view);
//    }


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
}
