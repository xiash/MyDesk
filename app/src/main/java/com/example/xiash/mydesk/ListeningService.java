package com.example.xiash.mydesk;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class ListeningService extends AccessibilityService {
    private static final String TAG = "WindowChange";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        //配置监听的事件类型为界面变化|点击事件
        config.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;//AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_CLICKED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        if (Build.VERSION.SDK_INT >= 16) {
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        }
        setServiceInfo(config);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
       // if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//                && event.getPackageName().equals("com.android.systemui")
//                &&(event.getClassName().equals("com.android.systemui.statusbar.phone.PhoneStatusBa‌​r$ExpandedDialog")
//                    || event.getClassName().equals("android.widget.FrameLayout")
//                    ||event.getClassName().equals("com.android.systemui.statusbar.StatusBarSe‌​rvice$ExpandedDialog")
//                   )
 //               )

        {
            sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        }
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
    }

}
