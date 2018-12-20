package com.example.xiash.mydesk;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class AppInfo {
    String Name="";
    Drawable Logo=null;
    String PackageName="";
    String VersionName="";
    int VersionCode=0;
    Intent intent=null;
    int appId=0;
    int isShow=1; //0隐藏1显示
    int installStage=0;	//是否卸载标识：-1原来的，0卸载的，1新装的
    public void setName (String arg) {
        Name = arg;
    }
    public String getName () {
        return Name;
    }

    public void setLogo (Drawable arg) {
        Logo = arg;
    }
    public Drawable getLogo () {
        return Logo;
    }

    public void setId (int arg) {
        appId = arg;
    }
    public int getId () {
        return appId;
    }

    public void setPackageName (String arg) {
        PackageName = arg;
    }
    public String getPackageName () {
        return PackageName;
    }

    public void setVersionName (String arg) {
        VersionName = arg;
    }
    public String getVersionName () {
        return VersionName;
    }

    public void setVersionCode (int arg) {
        VersionCode = arg;
    }
    public int getVersionCode () {
        return VersionCode;
    }

    public void setIntent (Intent arg) {
        intent = arg;
    }
    public Intent getIntent () {
        return intent;
    }

    public void setIsShow (int arg) {
        isShow = arg;
    }
    public int getIsShow () {
        return isShow;
    }

    public void setInstall (int arg) {
        installStage = arg;
    }
    public int getInstall () {
        return installStage;
    }
}
