package com.example.xiash.mydesk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class gridview_adapter extends BaseAdapter {
    private Context myContext;
    private LayoutInflater inflater;
    private DatabaseUtil dbUtils;
    private List<AppInfo> apps;

    public gridview_adapter(Context context, List<AppInfo> appList) {
        myContext=context;
        this.apps = appList;
        dbUtils = new DatabaseUtil();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.gridview_item, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.img);
                holder.name = (TextView) convertView.findViewById(R.id.text);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AppInfo appInfo = apps.get(i);
            holder.icon.setImageDrawable(appInfo.getLogo());
            holder.name.setText(appInfo.getName());
            holder.appName = appInfo.getName();
            holder.packageName = appInfo.getPackageName();
            convertView.setTag(holder);
        }catch(Exception e){
            String err = e.getMessage();
        }
        return convertView;
    }



    class ViewHolder {
        ImageView icon;
        TextView name;
        String appName;
        String packageName;
    }
    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int i) {
        return apps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
