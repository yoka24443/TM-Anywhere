package com.elearning.tm.android.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elearning.tm.android.client.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class TaskExListView extends Activity {

	final static String FATHERACCOUNT = "fatherAccount";
	final static String CHILDACCOUNT = "childAccount";
	final static String BALANCEACCOUNT = "accountBalance";

	List<String> accountFather = new ArrayList<String>();

	List<List<Map<String, String>>> accountChild = new ArrayList<List<Map<String, String>>>();

	TaskExpandablebaseadapter adapter;
	ExpandableListView listAccount;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list);

		listAccount = (ExpandableListView) findViewById(R.id.listAccount);

		// 准备一级列表中显示的数据
		accountFather.add("星期一");
		accountFather.add("星期二");
		accountFather.add("星期三");

		// 准备第一个一级列表中的二级列表数据:两个二级列表,分别显示"childData1"和"childData2"
		List<Map<String, String>> financeAccount = new ArrayList<Map<String, String>>();
		Map<String, String> child1Data1 = new HashMap<String, String>();
		child1Data1.put(FATHERACCOUNT, "银行卡(CNY)");
		child1Data1.put(CHILDACCOUNT, "银行卡");
		child1Data1.put(BALANCEACCOUNT, "￥0.00");
		financeAccount.add(child1Data1);

		// 准备第二个一级列表中的二级列表数据:一个二级列表,显示"child2Data1"
		List<Map<String, String>> dummyAccount = new ArrayList<Map<String, String>>();
		Map<String, String> child2Data1 = new HashMap<String, String>();
		child2Data1.put(FATHERACCOUNT, "饭卡(CNY)");
		child2Data1.put(CHILDACCOUNT, "储值卡");
		child2Data1.put(BALANCEACCOUNT, "￥0.00");
		dummyAccount.add(child2Data1);
		Map<String, String> child2Data2 = new HashMap<String, String>();
		child2Data2.put(FATHERACCOUNT, "财付通(CNY)");
		child2Data2.put(CHILDACCOUNT, "在线支付");
		child2Data2.put(BALANCEACCOUNT, "￥0.00");
		dummyAccount.add(child2Data2);
		Map<String, String> child2Data3 = new HashMap<String, String>();
		child2Data3.put(FATHERACCOUNT, "公交卡(CNY)");
		child2Data3.put(CHILDACCOUNT, "储值卡");
		child2Data3.put(BALANCEACCOUNT, "￥0.00");
		dummyAccount.add(child2Data3);
		Map<String, String> child2Data4 = new HashMap<String, String>();
		child2Data4.put(FATHERACCOUNT, "支付宝(CNY)");
		child2Data4.put(CHILDACCOUNT, "在线支付");
		child2Data4.put(BALANCEACCOUNT, "￥0.00");
		dummyAccount.add(child2Data4);

		// 准备第三个一级列表中的二级列表数据:一个二级列表,显示"child2Data1"
		List<Map<String, String>> cashAccount = new ArrayList<Map<String, String>>();
		Map<String, String> child3Data1 = new HashMap<String, String>();
		child3Data1.put(FATHERACCOUNT, "现金(CNY)");
		child3Data1.put(CHILDACCOUNT, "现金口袋");
		child3Data1.put(BALANCEACCOUNT, "￥0.00");
		cashAccount.add(child3Data1);
		Map<String, String> child3Data2 = new HashMap<String, String>();
		child3Data2.put(FATHERACCOUNT, "其他(CNY)");
		child3Data2.put(CHILDACCOUNT, "现金口袋");
		child3Data2.put(BALANCEACCOUNT, "￥0.00");
		cashAccount.add(child3Data2);

		// 用一个list对象保存所有的二级列表数据
		accountChild.add(financeAccount);
		accountChild.add(dummyAccount);
		accountChild.add(cashAccount);

		adapter = new TaskExpandablebaseadapter(TaskExListView.this, accountFather, accountChild);
		listAccount.setAdapter(adapter);
		// 去掉系统自带的按钮
		listAccount.setGroupIndicator(null);
		// 去掉系统自带的分隔线
		listAccount.setDivider(null);

		// 展开所有二级列表
		int groupCount = adapter.getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			listAccount.expandGroup(i);
		}
		// 监听二级列表
		listAccount.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(
						getBaseContext(),
						String.valueOf(groupPosition) + ":"
								+ String.valueOf(childPosition),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
}