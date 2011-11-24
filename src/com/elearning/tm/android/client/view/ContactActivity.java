package com.elearning.tm.android.client.view;

import java.util.List;
import android.os.Bundle;
import android.widget.TextView;

import com.elearning.tm.android.client.R;
import com.elearning.tm.android.client.manage.UserInfo.NetWorkUserInfoManager;
import com.elearning.tm.android.client.model.Paging;
import com.elearning.tm.android.client.model.UserInfo;
import com.elearning.tm.android.client.view.base.UserArrayBaseActivity;

public class ContactActivity extends UserArrayBaseActivity {

	private static final String TAG = "ContactActivity";
	private int currentPage = 1;
	private static final int PRE_PAGE_COUNT = 20;// 官方分页为每页100
	private int pageCount = 0;
	private String searchCondition = "";
	private TextView search;

	@Override
	protected boolean _onCreate(Bundle savedInstanceState) {

		if (super._onCreate(savedInstanceState)) {
			mNavbar.setHeaderTitle("通讯录");
			search = (TextView) this.findViewById(R.id.search_edit);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Paging getNextPage() {
		currentPage += 1;
		return new Paging(currentPage);
	}

	@Override
	public Paging getCurrentPage() {
		return new Paging(this.currentPage);
	}

	@Override
	protected List<UserInfo> getUsers(Paging page) {
		int pageindex = page.getPage();
		searchCondition = search.getText().toString();
		NetWorkUserInfoManager api = new NetWorkUserInfoManager();
		List<UserInfo> list = api.getUserList(searchCondition, pageindex,
				PRE_PAGE_COUNT);
		return list;
	}

}
