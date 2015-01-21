package com.xwq.qingyouapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.frag.ShowPageFrag;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.ThisApp;

public class MyShowActivity extends FragmentActivity {

	private ImageButton backBtn;
	private TextView titleView;
	private LocalStorage localStorage;
	private UserMetadata user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_show);
		
		FragmentManager fMgr = getSupportFragmentManager();
		FragmentTransaction ft = fMgr.beginTransaction();
		ShowPageFrag showPageFrag = new ShowPageFrag();
		ft.add(R.id.my_show_root, showPageFrag, "showPageFrag");
		ft.addToBackStack("showPageFrag");
		ft.commit();

		// 本地存储
		localStorage = new LocalStorage(this);
		// 通过SHOW_USER_ID获取需要显示的用户信息
		user = localStorage.getUserById(ThisApp.SHOW_USER_ID);

		titleView = (TextView) this.findViewById(R.id.my_show_title);
		titleView.setText(user.getNickname());

		backBtn = (ImageButton) findViewById(R.id.ta_main_back);
		backBtn.setOnClickListener(backLis);
	}

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			MyShowActivity.this.finish();
		}
	};

	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_show, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
