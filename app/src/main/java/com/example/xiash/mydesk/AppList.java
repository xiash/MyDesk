package com.example.xiash.mydesk;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppList {
    public List<ApplicationInfo> applicationInfos=new ArrayList<>();//系统内安装的app列表
    public List<AppInfo> appList = new ArrayList<>();   //系统内安装的app列表

    private PackageManager pm = null;

    public void queryFilterAppInfo(PackageManager packageManager)
    {
        pm=packageManager;
        queryFilterAppInfo();
    }
    private void queryFilterAppInfo() {
        if(pm==null)
            return;
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> appInfos= pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        List<ResolveInfo>  resolveinfoList = pm.queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages=new HashSet();
        for (ResolveInfo resolveInfo:resolveinfoList){
            allowPackages.add(resolveInfo.activityInfo.packageName);
        }
        int i=0;
        for (ApplicationInfo app:appInfos) {
            if(isUserApp(app))
            {
                applicationInfos.add(app);
                i++;
                AppInfo appInfo = new AppInfo();
                appInfo.setName(app.loadLabel(pm).toString());
                appInfo.setPackageName(app.packageName);
                appInfo.setIntent(pm.getLaunchIntentForPackage(app.packageName));
                appInfo.setLogo(app.loadIcon(pm));
                appInfo.installStage = 1;
                appInfo.appId = i;

                appList.add(appInfo);
            }

//            if (allowPackages.contains(app.packageName)){
//                applicationInfos.add(app);
//            }
        }
    }

    public  String getPackgeName(String name)
    {
        if(pm==null)
            return  "";
        for(int i=0;i<applicationInfos.size();i++) {
            String str_name = applicationInfos.get(i).loadLabel(pm).toString();
            if (str_name == name)
                return applicationInfos.get(i).packageName;
        }
        return  "";
    }


    public boolean isSystemApp(ApplicationInfo pInfo) {
        //判断是否是系统软件
        return ((pInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
    public boolean isSystemUpdateApp(ApplicationInfo pInfo) {
        //判断是否是软件更新..
        return ((pInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }
    public boolean isUserApp(ApplicationInfo pInfo) {

        if(pInfo.packageName.contains("android"))
            return  false;
        if(pInfo.packageName.contains("com.example.xiash.mydesk"))
            return  false;
        //是否是系统软件或者是系统软件正在更新
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }

}
