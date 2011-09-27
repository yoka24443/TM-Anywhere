package com.elearning.tm.android.client.view.module;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.TaskInfo;



public class TMArrayAdapter extends BaseAdapter implements TmAdapter {
	private static final String TAG = "TMArrayAdapter";

	protected ArrayList<TaskInfo> tasks;
	private Context mContext;
	protected LayoutInflater mInflater;
	protected StringBuilder mMetaBuilder;

	
	protected static final int STATUS_WORKING = 0;
	protected static final int STATUS_COMPLETE = 1;
	protected static final int STATUS_UNSTART = 2;
	protected static final int STATUS_CANCEL = 3;

	public TMArrayAdapter(Context context) {
		tasks = new ArrayList<TaskInfo>();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mMetaBuilder = new StringBuilder();
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		public LinearLayout taskLayout;
		public TextView taskProject;
		public TextView taskText;
		public TextView metaText;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.task, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.taskLayout=(LinearLayout) view.findViewById(R.id.task_layout);
			holder.taskProject = (TextView) view
					.findViewById(R.id.task_project_text);
			holder.taskText = (TextView) view.findViewById(R.id.task_content_text);
			holder.metaText = (TextView) view
					.findViewById(R.id.task_meta_text);
			view.setTag(holder);
		} else {
			view = convertView;
		}

		ViewHolder holder = (ViewHolder) view.getTag();

		TaskInfo task = tasks.get(position);

		holder.taskProject.setText(task.getPTitle());
		holder.taskText.setText(task.getRemark());
		holder.metaText.setText(this.buildMetaText(mMetaBuilder, task));
		return view;
	}

	public String buildMetaText(StringBuilder builder, TaskInfo task){
		builder.setLength(0);

		builder.append(com.elearning.tm.android.client.util.DateTimeHelper.getRelativeDate(task.getBeginTime()));
		builder.append(" ");
		builder.append(com.elearning.tm.android.client.util.DateTimeHelper.getRelativeDate(task.getEndTime()));
		builder.append(" ");
		builder.append("共耗时");
		builder.append(task.getTotalTime());
		builder.append("小时");
		builder.append(" ");
		
		switch (task.getStatus()) {
		case STATUS_WORKING:
			builder.append("进行中");
			break;
		case STATUS_COMPLETE:
			builder.append("已完成");
			break;
		case STATUS_UNSTART:
				builder.append("未开始");
				break;
		case STATUS_CANCEL:
				builder.append("取消");
				break;
		}	
		return builder.toString();
	}
	
	public void refresh(ArrayList<TaskInfo> tasks) {
		tasks = tasks;
		notifyDataSetChanged();
	}

	@Override
	public void refresh() {
		notifyDataSetChanged();
	}
}
