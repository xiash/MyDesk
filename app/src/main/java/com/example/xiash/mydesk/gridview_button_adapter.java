package com.example.xiash.mydesk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class gridview_button_adapter extends BaseAdapter implements View.OnClickListener {
    private Context myContext;
    private LayoutInflater inflater;
    private DatabaseUtil dbUtils;
    private List<AppInfo> apps;

    public gridview_button_adapter (Context context, List<AppInfo> appList) {
        myContext=context;
        this.apps = appList;
        dbUtils = new DatabaseUtil();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.gridview_button_item, viewGroup);
            holder.icon = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.text);
            holder.button = (Button) convertView.findViewById(R.id.show_btn);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = apps.get(i);
        holder.icon.setImageDrawable(appInfo.getLogo());
        holder.name.setText(appInfo.getName());

        if(appInfo.getIsShow()==1)
            holder.button.setText("隐藏");
        else
            holder.button.setText("显示");
        holder.button.setOnClickListener(this);
        holder.appName = appInfo.getName();
        holder.packageName = appInfo.getPackageName();

        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        String btnText = b.getText().toString();
        int showType = 1;
        //按隐藏按钮时，不显示
        if(btnText.equals("隐藏")) {
            showType = 0;
            b.setText("显示");
        } else { //显示
            showType = 1;
            b.setText("隐藏");
        }
        View convertView = (View)view.getParent();
        ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        String name = viewHolder.appName;
        String packageName = viewHolder.packageName;
        dbUtils.setVisible(myContext, name,packageName, showType);
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        Button button;
        int appId;
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
