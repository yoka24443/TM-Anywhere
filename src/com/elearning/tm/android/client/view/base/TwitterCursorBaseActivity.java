package com.elearning.tm.android.client.view.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.TaskInfo;
import com.elearning.tm.android.client.task.GenericTask;
import com.elearning.tm.android.client.task.TaskAdapter;
import com.elearning.tm.android.client.task.TaskListener;
import com.elearning.tm.android.client.task.TaskManager;
import com.elearning.tm.android.client.task.TaskResult;
import com.elearning.tm.android.client.view.module.TMArrayAdapter;
import com.elearning.tm.android.client.view.module.TmAdapter;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * TwitterCursorBaseLine用于带有静态数据来源（对应数据库的，与twitter表同构的特定表）的展现
 */
public abstract class TwitterCursorBaseActivity extends TwitterListBaseActivity {
      
	static final String TAG = "TwitterCursorBaseActivity";
    // Views.
    protected PullToRefreshListView mTweetList;
    protected TMArrayAdapter TmAdapter;

    protected View mListHeader;
    protected View mListFooter;

    protected TextView loadMoreBtn;
    protected ProgressBar loadMoreGIF;
    protected TextView loadMoreBtnTop;
    protected ProgressBar loadMoreGIFTop;

    protected static int lastPosition = 0;

    // Tasks.
    protected TaskManager taskManager = new TaskManager();
    private GenericTask mRetrieveTask;
    private GenericTask mGetMoreTask;

    private int mRetrieveCount = 0;

    private TaskListener mRetrieveTaskListener = new TaskAdapter() {

        @Override
        public String getName() {
            return "RetrieveTask";
        }

        @Override
        public void onPostExecute(GenericTask task, TaskResult result) {
            // 刷新按钮停止旋转
            loadMoreGIF.setVisibility(View.GONE);
            mTweetList.onRefreshComplete();

            if (result == TaskResult.AUTH_ERROR) {
                mFeedback.failed("登录信息出错");
                logout();
            } else if (result == TaskResult.OK) {
                draw();
                if (task == mRetrieveTask) {
                    goTop();
                }
            } else if (result == TaskResult.IO_ERROR) {
                // FIXME: bad smell
                if (task == mRetrieveTask) {
                    mFeedback.failed(((RetrieveTask) task).getErrorMsg());
                } else if (task == mGetMoreTask) {
                    mFeedback.failed(((GetMoreTask) task).getErrorMsg());
                }
            } 
        }

        @Override
        public void onPreExecute(GenericTask task) {
            mRetrieveCount = 0;
            mTweetList.prepareForRefresh();
        }

        @Override
        public void onProgressUpdate(GenericTask task, Object param) {
            Log.d(TAG, "onProgressUpdate");
            draw();
        }
    };

    private TaskListener mFollowerRetrieveTaskListener = new TaskAdapter() {

        @Override
        public String getName() {
            return "FollowerRetrieve";
        }

        @Override
        public void onPostExecute(GenericTask task, TaskResult result) {
            if (result == TaskResult.OK) {

            } else {
                // Do nothing.
            }
        }
    };

    // Refresh data at startup if last refresh was this long ago or greater.
    private static final long REFRESH_THRESHOLD = 5 * 60 * 1000;

    // Refresh followers if last refresh was this long ago or greater.
    private static final long FOLLOWERS_REFRESH_THRESHOLD = 12 * 60 * 60 * 1000;

    abstract protected Cursor fetchMessages();

    public abstract int getDatabaseType();

    public abstract String getUserId();

    public abstract String fetchMaxId();

    public abstract String fetchMinId();

    public abstract int addMessages(ArrayList<TaskInfo> tweets, boolean isUnread);

    public abstract List<TaskInfo> getMessageSinceId(String maxId);

    public abstract List<TaskInfo> getMoreMessageFromId(String minId);

    @Override
    protected void setupState() {
        Cursor cursor;

        cursor = fetchMessages(); // getDb().fetchMentions();
        setTitle(getActivityTitle());
        startManagingCursor(cursor);

        mTweetList = (PullToRefreshListView) findViewById(R.id.tweet_list);

        // TODO: 需处理没有数据时的情况
        Log.d("LDS", cursor.getCount() + " cursor count");
        setupListHeader(true);

        TmAdapter = new TweetCursorAdapter(this, cursor);
        mTweetList.setAdapter(TmAdapter);
    }

    /**
     * 绑定listView底部 - 载入更多 NOTE: 必须在listView#setAdapter之前调用
     */
    protected void setupListHeader(boolean addFooter) {
    	mTweetList.setOnRefreshListener(new OnRefreshListener(){
    		@Override
    		public void onRefresh(){
    			doRetrieve();
    		}
    	});

        // Add Footer to ListView
        mListFooter = View.inflate(this, R.layout.listview_footer, null);
        mTweetList.addFooterView(mListFooter, null, true);
        
        // Find View
        loadMoreBtn = (TextView) findViewById(R.id.ask_for_more);
        loadMoreGIF = (ProgressBar) findViewById(R.id.rectangleProgressBar);
//        loadMoreBtnTop = (TextView) findViewById(R.id.ask_for_more_header);
//        loadMoreGIFTop = (ProgressBar) findViewById(R.id.rectangleProgressBar_header);

    }

    @Override
    protected void specialItemClicked(int position) {
        // 注意 mTweetAdapter.getCount 和 mTweetList.getCount的区别
        // 前者仅包含数据的数量（不包括foot和head），后者包含foot和head
        // 因此在同时存在foot和head的情况下，list.count = adapter.count + 2
        if (position == 0) {
            // 第一个Item(header)
            loadMoreGIFTop.setVisibility(View.VISIBLE);
            doRetrieve();
        } else if (position == mTweetList.getCount() - 1) {
            // 最后一个Item(footer)
            loadMoreGIF.setVisibility(View.VISIBLE);
            doGetMore();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main;
    }

    @Override
    protected ListView getTMList() {
        return mTweetList;
    }

    @Override
    protected TmAdapter getTMAdapter() {
        return TmAdapter;
    }


    @Override
    protected TaskInfo getContextItemTask(int position) {
        position = position - 1;
        // 因为List加了Header和footer，所以要跳过第一个以及忽略最后一个
        if (position >= 0 && position < TmAdapter.getCount()) {
            Cursor cursor = (Cursor) TmAdapter.getItem(position);
            if (cursor == null) {
                return null;
            } else {
                return StatusTable.parseCursor(cursor);
            }
        } else {
            return null;
        }
    }

    @Override
    protected void updateTask(TaskInfo task) {

    }

    @Override
    protected boolean _onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate.");
        if (super._onCreate(savedInstanceState)) {
            boolean shouldRetrieve = false;
            if (shouldRetrieve) {
                doRetrieve();
            }
            goTop(); // skip the header
            return true;
        } else {
            return false;
        }

    }

	@Override
    protected void onResume() {
        Log.d(TAG, "onResume.");
        if (lastPosition != 0) {
            mTweetList.setSelection(lastPosition);
        }
        super.onResume();
        checkIsLogedIn();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRetrieveTask != null
                && mRetrieveTask.getStatus() == GenericTask.Status.RUNNING) {
            outState.putBoolean(SIS_RUNNING_KEY, true);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy.");
        super.onDestroy();
        taskManager.cancelAll();

        // 刷新按钮停止旋转
        if (loadMoreGIF != null){
        	loadMoreGIF.setVisibility(View.GONE);
        }
        if (mTweetList != null){
        	mTweetList.onRefreshComplete();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause.");
        super.onPause();
        lastPosition = mTweetList.getFirstVisiblePosition();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart.");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart.");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop.");
        super.onStop();
    }

    // UI helpers.

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void adapterRefresh() {
        TmAdapter.notifyDataSetChanged();
        TmAdapter.refresh();
    }

    // Retrieve interface
    public void updateProgress(String progress) {
        mProgressText.setText(progress);
    }

    public void draw() {
        TmAdapter.refresh();
    }

    public void goTop() {
        Log.d(TAG, "goTop.");
        mTweetList.setSelection(1);
    }

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

    private class RetrieveTask extends GenericTask {
        private String _errorMsg;

        public String getErrorMsg() {
            return _errorMsg;
        }

        @Override
        protected TaskResult _doInBackground(TaskParams... params) {
            List<TaskInfo> tasksList;
            try {
                String maxId = fetchMaxId(); 
                statusList = getMessageSinceId(maxId);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                _errorMsg = e.getMessage();
                return TaskResult.IO_ERROR;
            }

            ArrayList<TaskInfo> tasks = new ArrayList<TaskInfo>();
            for (TaskInfo status : statusList) {
                if (isCancelled()) {
                    return TaskResult.CANCELLED;
                }
                tweets.add(Tweet.create(status));
                if (isCancelled()) {
                    return TaskResult.CANCELLED;
                }
            }
            publishProgress(SimpleFeedback.calProgressBySize(40, 20, tweets));
            mRetrieveCount = addMessages(tweets, false);

            return TaskResult.OK;
        }

    }

    // GET MORE TASK
    private class GetMoreTask extends GenericTask {
        private String _errorMsg;

        public String getErrorMsg() {
            return _errorMsg;
        }

        @Override
        protected TaskResult _doInBackground(TaskParams... params) {
            List<TaskInfo> statusList;
            String minId = fetchMinId(); 
            if (minId == null) {
                return TaskResult.FAILED;
            }

            try {
                statusList = getMoreMessageFromId(minId);
            } catch (HttpException e) {
                Log.e(TAG, e.getMessage(), e);
                _errorMsg = e.getMessage();
                return TaskResult.IO_ERROR;
            }

            if (statusList == null) {
                return TaskResult.FAILED;
            }

            ArrayList<TaskInfo> tweets = new ArrayList<TaskInfo>();
            publishProgress(SimpleFeedback.calProgressBySize(40, 20, tweets));

            for (TaskInfo tasks : statusList) {
                if (isCancelled()) {
                    return TaskResult.CANCELLED;
                }

                tweets.add(Tweet.create(status));

                if (isCancelled()) {
                    return TaskResult.CANCELLED;
                }
            }

            addMessages(tweets, false); // getDb().addMentions(tweets, false);

            return TaskResult.OK;
        }
    }

    public void doGetMore() {
        Log.d(TAG, "Attempting getMore.");

        if (mGetMoreTask != null
                && mGetMoreTask.getStatus() == GenericTask.Status.RUNNING) {
            return;
        } else {
            mGetMoreTask = new GetMoreTask();
            mGetMoreTask.setFeedback(mFeedback);
            mGetMoreTask.setListener(mRetrieveTaskListener);
            mGetMoreTask.execute();
            // Add Task to manager
            taskManager.addTask(mGetMoreTask);
        }
    }
}