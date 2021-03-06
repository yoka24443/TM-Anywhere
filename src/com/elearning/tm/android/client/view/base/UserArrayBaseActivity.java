package com.elearning.tm.android.client.view.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.Paging;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskListener;
import com.elearning.tm.android.client.task.TaskManager;
import com.elearning.tm.android.client.task.TaskParams;
import com.elearning.tm.android.client.task.TaskResult;
import com.elearning.tm.android.client.view.module.SimpleFeedback;
import com.elearning.tm.android.client.view.module.TmAdapter;
import com.elearning.tm.android.client.view.module.UserArrayAdapter;

public abstract class UserArrayBaseActivity extends UserListBaseActivity {
	static final String TAG = "UserArrayBaseActivity";

	protected ListView mUserList;
	protected UserArrayAdapter mUserListAdapter;

	protected TextView loadMoreBtn;
	protected ProgressBar loadMoreGIF;
	protected TextView loadMoreBtnTop;
	protected ProgressBar loadMoreGIFTop;
	private ImageButton search;
	private View otherInflatedView;
	private Button btn_call;
	private Button btn_sendMsg;
	private Button btn_sendMail;
	protected static int lastPosition = 0;
	
	
	
	// Tasks.
	protected TaskManager taskManager = new TaskManager();
	private GenericTask mRetrieveTask;
	private GenericTask mGetMoreTask;// 每次20用户

	public abstract Paging getCurrentPage();// 加载

	public abstract Paging getNextPage();// 加载

	protected abstract List<UserInfo> getUsers(Paging page);

	private ArrayList<UserInfo> allUserList;

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate.");
		if (super._onCreate(savedInstanceState)) {
//			doRetrieve();// 加载第一页
			registerOnClickListener(getUserList());
			search = (ImageButton) this.findViewById(R.id.search);
			search.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					allUserList.clear(); // 搜索清空原有数据
					allUserList.addAll(getUsers(new Paging(1)));
					draw();
				}
			});
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doRetrieve() {
		Log.d(TAG, "Attempting retrieve.");

		if (mRetrieveTask != null
				&& mRetrieveTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mRetrieveTask = new RetrieveTask();
			mRetrieveTask.setFeedback(mFeedback);
			mRetrieveTask.setListener(mRetrieveTaskListener);
			mRetrieveTask.execute();
			// Add Task to manager
			taskManager.addTask(mRetrieveTask);
		}

	}

	private TaskListener mRetrieveTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			return "RetrieveTask";
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			if (result == TaskResult.AUTH_ERROR) {
				logout();
			} else if (result == TaskResult.OK) {
				draw();
			}
			updateProgress("");
		}

		@Override
		public void onPreExecute(GenericTask task) {
			onRetrieveBegin();
		}

		@Override
		public void onProgressUpdate(GenericTask task, Object param) {
			Log.d(TAG, "onProgressUpdate");
			draw();
		}
	};

	public void updateProgress(String progress) {
		mProgressText.setText(progress);
	}

	public void onRetrieveBegin() {
		updateProgress(getString(R.string.page_status_refreshing));
	}

	/**
	 * TODO：从API获取当前Followers
	 * 
	 * @author Dino
	 * 
	 */
	private class RetrieveTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			Log.d(TAG, "load RetrieveTask");

			List<UserInfo> usersList = null;
			try {
				usersList = getUsers(getCurrentPage());
			} catch (Exception e) {
				e.printStackTrace();
				return TaskResult.IO_ERROR;
			}
			publishProgress(SimpleFeedback.calProgressBySize(40, 20, usersList));
			allUserList.clear();
			for (UserInfo user : usersList) {
				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}
				allUserList.add(user);

				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}
			}

			return TaskResult.OK;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIsLogedIn();
		doRetrieve();// 加载第一页
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.follower;
	}

	@Override
	protected void setupState() {
		setTitle(getActivityTitle());

		mUserList = (ListView) findViewById(R.id.follower_list);

		setupListHeader(true);

		mUserListAdapter = new UserArrayAdapter(this);
		mUserList.setAdapter(mUserListAdapter);
		allUserList = new ArrayList<UserInfo>();
	}

	@Override
	protected String getActivityTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean useBasicMenu() {
		return true;
	}

	@Override
	protected UserInfo getContextItemUser(int position) {
		Log.d(TAG, "list position:" + position);
		// 加入footer跳过footer
		if (position >= 0 && position < mUserListAdapter.getCount()) {
			UserInfo item = (UserInfo) mUserListAdapter.getItem(position);
			if (item == null) {
				return null;
			} else {
				return item;
			}
		} else {
			return null;
		}
	}

	protected void registerOnClickListener(ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				UserInfo user = getContextItemUser(position);
				if (user == null) {
					Log.w(TAG, "selected item not available");
					specialItemClicked(position);
				} else {
					View inflatedView = (View) view.findViewById(R.id.call_log_expand);
					if (otherInflatedView != null && otherInflatedView != inflatedView) {
						otherInflatedView.setVisibility(View.GONE);
					}
					if (inflatedView == null) {
						ViewStub mViewStub = (ViewStub) view.findViewById(R.id.stub_call_detail);
						mViewStub.setVisibility(View.VISIBLE);
						
						Log.v("get", view.findViewById(R.id.btn_call).toString());
						Log.v("position", String.valueOf(position));
						Log.v("id", String.valueOf(id));
						
						otherInflatedView = (View) view.findViewById(R.id.call_log_expand);
					} else {
						otherInflatedView = inflatedView;
						if (inflatedView.getVisibility() == View.GONE) {
							inflatedView.setVisibility(View.VISIBLE);
							
							Log.v("get", view.findViewById(R.id.btn_call).toString());
							Log.v("position", String.valueOf(position));
							Log.v("id", String.valueOf(id));
							
						} else if (inflatedView.getVisibility() == View.VISIBLE) {
							inflatedView.setVisibility(View.GONE);
						}

					}
					bindButtonEvent(view,  user);
				}
			}
		});
	}
	
	//绑定iistitem中button的事件
	private void bindButtonEvent(View view, final UserInfo user){
		View inflatedView = (View) view.findViewById(R.id.call_log_expand);
		if(inflatedView != null){
			btn_call = (Button)view.findViewById(R.id.btn_call);
			btn_sendMsg = (Button)view.findViewById(R.id.btn_sms);
			btn_sendMail = (Button)view.findViewById(R.id.btn_detail);
			btn_call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 打电话
					  Intent myIntentDial = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+user.getMobile())  
	                  );  
					startActivity(myIntentDial);  
				}
			});
			
			btn_sendMsg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 发送短信
					Uri uri = Uri.parse("smsto:" + user.getMobile());            
					Intent it = new Intent(Intent.ACTION_SENDTO, uri);            
					it.putExtra("sms_body", user.getUserAccount() + ", 你好:");            
					startActivity(it);  
				}
			});
			
			btn_sendMail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 发送邮件
					 String[] receiver = new String[]{user.getEmail()};
					 Intent it = new Intent(Intent.ACTION_SEND);  
					     it.putExtra(Intent.EXTRA_EMAIL, receiver);  
					     it.putExtra(Intent.EXTRA_TEXT, user.getUserAccount() + ", 你好:");  
					     it.setType("text/plain");  
					     startActivity(Intent.createChooser(it, "选择邮件客户端"));
				}
			});
			
		}
	}

	@Override
	protected ListView getUserList() {
		return mUserList;
	}

	@Override
	protected TmAdapter getUserAdapter() {
		return mUserListAdapter;
	}

	/**
	 * 绑定listView底部 - 载入更多 NOTE: 必须在listView#setAdapter之前调用
	 */
	protected void setupListHeader(boolean addFooter) {
		// Add footer to Listview
		View footer = View.inflate(this, R.layout.listview_footer, null);
		mUserList.addFooterView(footer, null, true);
		// Find View
		loadMoreBtn = (TextView) findViewById(R.id.ask_for_more);
		loadMoreGIF = (ProgressBar) findViewById(R.id.rectangleProgressBar);
	}

	@Override
	protected void specialItemClicked(int position) {
		if (position == mUserList.getCount() - 1) {
			// footer
			loadMoreGIF.setVisibility(View.VISIBLE);
			doGetMore();
		}
	}

	public void doGetMore() {
		Log.d(TAG, "Attempting getMore.");
		mFeedback.start("");

		if (mGetMoreTask != null
				&& mGetMoreTask.getStatus() == GenericTask.Status.RUNNING) {
			return;
		} else {
			mGetMoreTask = new GetMoreTask();
			mGetMoreTask.setListener(getMoreListener);
			mGetMoreTask.execute();

			// Add Task to manager
			taskManager.addTask(mGetMoreTask);
		}
	}

	private TaskListener getMoreListener = new TaskAdapter() {

		@Override
		public String getName() {
			return "getMore";
		}

		@Override
		public void onPostExecute(GenericTask task, TaskResult result) {
			super.onPostExecute(task, result);
			draw();
			mFeedback.success("");
			loadMoreGIF.setVisibility(View.GONE);
		}

	};

	/**
	 * TODO:需要重写,获取下一批用户,按页分100页一次
	 * 
	 * @author Dino
	 * 
	 */
	private class GetMoreTask extends GenericTask {
		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			Log.d(TAG, "load RetrieveTask");

			List<UserInfo> usersList = null;
			try {
				usersList = getUsers(getNextPage());
				mFeedback.update(60);
			} catch (Exception e) {
				e.printStackTrace();
				return TaskResult.IO_ERROR;
			}
			// 将获取到的数据(保存/更新)到数据库
			// getDb().syncWeiboUsers(usersList);

			mFeedback.update(100 - (int) Math.floor(usersList.size() * 2));
			for (UserInfo user : usersList) {
				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}
				allUserList.add(user);
				if (isCancelled()) {
					return TaskResult.CANCELLED;
				}
			}
			mFeedback.update(99);
			return TaskResult.OK;
		}
	}

	public void draw() {
		mUserListAdapter.refresh(allUserList);
	}

}
