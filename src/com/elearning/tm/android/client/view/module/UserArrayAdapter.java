package com.elearning.tm.android.client.view.module;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
		public TextView followBtn;
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
			holder.followBtn = (TextView) view.findViewById(R.id.follow_btn);
			view.setTag(holder);
		} else {
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
		holder.followBtn.setText("发送短信");

		holder.followBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 发送短信
				Uri uri = Uri.parse("smsto:" + user.getMobile());            
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);            
				it.putExtra("sms_body", user.getUserAccount() + ", 你好:");            
				tmContext.startActivity(it);  
			}
		});
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
