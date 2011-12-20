package com.elearning.tm.android.client.view.module;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.view.base.TwitterListBaseActivity;

public class ListItemAdapter extends BaseAdapter {

	private int[] imgIds = {R.drawable.ic_pop_reply, R.drawable.ic_pop_share, R.drawable.ic_pop_delete};
	private Context tmContext;
	
	public ListItemAdapter(Context tmContext) {
		super();
		this.tmContext = tmContext;
	}
	@Override
	public int getCount() {
		return imgIds.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, 
			View contentView, ViewGroup parent) {
		TextView textView = 
			new TextView(tmContext);
		//获得array.xml中的数组资源getStringArray返回的是一个String数组
		String text = tmContext.getResources().getStringArray(R.array.hobby)[position];
		textView.setText(text);
		//设置字体大小
		textView.setTextSize(24);
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(layoutParams);
		//设置水平方向上居中
		textView.setGravity(android.view.Gravity.CENTER_VERTICAL);
		textView.setMinHeight(65);
		//设置文字颜色
		textView.setTextColor(Color.BLACK);  
		//设置图标在文字的左边
		textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position], 0, 0, 0);
		//设置textView的左上右下的padding大小
		textView.setPadding(15, 0, 15, 0);
		//设置文字和图标之间的padding大小
		textView.setCompoundDrawablePadding(15);
		return textView;
	}
	
	
}
