package com.elearning.tm.android.client.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.model.ItemInfo;
import com.elearning.tm.android.client.view.base.BaseActivity;
import com.elearning.tm.android.client.view.module.NavBar;
import com.elearning.tm.android.client.view.module.SquareItemAdapter;



public class AboutActivity extends BaseActivity {
	NavBar mNavbar;
	private ListView mListView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		mNavbar = new NavBar(NavBar.HEADER_STYLE_TITLE, this);
		mNavbar.setHeaderTitle("帮助");
		
		mListView =  (ListView)findViewById(R.id.list);
		SquareItemAdapter sAdapter = new SquareItemAdapter(AboutActivity.this);
		mListView.setAdapter(sAdapter);
		ArrayList<ItemInfo> title = loadListItemData();
		sAdapter.refresh(title);
		mListView.setDivider(null);
		
		//添加listitem的事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ItemInfo info =	(ItemInfo)parent.getItemAtPosition(position);
				try {
					//根据action对应的class跳转
					Class<?> action = Class.forName(info.getAction());
					Intent intent = new Intent();
					intent.setClass(AboutActivity.this, action);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Log.d(info.getTitle(), info.getAction());
			}
		});
		
		

	}
	
	private ArrayList<ItemInfo> loadListItemData() {
		ArrayList<ItemInfo> title = new ArrayList<ItemInfo>();
		ItemInfo item1 = new ItemInfo();
		item1.setTitle("操作说明");
		item1.setAction("com.elearning.tm.android.client.view.HelpActivity");
		title.add(item1);
		
	/*	功能没有完成,暂时隐掉
		ItemInfo item2 = new ItemInfo();
		item2.setTitle("数据同步");
		item2.setAction("com.elearning.tm.android.client.view.SyncActivity");
		title.add(item2);*/
		
		ItemInfo item3 = new ItemInfo();
		item3.setTitle("软件更新");
		item3.setAction("com.elearning.tm.android.client.view.UpdateActivity");
		title.add(item3);
		
		ItemInfo item4 = new ItemInfo();
		item4.setTitle("项目相关");
		item4.setAction("com.elearning.tm.android.client.view.SaySomeWordsActivity");
		title.add(item4);
		return title;
	}
}
