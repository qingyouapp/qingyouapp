package com.xwq.qingyouapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.frag.MatchPageFrag;
import com.xwq.qingyouapp.frag.ShowPageFrag;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.ThisApp;

public class TaMainActivity extends FragmentActivity {

	public enum ITEM {
		ShowPage, MatchPage, ChatPage
	};

	private static FragmentManager fMgr;
	private RadioButton showPageBtn, matchPageBtn, chatPageBtn;
	private Drawable item1d, item1n, item2d, item2n, item3d, item3n;
	private int downColor;
	private ImageButton backBtn, settingBtn;
	private TextView titleView;

	private LocalStorage localStorage;
	private UserMetadata user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ta_main);
		
		fMgr = getSupportFragmentManager();
		getComponents();
		initFragment();
		dealBottomButtonsClickEvent();

		// 本地存储
		localStorage = new LocalStorage(this);
		// 通过SHOW_USER_ID获取需要显示的用户信息
		user = localStorage.getUserById(ThisApp.SHOW_USER_ID);
		titleView = (TextView)this.findViewById(R.id.ta_main_title);
		titleView.setText(user.getNickname());

		backBtn.setOnClickListener(backLis);
		settingBtn.setOnClickListener(settingLis);
	}

	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		ShowPageFrag showPageFrag = new ShowPageFrag();
		ft.add(R.id.fragmentRoot, showPageFrag, "showPageFrag");
		ft.addToBackStack("showPageFrag");
		ft.commit();
	}

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			TaMainActivity.this.finish();
		}
	};

	OnClickListener settingLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(TaMainActivity.this, TaSettingActivity.class);
			startActivity(intent);
		}
	};

	public void getComponents() {

		backBtn = (ImageButton) findViewById(R.id.ta_main_back);
		settingBtn = (ImageButton) findViewById(R.id.setting);

		showPageBtn = (RadioButton) findViewById(R.id.show_page);
		matchPageBtn = (RadioButton) findViewById(R.id.match_page);
		chatPageBtn = (RadioButton) findViewById(R.id.chat_page);
		downColor = getResources().getColor(R.color.down_color);

		item1d = getResources().getDrawable(R.drawable.homepage_down);
		item1n = getResources().getDrawable(R.drawable.homepage_normal);
		item2d = getResources().getDrawable(R.drawable.match_down);
		item2n = getResources().getDrawable(R.drawable.match_normal);
		item3d = getResources().getDrawable(R.drawable.chat_down);
		item3n = getResources().getDrawable(R.drawable.chat_normal);

		float num = 0.8f;
		item1d.setBounds(0, 0, (int) (item1d.getMinimumWidth() * num),
				(int) (item1d.getMinimumHeight() * num));
		item1n.setBounds(0, 0, (int) (item1n.getMinimumWidth() * num),
				(int) (item1n.getMinimumHeight() * num));
		item2d.setBounds(0, 0, (int) (item2d.getMinimumWidth() * num),
				(int) (item2d.getMinimumHeight() * num));
		item2n.setBounds(0, 0, (int) (item2n.getMinimumWidth() * num),
				(int) (item2n.getMinimumHeight() * num));
		item3d.setBounds(0, 0, (int) (item3d.getMinimumWidth() * num),
				(int) (item3d.getMinimumHeight() * num));
		item3n.setBounds(0, 0, (int) (item3n.getMinimumWidth() * num),
				(int) (item3n.getMinimumHeight() * num));

		ChooseItem(ITEM.ShowPage);
	}

	@SuppressLint("ResourceAsColor")
	public void ChooseItem(ITEM item) {

		showPageBtn.setTextColor(R.color.normal_color);
		showPageBtn.setCompoundDrawables(null, item1n, null, null);
		matchPageBtn.setTextColor(R.color.normal_color);
		matchPageBtn.setCompoundDrawables(null, item2n, null, null);
		chatPageBtn.setTextColor(R.color.normal_color);
		chatPageBtn.setCompoundDrawables(null, item3n, null, null);

		switch (item) {
		case ShowPage:
			showPageBtn.setTextColor(downColor);
			showPageBtn.setCompoundDrawables(null, item1d, null, null);
			break;
		case MatchPage:
			matchPageBtn.setTextColor(downColor);
			matchPageBtn.setCompoundDrawables(null, item2d, null, null);
			break;
		case ChatPage:
			chatPageBtn.setTextColor(downColor);
			chatPageBtn.setCompoundDrawables(null, item3d, null, null);
			break;
		}
	}

	private void dealBottomButtonsClickEvent() {
		showPageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fMgr.findFragmentByTag("showPageFrag") != null
						&& fMgr.findFragmentByTag("showPageFrag").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();
				ChooseItem(ITEM.ShowPage);
			}
		});
		matchPageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("showPageFrag"));
				MatchPageFrag matchPageFrag = new MatchPageFrag();
				ft.add(R.id.fragmentRoot, matchPageFrag, "matchPageFrag");
				ft.addToBackStack("matchPageFrag");
				ft.commit();
				ChooseItem(ITEM.MatchPage);
			}
		});
		chatPageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// popAllFragmentsExceptTheBottomOne();
				// FragmentTransaction ft = fMgr.beginTransaction();
				// ft.hide(fMgr.findFragmentByTag("showPageFrag"));
				// ChatPageFrag chatPageFrag = new ChatPageFrag();
				// ft.add(R.id.fragmentRoot, chatPageFrag, "chatPageFrag");
				// ft.addToBackStack("chatPageFrag");
				// ft.commit();
				// ChooseItem(ITEM.ChatPage);
				Intent intent = new Intent(TaMainActivity.this, ChatActivity.class);
				startActivity(intent);
			}
		});
	}

	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();
		}
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ta_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
