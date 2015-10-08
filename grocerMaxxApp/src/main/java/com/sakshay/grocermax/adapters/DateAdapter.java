package com.sakshay.grocermax.adapters;

import java.util.ArrayList;


import com.sakshay.grocermax.R;
import com.sakshay.grocermax.exception.GrocermaxBaseException;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DateAdapter extends BaseAdapter{
	
	Context con;
	ArrayList<String> list;
	boolean[] status;
	
	public DateAdapter(Context con,ArrayList<String> list) {
		// TODO Auto-generated constructor stub
		try{
			this.con=con;
			this.list=list;
			status=new boolean[list.size()];
			for(int i=0;i<list.size();i++)
				status[i]=false;

			status[0]=true;
		}catch(Exception e){
			new GrocermaxBaseException("DateAdapter","DateAdapter",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		View v=view;
		try{
		if(v==null)
		{
			LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.single_text_listitem, null);
		}
		final TextView tv=(TextView)v.findViewById(R.id.textView1);
		tv.setText(list.get(position));
		/*v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((ChooseAddress)con).changetimeSlot(position);
				for(int i=0;i<list.size();i++)
				{
					status[i]=false;
					tv.setBackgroundColor(((Activity)con).getResources().getColor(R.color.orange_text));
				}
				status[position]=true;
				tv.setBackgroundColor(((Activity)con).getResources().getColor(R.color.blue));
				
			}
		});
		if(status[position]==true)
			tv.setBackgroundColor(((Activity)con).getResources().getColor(R.color.orange_text));
		else
			tv.setBackgroundColor(((Activity)con).getResources().getColor(R.color.blue));*/


		}catch(Exception e){
			new GrocermaxBaseException("DateAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return v;
	}

}
