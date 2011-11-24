package com.elearning.tm.android.client.view.module;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.util.NumberHelper;
import com.elearning.tm.android.client.view.TaskAddActivity;

public class BaseCalendar extends Activity implements OnTouchListener {

	//判断手势用
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	//动画
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	private ViewFlipper viewFlipper;
	GestureDetector mGesture = null;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGesture.onTouchEvent(event);
	}

	AnimationListener animationListener=new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			//当动画完成后调用
			CreateGirdView();
		}
	};

	class GestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE	&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
					setNextViewItem();
					//CreateGirdView();
					return true;

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
					setPrevViewItem();
					//CreateGirdView();
					return true;

				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			//得到当前选中的是第几个单元格
			int pos = gView2.pointToPosition((int) e.getX(), (int) e.getY());
			LinearLayout txtDay = (LinearLayout) gView2.findViewById(pos + 5000);
			if (txtDay != null) {
				if (txtDay.getTag() != null) {
					Date date = (Date) txtDay.getTag();
					calSelected.setTime(date);

					gAdapter.setSelectedDate(calSelected);
					gAdapter.notifyDataSetChanged();

					gAdapter1.setSelectedDate(calSelected);
					gAdapter1.notifyDataSetChanged();

					gAdapter3.setSelectedDate(calSelected);
					gAdapter3.notifyDataSetChanged();
				}
			}

			Log.i("TEST", "onSingleTapUp -  pos=" + pos);
			Log.i("TEST", "onSingleTapUp -  date" + txtDay.getTag().toString());
			//点击日历的单元格,跳转到任务添加功能
			
			//格式化日期格式
			Date dt = (Date) txtDay.getTag();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String time=df.format(dt);
			
			Intent i = new Intent(mContext, TaskAddActivity.class);
			i.putExtra("date", time);
			startActivity(i);
			
			return false;
		}
	}

	// 基本变量
	private Context mContext = BaseCalendar.this;
	private GridView title_gView;
	private GridView gView1;// 上一个月
	private GridView gView2;// 当前月
	private GridView gView3;// 下一个月
	boolean bIsSelection = false;// 是否是选择事件发生
	private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
	private Calendar calSelected = Calendar.getInstance(); // 选择的日历
	private Calendar calToday = Calendar.getInstance(); // 今日
	private CalendarGridViewAdapter gAdapter;
	private CalendarGridViewAdapter gAdapter1;
	private CalendarGridViewAdapter gAdapter3;
	// 顶部按钮
	private Button btnToday = null;
	private Button title_left_button = null;
	private Button title_right_button = null;
	private RelativeLayout mainLayout;

	private int iMonthViewCurrentMonth = 0; // 当前视图月
	private int iMonthViewCurrentYear = 0; // 当前视图年
	private int iFirstDayOfWeek = Calendar.MONDAY;

	private static final int mainLayoutID = 88; // 设置主布局ID
	private static final int titleLayoutID = 77; // title布局ID
	private static final int caltitleLayoutID = 66; // title布局ID
	private static final int calLayoutID = 55; // 日历布局ID
	/** 底部菜单文字 **/
	String[] menu_toolbar_name_array;
	NavBar mNavbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.month_flipper_view, null);
		LinearLayout line = (LinearLayout)view.findViewById(R.id.month_grid);
		generateTopButtons(view); // 生成顶部按钮 （上一月，下一月，当前月）
		line.addView(generateContentView());

		setContentView(view);
		UpdateStartDateForMonth();
		
		mNavbar = new NavBar(NavBar.HEADER_STYLE_CALENDER, this);
		mNavbar.setHeaderTitle("日历");
		
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,R.anim.slide_right_out);
		
		slideLeftIn.setAnimationListener(animationListener);
		slideLeftOut.setAnimationListener(animationListener);
		slideRightIn.setAnimationListener(animationListener);
		slideRightOut.setAnimationListener(animationListener);
		
		mGesture = new GestureDetector(this, new GestureListener());
	}

	AlertDialog.OnKeyListener onKeyListener = new AlertDialog.OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				BaseCalendar.this.finish();
			}
			return false;

		}

	};

	
	// 生成内容视图
	private View generateContentView() {
		// 创建一个垂直的线性布局（整体内容）
		viewFlipper = new ViewFlipper(this);
		viewFlipper.setId(calLayoutID);
		
		mainLayout = new RelativeLayout(this); // 创建一个垂直的线性布局（整体内容）
		RelativeLayout.LayoutParams params_main = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mainLayout.setLayoutParams(params_main);
		mainLayout.setId(mainLayoutID);
		mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		calStartDate = getCalendarStartDate();

		setTitleGirdView();
		RelativeLayout.LayoutParams params_cal_title = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params_cal_title.addRule(RelativeLayout.BELOW, titleLayoutID);
		mainLayout.addView(title_gView, params_cal_title);

		CreateGirdView();

		RelativeLayout.LayoutParams params_cal = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params_cal.addRule(RelativeLayout.BELOW, caltitleLayoutID);
		mainLayout.addView(viewFlipper, params_cal);
		
		LinearLayout br = new LinearLayout(this);
		RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 1);
		params_br.addRule(RelativeLayout.BELOW, calLayoutID);
		br.setBackgroundColor(getResources().getColor(R.color.calendar_background));
		mainLayout.addView(br, params_br);

		return mainLayout;

	}

	// 生成顶部按钮
	// 参数：布局
	private void generateTopButtons(View view) {
		// 创建一个当前月按钮（中间的按钮）
		btnToday = (Button)view.findViewById(R.id.title_center_button);
		title_left_button = (Button)view.findViewById(R.id.title_left_button);
		title_right_button = (Button)view.findViewById(R.id.title_right_button);
		
		// 当前月的点击事件的监听
		btnToday.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				setToDayViewItem();
			}
		});
		// 上一个月的点击事件的监听
		title_left_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				viewFlipper.setInAnimation(slideRightIn);
				viewFlipper.setOutAnimation(slideRightOut);
				viewFlipper.showPrevious();
				setPrevViewItem();
			}
		});
		// 下一个月的点击事件的监听
		title_right_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				viewFlipper.setInAnimation(slideLeftIn);
				viewFlipper.setOutAnimation(slideLeftOut);
				viewFlipper.showNext();
				setNextViewItem();
			}
		});
	}

	private void setTitleGirdView() {

		title_gView = setGirdView();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		title_gView.setLayoutParams(params);
		title_gView.setVerticalSpacing(0);// 垂直间隔
		title_gView.setHorizontalSpacing(0);// 水平间隔
		TitleGridAdapter titleAdapter = new TitleGridAdapter(this);
		title_gView.setAdapter(titleAdapter);// 设置菜单Adapter
		title_gView.setId(caltitleLayoutID);
	}

	private void CreateGirdView() {

		Calendar tempSelected1 = Calendar.getInstance(); // 临时
		Calendar tempSelected2 = Calendar.getInstance(); // 临时
		Calendar tempSelected3 = Calendar.getInstance(); // 临时
		tempSelected1.setTime(calStartDate.getTime());
		tempSelected2.setTime(calStartDate.getTime());
		tempSelected3.setTime(calStartDate.getTime());

		gView1 = new CalendarGridView(mContext);
		tempSelected1.add(Calendar.MONTH, -1);
		gAdapter1 = new CalendarGridViewAdapter(this, tempSelected1);
		gView1.setAdapter(gAdapter1);// 设置菜单Adapter
		gView1.setId(calLayoutID);

		gView2 = new CalendarGridView(mContext);
		gAdapter = new CalendarGridViewAdapter(this, tempSelected2);
		gView2.setAdapter(gAdapter);// 设置菜单Adapter
		gView2.setId(calLayoutID);

		gView3 = new CalendarGridView(mContext);
		tempSelected3.add(Calendar.MONTH, 1);
		gAdapter3 = new CalendarGridViewAdapter(this, tempSelected3);
		gView3.setAdapter(gAdapter3);// 设置菜单Adapter
		gView3.setId(calLayoutID);

		gView2.setOnTouchListener(this);
		gView1.setOnTouchListener(this);
		gView3.setOnTouchListener(this);
		
		if (viewFlipper.getChildCount() != 0) {
			viewFlipper.removeAllViews();
		}

		viewFlipper.addView(gView2);
		viewFlipper.addView(gView3);
		viewFlipper.addView(gView1);

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1);

		btnToday.setText(s);
	}

	private GridView setGirdView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		GridView gridView = new GridView(this);
		gridView.setLayoutParams(params);
		gridView.setNumColumns(7);// 设置每行列数
		gridView.setGravity(Gravity.CENTER_VERTICAL);// 位置居中
		gridView.setVerticalSpacing(1);// 垂直间隔
		gridView.setHorizontalSpacing(1);// 水平间隔
		gridView.setBackgroundColor(getResources().getColor(
				R.color.calendar_background));

		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int i = display.getWidth() / 7;
		int j = display.getWidth() - (i * 7);
		int x = j / 2;
		gridView.setPadding(x, 0, 0, 0);// 居中

		return gridView;
	}

	// 上一个月
	private void setPrevViewItem() {
		iMonthViewCurrentMonth--;// 当前选择月--
		// 如果当前月为负数的话显示上一年
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth); // 设置月
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear); // 设置年

	}

	// 当月
	private void setToDayViewItem() {

		calSelected.setTimeInMillis(calToday.getTimeInMillis());
		calSelected.setFirstDayOfWeek(iFirstDayOfWeek);
		calStartDate.setTimeInMillis(calToday.getTimeInMillis());
		calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);

	}

	// 下一个月
	private void setNextViewItem() {
		iMonthViewCurrentMonth++;
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);

	}

	// 根据改变的日期更新日历
	// 填充日历控件用
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当前日历显示的年

		String s = calStartDate.get(Calendar.YEAR)
				+ "-"
				+ NumberHelper.LeftPad_Tow_Zero(calStartDate
						.get(Calendar.MONTH) + 1);
		btnToday.setText(s);

		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

	}

	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}

		return calStartDate;
	}

	public class TitleGridAdapter extends BaseAdapter {

		int[] titles = new int[] { R.string.Sun, R.string.Mon, R.string.Tue,
				R.string.Wed, R.string.Thu, R.string.Fri, R.string.Sat };

		private Activity activity;

		// construct
		public TitleGridAdapter(Activity a) {
			activity = a;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object getItem(int position) {
			return titles[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout iv = new LinearLayout(activity);
			TextView txtDay = new TextView(activity);
			txtDay.setFocusable(false);
			txtDay.setBackgroundColor(Color.TRANSPARENT);
			iv.setOrientation(1);

			txtDay.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

			int i = (Integer) getItem(position);

			txtDay.setTextColor(Color.WHITE);
			Resources res = getResources();

			if (i == R.string.Sat) {
				// 周六
				txtDay.setBackgroundColor(res.getColor(R.color.title_text_6));
			} else if (i == R.string.Sun) {
				// 周日
				txtDay.setBackgroundColor(res.getColor(R.color.title_text_7));
			} else {

			}

			txtDay.setText((Integer) getItem(position));

			iv.addView(txtDay, lp);

			return iv;
		}
	}

}
