package com.elearning.tm.android.client.view.module;

import java.util.ArrayList;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.ItemInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SquareItemAdapter extends BaseAdapter {

	private static final String TAG = "SquareItemAdapter";
	protected ArrayList<ItemInfo> items;
	private Context tmContext;
	protected LayoutInflater tmInflater;

	private static class ViewHolder {
		public ImageView image; //已经在list的layout文件中存在默认选项
		public TextView text;
		public TextView action;
	}

	public SquareItemAdapter(Context context) {
		items = new ArrayList<ItemInfo>();
		tmContext = context;
		tmInflater = LayoutInflater.from(tmContext);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = tmInflater.inflate(R.layout.square_item_view, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.item_title);
			holder.action = (TextView) view.findViewById(R.id.item_action);
			view.setTag(holder);
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		int size = items.size() - 1;
		// list first
		if (position == 0) {
			ItemInfo item1 = items.get(0);
			holder.text.setText(item1.getTitle());
			holder.action.setText(item1.getAction());
			view.setBackgroundResource(R.drawable.circle_list_top);
		} else if (position == size) { // list end
			ItemInfo item3 = items.get(size);
			holder.text.setText(item3.getTitle());
			holder.action.setText(item3.getAction());
			view.setBackgroundResource(R.drawable.circle_list_bottom);
		} else {		// other
			ItemInfo item2 = items.get(position);
			holder.text.setText(item2.getTitle());
			holder.action.setText(item2.getAction());
			view.setBackgroundResource(R.drawable.circle_list_middle);
		}
		return view;
	}
	
	public void refresh(ArrayList<ItemInfo> label) {
		items = (ArrayList<ItemInfo>) label.clone();
		notifyDataSetChanged();
	}
}
