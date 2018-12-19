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
    public gridview_button_adapter (Context context, List<Map<String, Object>> appList) {
        myContext=context;
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

        return null;
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

        //dbUtils.setVisible(myContext, name, showType);
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        //ToggleButton mTogBtn;
        Button button;
        int appId;
        String appName;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
