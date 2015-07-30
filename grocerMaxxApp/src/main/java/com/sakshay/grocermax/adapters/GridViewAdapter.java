package com.sakshay.grocermax.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakshay.grocermax.R;

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> date_list;
    public static int selectedPosition = 0;

    public GridViewAdapter(Context c,ArrayList<String> date_list) {
        mContext = c;
        this.date_list = date_list;
    }

    public int getCount() {
        return date_list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

//    // create a new ImageView for each item referenced by the Adapter
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        imageView.setImageResource(mThumbIds[position]);
//        return imageView;
//    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
        	
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView ivGrid = (ImageView) grid.findViewById(R.id.iv_grid);
//            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            
//            if(position == 0){
//            	grid.setBackgroundResource(R.color.delivery_slot_selected_color);
//            }else{
//            	grid.setBackgroundResource(R.color.delivery_slot_unselected_color);
//            }
            
            textView.setText(date_list.get(position));
        } else {
            grid = (View) convertView;
        }
        
        
        System.out.println("======position==="+position);
//        if(position == selectedPosition){
//        	grid.setBackgroundResource(R.color.delivery_slot_selected_color);
//        }else{
//        	grid.setBackgroundResource(R.color.delivery_slot_unselected_color);
//        }
        
        return grid;

	}
	
}