package com.elearning.tm.android.client.view.module;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.UserInfo;

/*
 * 用于用户的Adapter
 */
public class UserArrayAdapter extends BaseAdapter implements TmAdapter {
	
	private static final String TAG = "UserArrayAdapter";
	protected ArrayList<UserInfo> tmUsers;
	private Context tmContext;
	protected LayoutInflater tmInflater;
	private static String selectedUser;
	public UserArrayAdapter(Context context) {
		tmUsers = new ArrayList<UserInfo>();
		tmContext = context;
		tmInflater = LayoutInflater.from(tmContext);
	}

	@Override
	public int getCount() {
		return tmUsers.size();
	}

	@Override
	public Object getItem(int position) {
		return tmUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		public ImageView profileImage;
		public TextView screenName;
		public TextView userPhone;
		public TextView userMail;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = tmInflater.inflate(R.layout.follower_item, parent, false);
			ViewHolder holder = new ViewHolder();
			holder.profileImage = (ImageView) view.findViewById(R.id.profile_image);
			holder.screenName = (TextView) view.findViewById(R.id.screen_name);
			holder.userPhone = (TextView) view.findViewById(R.id.user_phone);
			holder.userMail = (TextView) view.findViewById(R.id.user_mail);
			view.setTag(holder);
		} else {
			View buttons = (View) convertView.findViewById(R.id.call_log_expand);
			if(buttons != null && buttons.getVisibility() == View.VISIBLE){
				TextView user = (TextView)((View)(buttons.getParent())).findViewById(R.id.screen_name);
				selectedUser = user.getText().toString();
				buttons.setVisibility(View.GONE);
			}
			view = convertView;
		}
		
		ViewHolder holder = (ViewHolder) view.getTag();
		final UserInfo user = tmUsers.get(position);
//		如果需要添加avatar
//		String profileImageUrl = user.getHeader();
//		if(TextUtils.isEmpty(profileImageUrl)){
//			holder.profileImage.setImageBitmap();
//		}else
//			holder.profileImage.setImageBitmap();
		holder.profileImage.setBackgroundResource(R.drawable.default_avatar);
		holder.screenName.setText(user.getUserAccount());
		holder.userPhone.setText(user.getMobile());
		holder.userMail.setText(user.getEmail());
		View buttons = (View) view.findViewById(R.id.call_log_expand);
		if(buttons != null  && user.getUserAccount().equals(selectedUser)){
			buttons.setVisibility(View.VISIBLE);
		}
		
		return view;
	}

	public void refresh(ArrayList<UserInfo> users) {
		tmUsers = (ArrayList<UserInfo>) users.clone();
		notifyDataSetChanged();
	}

	@Override
	public void refresh() {
		notifyDataSetChanged();
	}

}
