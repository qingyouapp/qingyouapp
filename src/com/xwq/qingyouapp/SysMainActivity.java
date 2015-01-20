package com.xwq.qingyouapp;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;

import com.xwq.qingyouapp.frag.ChatHistoryFrag;
import com.xwq.qingyouapp.frag.RecomHistoryFrag;
import com.xwq.qingyouapp.frag.ShowPageFrag;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.ThisApp;

public class SysMainActivity extends FragmentActivity {

	public enum ITEM {
		RecomHistory, ChatHistory, SelfPage
	};

	private static FragmentManager fMgr;
	private RadioButton recomHistory, chatHistory, selfPage;
	private Drawable item1d, item1n, item2d, item2n, item3d, item3n;
	private int downColor;
	private LocalStorage localStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sys_main);
		// 获取FragmentManager实例
		fMgr = getSupportFragmentManager();
		getComponents();
		initFragment();
		dealBottomButtonsClickEvent();
		
		localStorage = new LocalStorage(this);
	}

	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		RecomHistoryFrag recomHistoryFrag = new RecomHistoryFrag();
		ft.add(R.id.fragmentRoot, recomHistoryFrag, "recomHistoryFrag");
		ft.addToBackStack("recomHistoryFrag");
		ft.commit();
	}

	public void getComponents() {
		recomHistory = (RadioButton) findViewById(R.id.recommand_history);
		chatHistory = (RadioButton) findViewById(R.id.chat_history);
		selfPage = (RadioButton) findViewById(R.id.self_page);
		downColor = getResources().getColor(R.color.down_color);

		item1d = getResources().getDrawable(R.drawable.history_down);
		item1n = getResources().getDrawable(R.drawable.history_normal);
		item2d = getResources().getDrawable(R.drawable.chat_down);
		item2n = getResources().getDrawable(R.drawable.chat_normal);
		item3d = getResources().getDrawable(R.drawable.homepage_down);
		item3n = getResources().getDrawable(R.drawable.homepage_normal);

		float num = 0.8f;
		item1d.setBounds(0, 0, (int) (item1d.getMinimumWidth()*num),
				(int) (item1d.getMinimumHeight()*num));
		item1n.setBounds(0, 0, (int)(item1n.getMinimumWidth()*num),
				(int) (item1n.getMinimumHeight()*num));
		item2d.setBounds(0, 0, (int) (item2d.getMinimumWidth()*num),
				(int) (item2d.getMinimumHeight()*num));
		item2n.setBounds(0, 0, (int) (item2n.getMinimumWidth()*num),
				(int) (item2n.getMinimumHeight()*num));
		item3d.setBounds(0, 0, (int) (item3d.getMinimumWidth()*num),
				(int) (item3d.getMinimumHeight()*num));
		item3n.setBounds(0, 0, (int) (item3n.getMinimumWidth()*num),
				(int) (	item3n.getMinimumHeight()*num));
		
		ChooseItem(ITEM.RecomHistory);
	}

	/**
	 * 处理底部点击事件
	 */
	private void dealBottomButtonsClickEvent() {
		recomHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fMgr.findFragmentByTag("recomHistoryFrag") != null
						&& fMgr.findFragmentByTag("recomHistoryFrag").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();
				ChooseItem(ITEM.RecomHistory);
			}
		});
		chatHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("recomHistoryFrag"));
				ChatHistoryFrag chatHistoryFrag = new ChatHistoryFrag();
				ft.add(R.id.fragmentRoot, chatHistoryFrag, "chatHistoryFrag");
				ft.addToBackStack("chatHistoryFrag");
				ft.commit();
				ChooseItem(ITEM.ChatHistory);
			}
		});
		selfPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//告诉ShowPageFrag显示哪个User的信息
				ThisApp.SHOW_USER_ID = localStorage.getUser().getUserid();
				
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("recomHistoryFrag"));
				ShowPageFrag showPageFrag = new ShowPageFrag();
				ft.add(R.id.fragmentRoot, showPageFrag, "selfPageFrag");
				ft.addToBackStack("selfPageFrag");
				ft.commit();
				ChooseItem(ITEM.SelfPage);
			}
		});
	}

	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();
		}
	}

	@SuppressLint("ResourceAsColor")
	public void ChooseItem(ITEM item) {

		recomHistory.setTextColor(R.color.normal_color);
		chatHistory.setTextColor(R.color.normal_color);
		selfPage.setTextColor(R.color.normal_color);
		recomHistory.setCompoundDrawables(null, item1n, null, null);
		chatHistory.setCompoundDrawables(null, item2n, null, null);
		selfPage.setCompoundDrawables(null, item3n, null, null);

		switch (item) {
		case RecomHistory:
			recomHistory.setTextColor(downColor);
			recomHistory.setCompoundDrawables(null, item1d, null, null);
			break;
		case ChatHistory:
			chatHistory.setTextColor(downColor);
			chatHistory.setCompoundDrawables(null, item2d, null, null);
			break;
		case SelfPage:
			selfPage.setTextColor(downColor);
			selfPage.setCompoundDrawables(null, item3d, null, null);
			break;
		}
	}

	// 点击返回按钮
	@Override
	public void onBackPressed() {
		SysMainActivity.this.finish();
	}
		
}
