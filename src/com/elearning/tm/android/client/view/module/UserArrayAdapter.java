package com.elearning.tm.android.client.view.module;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.app.Preferences;
import com.elearning.tm.android.client.app.TMApplication;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskParams;
import com.elearning.tm.android.client.task.TaskResult;



/*
 * 用于用户的Adapter
 */
public class UserArrayAdapter extends BaseAdapter implements TmAdapter {
	private static final String TAG = "UserArrayAdapter";
	private static final String USER_ID = "userId";

	protected ArrayList<UserInfo> mUsers;
	private Context mContext;
	protected LayoutInflater mInflater;

	public UserArrayAdapter(Context context) {
		mUsers = new ArrayList<UserInfo>();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mUsers.size();
	}

	@Override
	public Object getItem(int position) {
		return mUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		public ImageView profileImage;
		public TextView screenName;
		public TextView userId;
		public TextView lastStatus;
		public TextView followBtn;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		SharedPreferences pref = TMApplication.tmPref; 
//		boolean useProfileImage = pref.getBoolean(
//				Preferences.USE_PROFILE_IMAGE, true);

		if (convertView == null) {
			view = mInflater.inflate(R.layout.follower_item, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.profileImage = (ImageView) view
					.findViewById(R.id.profile_image);
			holder.screenName = (TextView) view.findViewById(R.id.screen_name);
			holder.userId = (TextView) view.findViewById(R.id.user_id);
			holder.followBtn = (TextView) view.findViewById(R.id.follow_btn);

			view.setTag(holder);
		} else {
			view = convertView;
		}

		ViewHolder holder = (ViewHolder) view.getTag();

		final UserInfo user = mUsers.get(position);

		//String profileImageUrl = user.profileImageUrl;
//		if (useProfileImage) {
//			if (!TextUtils.isEmpty(profileImageUrl)) {
//				holder.profileImage
//						.setImageBitmap(TMApplication.mImageLoader.get(
//								profileImageUrl, callback));
//			}
//		} else {
//			holder.profileImage.setVisibility(View.GONE);
//		}
		// holder.profileImage.setImageBitmap(ImageManager.mDefaultBitmap);
//		holder.screenName.setText(user.screenName);
//		holder.userId.setText(user.id);
		// holder.lastStatus.setText(user.lastStatus);

//		holder.followBtn.setText(user.isFollowing ? mContext
//				.getString(R.string.general_del_friend) : mContext
//				.getString(R.string.general_add_friend));
//
//		holder.followBtn
//				.setOnClickListener(user.isFollowing ? new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// Toast.makeText(mContext, user.name+"following",
//						// Toast.LENGTH_SHORT).show();
//						delFriend(user.id,v);
//					}
//				} : new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// Toast.makeText(mContext, user.name+"not following",
//						// Toast.LENGTH_SHORT).show();
//
//						addFriend(user.id,v);
//					}
//				});
		return view;
	}

	public void refresh(ArrayList<UserInfo> users) {
		mUsers = (ArrayList<UserInfo>) users.clone();
		notifyDataSetChanged();
	}

	@Override
	public void refresh() {
		notifyDataSetChanged();
	}

//	private ImageLoaderCallback callback = new ImageLoaderCallback() {
//		@Override
//		public void refresh(String url, Bitmap bitmap) {
//			UserArrayAdapter.this.refresh();
//		}
//	};

	/**
	 * 取消关注
	 * 
	 * @param id
	 */
	private void delFriend(final String id,final View v) {
		Builder diaBuilder = new AlertDialog.Builder(mContext).setTitle("关注提示")
				.setMessage("确实要取消关注吗?");
		diaBuilder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (cancelFollowingTask != null
								&& cancelFollowingTask.getStatus() == GenericTask.Status.RUNNING) {
							return;
						} else {
							cancelFollowingTask = new CancelFollowingTask();
							cancelFollowingTask.setListener(new TaskAdapter() {//闭包？
								@Override
								public void onPostExecute(GenericTask task,
										TaskResult result) {
									if (result == TaskResult.OK) {
										//添加成功后动态改变按钮
										TextView followBtn=(TextView)v.findViewById(R.id.follow_btn);
										followBtn.setText("添加关注");
										followBtn.setOnClickListener( new OnClickListener() {

											@Override
											public void onClick(View view) {
												addFriend(id,view);
											}
										});
										Toast.makeText(mContext, "取消关注成功",
												Toast.LENGTH_SHORT).show();

									} else if (result == TaskResult.FAILED) {
										Toast.makeText(mContext, "取消关注失败",
												Toast.LENGTH_SHORT).show();
									}
								}

								@Override
								public String getName() {
									
									return null;
								}

							});
							TaskParams params = new TaskParams();
							params.put(USER_ID, id);

							cancelFollowingTask.execute(params);
						}
					}
				});
		diaBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		Dialog dialog = diaBuilder.create();
		dialog.show();
	}

	private GenericTask cancelFollowingTask;

	/**
	 * 取消关注
	 * 
	 * @author Dino
	 * 
	 */
	private class CancelFollowingTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
//			try {
				
				String userId = params[0].getString(USER_ID);
			
//				getApi().destroyFriendship(userId);
//			} catch (HttpException e) {
				Log.w(TAG, "create friend ship error");
				return TaskResult.FAILED;
//			}
//			return TaskResult.OK;
		}

	}

//	private TaskListener cancelFollowingTaskLinstener = new TaskAdapter() {
//		@Override
//		public void onPostExecute(GenericTask task, TaskResult result) {
//			if (result == TaskResult.OK) {
//				Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
//
//			} else if (result == TaskResult.FAILED) {
//				Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
//			}
//		}
//
//		@Override
//		public String getName() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//	};

	private GenericTask setFollowingTask;

	/**
	 * 设置关注
	 * 
	 * @param id
	 */
	private void addFriend(final String id,final View view) {
		Builder diaBuilder = new AlertDialog.Builder(mContext).setTitle("关注提示")
				.setMessage("确实要添加关注吗?");
		diaBuilder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (setFollowingTask != null
								&& setFollowingTask.getStatus() == GenericTask.Status.RUNNING) {
							return;
						} else {
							setFollowingTask = new SetFollowingTask();
							setFollowingTask
									.setListener(new TaskAdapter() {

										@Override
										public void onPostExecute(GenericTask task, TaskResult result) {
											if (result == TaskResult.OK) {

												//添加成功后动态改变按钮
												TextView followBtn=(TextView)view.findViewById(R.id.follow_btn);
												followBtn.setText("取消关注");
												followBtn.setOnClickListener( new OnClickListener() {

													@Override
													public void onClick(View v) {
														delFriend(id,v);
													}
												});
												
												Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();

											} else if (result == TaskResult.FAILED) {
												Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
											}
										}

										@Override
										public String getName() {
											// TODO Auto-generated method stub
											return null;
										}

									});
							TaskParams params = new TaskParams();
							params.put(USER_ID, id);
							setFollowingTask.execute(params);
						}

					}
				});
		diaBuilder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		Dialog dialog = diaBuilder.create();
		dialog.show();
	}

	/**
	 * 设置关注
	 * 
	 * @author Dino
	 * 
	 */
	private class SetFollowingTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

//			try {
				String userId = params[0].getString(USER_ID);
//				getApi().createFriendship(userId);

//			} catch (HttpException e) {
//				Log.w(TAG, "create friend ship error");
//				
//				return TaskResult.FAILED;
//			}

			return TaskResult.OK;
		}

	}

//	private TaskListener setFollowingTaskLinstener = new TaskAdapter() {
//
//		@Override
//		public void onPostExecute(GenericTask task, TaskResult result) {
//			if (result == TaskResult.OK) {
//
//				Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
//
//			} else if (result == TaskResult.FAILED) {
//				Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
//			}
//		}
//
//		@Override
//		public String getName() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//	};

//	public Weibo getApi() {
//		return TMApplication.mApi;
//	}

}
