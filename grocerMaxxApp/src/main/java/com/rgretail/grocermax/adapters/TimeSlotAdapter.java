package com.rgretail.grocermax.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.TimeSlotStatus;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 25-Jan-17.
 */
public class TimeSlotAdapter extends BaseAdapter {

    Context con;
    ArrayList<TimeSlotStatus> dataList;


    public TimeSlotAdapter(Context con, ArrayList<TimeSlotStatus> dataList) {
        this.con = con;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.time_slot_listitem,null);
        }
        TextView tv=(TextView)convertView.findViewById(R.id.tv);
        tv.setText(dataList.get(position).getData());

        if(dataList.get(position).isStatus())
            tv.setTextColor(con.getResources().getColor(R.color.app_header));
        else
            tv.setTextColor(Color.parseColor("#808080"));

        return convertView;
    }
}
