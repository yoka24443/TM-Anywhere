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

		// ׼��һ���б�����ʾ������
		accountFather.add("����һ");
		accountFather.add("���ڶ�");
		accountFather.add("������");

		// ׼����һ��һ���б��еĶ����б�����:���������б�,�ֱ���ʾ"childData1"��"childData2"
		List<Map<String, String>> financeAccount = new ArrayList<Map<String, String>>();
		Map<String, String> child1Data1 = new HashMap<String, String>();
		child1Data1.put(FATHERACCOUNT, "���п�(CNY)");
		child1Data1.put(CHILDACCOUNT, "���п�");
		child1Data1.put(BALANCEACCOUNT, "��0.00");
		financeAccount.add(child1Data1);

		// ׼���ڶ���һ���б��еĶ����б�����:һ�������б�,��ʾ"child2Data1"
		List<Map<String, String>> dummyAccount = new ArrayList<Map<String, String>>();
		Map<String, String> child2Data1 = new HashMap<String, String>();
		child2Data1.put(FATHERACCOUNT, "����(CNY)");
		child2Data1.put(CHILDACCOUNT, "��ֵ��");
		child2Data1.put(BALANCEACCOUNT, "��0.00");
		dummyAccount.add(child2Data1);
		Map<String, String> child2Data2 = new HashMap<String, String>();
		child2Data2.put(FATHERACCOUNT, "�Ƹ�ͨ(CNY)");
		child2Data2.put(CHILDACCOUNT, "����֧��");
		child2Data2.put(BALANCEACCOUNT, "��0.00");
		dummyAccount.add(child2Data2);
		Map<String, String> child2Data3 = new HashMap<String, String>();
		child2Data3.put(FATHERACCOUNT, "������(CNY)");
		child2Data3.put(CHILDACCOUNT, "��ֵ��");
		child2Data3.put(BALANCEACCOUNT, "��0.00");
		dummyAccount.add(child2Data3);
		Map<String, String> child2Data4 = new HashMap<String, String>();
		child2Data4.put(FATHERACCOUNT, "֧����(CNY)");
		child2Data4.put(CHILDACCOUNT, "����֧��");
		child2Data4.put(BALANCEACCOUNT, "��0.00");
		dummyAccount.add(child2Data4);

		// ׼��������һ���б��еĶ����б�����:һ�������б�,��ʾ"child2Data1"
		List<Map<String, String>> cashAccount = new ArrayList<Map<String, String>>();
		Map<String, String> child3Data1 = new HashMap<String, String>();
		child3Data1.put(FATHERACCOUNT, "�ֽ�(CNY)");
		child3Data1.put(CHILDACCOUNT, "�ֽ�ڴ�");
		child3Data1.put(BALANCEACCOUNT, "��0.00");
		cashAccount.add(child3Data1);
		Map<String, String> child3Data2 = new HashMap<String, String>();
		child3Data2.put(FATHERACCOUNT, "����(CNY)");
		child3Data2.put(CHILDACCOUNT, "�ֽ�ڴ�");
		child3Data2.put(BALANCEACCOUNT, "��0.00");
		cashAccount.add(child3Data2);

		// ��һ��list���󱣴����еĶ����б�����
		accountChild.add(financeAccount);
		accountChild.add(dummyAccount);
		accountChild.add(cashAccount);

		adapter = new TaskExpandablebaseadapter(TaskExListView.this, accountFather, accountChild);
		listAccount.setAdapter(adapter);
		// ȥ��ϵͳ�Դ��İ�ť
		listAccount.setGroupIndicator(null);
		// ȥ��ϵͳ�Դ��ķָ���
		listAccount.setDivider(null);

		// չ�����ж����б�
		int groupCount = adapter.getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			listAccount.expandGroup(i);
		}
		// ���������б�
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