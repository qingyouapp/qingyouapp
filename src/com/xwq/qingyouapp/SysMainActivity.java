package com.xwq.qingyouapp;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeNotify;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.xwq.qingyouapp.chat.LoginPage;
import com.xwq.qingyouapp.chat.base.BaseActivity;
import com.xwq.qingyouapp.chat.util.BeepManager;
import com.xwq.qingyouapp.frag.ChatHistoryFrag;
import com.xwq.qingyouapp.frag.MyselfPageFrag;
import com.xwq.qingyouapp.frag.RecomHistoryFrag;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.ThisApp;

public class SysMainActivity extends BaseActivity{

	public enum ITEM {
		RecomHistory, ChatHistory, MyselfPage
	};

	private static FragmentManager fMgr;
	private ChatHistoryFrag chatHistoryFrag = null;
	private RadioButton recomHistory, chatHistory, selfPage;
	private Drawable item1d, item1n, item2d, item2n, item3d, item3n;
	private int downColor;
	private LocalStorage localStorage;

	private BeepManager beep;
	private boolean returnNotify = false;

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
		FragmentTransaction ft =  fMgr.beginTransaction();
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
	 * 处理底部点击事件O
	 */
	private void dealBottomButtonsClickEvent() {
		recomHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentTransaction ft =  fMgr.beginTransaction();
				if(fMgr.findFragmentByTag("chatHistoryFrag")!=null){
					ft.hide(fMgr.findFragmentByTag("chatHistoryFrag"));
				}
				if(fMgr.findFragmentByTag("myselfFrag")!=null){
					ft.hide(fMgr.findFragmentByTag("myselfFrag"));
				}
				if(fMgr.findFragmentByTag("recomHistoryFrag")==null){
					RecomHistoryFrag recomHistoryFrag = new RecomHistoryFrag();
					ft.add(R.id.fragmentRoot, recomHistoryFrag, "recomHistoryFrag");
					ft.addToBackStack("recomHistoryFrag");
				}else if (fMgr.findFragmentByTag("recomHistoryFrag") != null
						&& fMgr.findFragmentByTag("recomHistoryFrag").isVisible()) {
					return;
				}else if(fMgr.findFragmentByTag("recomHistoryFrag") != null
						&& !fMgr.findFragmentByTag("recomHistoryFrag").isVisible()){
					ft.show( fMgr.findFragmentByTag("recomHistoryFrag"));
				}

				ft.commit();
				//				popAllFragmentsExceptTheBottomOne();
				ChooseItem(ITEM.RecomHistory);
			}
		});
		chatHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft =  fMgr.beginTransaction();
				if(fMgr.findFragmentByTag("recomHistoryFrag")!=null){
					ft.hide(fMgr.findFragmentByTag("recomHistoryFrag"));
				}
				if(fMgr.findFragmentByTag("myselfFrag")!=null){
					ft.hide(fMgr.findFragmentByTag("myselfFrag"));
				}
				if(fMgr.findFragmentByTag("chatHistoryFrag")==null){
					chatHistoryFrag = new ChatHistoryFrag();
					ft.add(R.id.fragmentRoot, chatHistoryFrag, "chatHistoryFrag");
					ft.addToBackStack("chatHistoryFrag");
				}else if (fMgr.findFragmentByTag("chatHistoryFrag") != null
						&& fMgr.findFragmentByTag("chatHistoryFrag").isVisible()) {
					return;
				}else if(fMgr.findFragmentByTag("chatHistoryFrag") != null
						&& !fMgr.findFragmentByTag("chatHistoryFrag").isVisible()){
					ft.show( fMgr.findFragmentByTag("chatHistoryFrag"));
				}

				ft.commit();
				ChooseItem(ITEM.ChatHistory);
			}
		});
		selfPage.setOnClickListener(new OnClickListener() {


			@Override
			public void onClick(View v) {
				//告诉ShowPageFrag显示哪个User的信息
				ThisApp.SHOW_USER_ID = localStorage.getUser().getUserid();
				FragmentTransaction ft =  fMgr.beginTransaction();
				if(fMgr.findFragmentByTag("recomHistoryFrag")!=null){
					ft.hide(fMgr.findFragmentByTag("recomHistoryFrag"));
				}
				if(fMgr.findFragmentByTag("chatHistoryFrag")!=null){
					ft.hide(fMgr.findFragmentByTag("chatHistoryFrag"));
				}
				if(fMgr.findFragmentByTag("myselfFrag")==null){
					MyselfPageFrag myselfPageFrag = new MyselfPageFrag();
					ft.add(R.id.fragmentRoot, myselfPageFrag, "myselfFrag");
					ft.addToBackStack("myselfFrag");
				}else if (fMgr.findFragmentByTag("myselfFrag") != null
						&& fMgr.findFragmentByTag("myselfFrag").isVisible()) {
					return;
				}else if(fMgr.findFragmentByTag("myselfFrag") != null
						&& !fMgr.findFragmentByTag("myselfFrag").isVisible()){
					ft.show( fMgr.findFragmentByTag("myselfFrag"));
				}
				
				ft.commit();

				ChooseItem(ITEM.MyselfPage);
			}
		});
	}

	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	//	public static void popAllFragmentsExceptTheBottomOne() {
	//		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
	//			fMgr.popBackStack();
	//		}
	//	}

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
		case MyselfPage:
			selfPage.setTextColor(downColor);
			selfPage.setCompoundDrawables(null, item3d, null, null);
			break;
		}
	}


	// 更新提醒
	public void updateUnReadTip() {
		int unreadCount = api.getTotalUnreadMsgCount();
		int unreadNotifyCount = api.getUnreadNotifyCount();
		unreadCount += unreadNotifyCount;
		//		msgTip.setVisibility(View.VISIBLE);
		//		if (unreadCount > 0 && unreadCount < 100) {
		//			msgTip.setText(String.valueOf(unreadCount));
		//		} else if (unreadCount >= 100) {
		//			msgTip.setText("99");
		//		} else {
		//			msgTip.setVisibility(View.GONE);
		//		}
	}

	// 页面刷新
	private void mainRefresh() {
		updateUnReadTip();
		if(chatHistoryFrag!=null)
			chatHistoryFrag.refresh();
	}

	// 此处处理账号在另外设备登陆造成的被动下线
	@Override
	public void onLogout(int code) {
		if (code == GotyeStatusCode.CODE_FORCELOGOUT) {
			Toast.makeText(this, "您的账号在另外一台设备上登录了！", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getBaseContext(), LoginPage.class);
			intent.putExtra("logoutQuit", 100);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			finish();
		} else if (code == GotyeStatusCode.CODE_NETWORK_DISCONNECTED) {

			Toast.makeText(this, "您的账号掉线了！", Toast.LENGTH_SHORT).show();
			/*
			Intent intent = new Intent(getBaseContext(), LoginPage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);*/
		}else{
			Intent i = new Intent(this, LoginPage.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			i.putExtra("logoutQuit", 100);
			Toast.makeText(this, "退出登陆！", Toast.LENGTH_SHORT).show();
			startActivity(i);
			finish();
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		//		super.onNewIntent(intent);
		//		if (intent != null) {
		//			int tab = intent.getIntExtra("tab", -1);
		//			if (tab == 1) {
		//				contactsFragment.refresh();
		//			}
		//			int notify=intent.getIntExtra("notify", 0);
		//			if(notify==1){
		//				clearNotify();
		//			}
		//			
		//			int selection_index=intent.getIntExtra("selection_index", -1);
		//			if(selection_index==1){
		//				setTabSelection(1);
		//			}
		//		}
	}
	private void clearNotify(){
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}

	// 收到消息（此处只是单纯的更新聊天历史界面，不涉及聊天消息处理，当然你也可以处理，若你非要那样做）
	@Override
	public void onReceiveMessage(int code, GotyeMessage message, boolean unRead) {
		if (returnNotify) {
			return;
		}
		if(chatHistoryFrag!=null)
			chatHistoryFrag.refresh();
		if (unRead) {
			updateUnReadTip();

			if (!api.isNewMsgNotify()) {
				return;
			}
			if (message.getReceiverType() == 2) {
				if (api.isNotReceiveGroupMsg()) {
					return;
				}
				if (api.isGroupDontdisturb(((GotyeGroup) message.getReceiver())
						.getGroupID())) {
					return;
				}
			}
			beep.playBeepSoundAndVibrate();
		}
	}

	// 自己发送的信息统一在此处理
	@Override
	public void onSendMessage(int code, GotyeMessage message) {
		if (returnNotify) {
			return;
		}
		if(chatHistoryFrag!=null)
			chatHistoryFrag.refresh();
	}

	// 收到群邀请信息
	@Override
	public void onReceiveNotify(int code,GotyeNotify notify) {
		if (returnNotify) {
			return;
		}
		if(chatHistoryFrag!=null)
			chatHistoryFrag.refresh();
		updateUnReadTip();
		if (!api.isNotReceiveGroupMsg()) {
			beep.playBeepSoundAndVibrate();
		}
	}

	//	@Override
	//	public void onRemoveFriend(int code, GotyeUser user) {
	//		if (returnNotify) {
	//			return;
	//		}
	//		api.deleteSession(user, true);
	//		chatHistoryFrag.refresh();
	//	}

	//	@Override
	//	public void onAddFriend(int code, GotyeUser user) {
	//		// TODO Auto-generated method stub
	//		if (returnNotify) {
	//			return;
	//		}
	//		if (currentPosition == 1) {
	//			contactsFragment.refresh();
	//		}
	//	}

	//	@Override
	//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	//		// 选取图片的返回值
	//		if (resultCode == RESULT_OK) {
	//			if (data != null) {
	//				Uri selectedImage = data.getData();
	//				if (selectedImage != null) {
	//					String path = URIUtil.uriToPath(this, selectedImage);
	//					setPicture(path);
	//				}
	//			}
	//		}
	//		super.onActivityResult(requestCode, resultCode, data);
	//	}

	//	private void setPicture(String path) {
	//		File f = new File(PathUtil.getAppFIlePath());
	//		if (!f.isDirectory()) {
	//			f.mkdirs();
	//		}
	//		File file = new File(PathUtil.getAppFIlePath()
	//				+ System.currentTimeMillis() + "jpg");
	//		if (file.exists()) {
	//			file.delete();
	//			try {
	//				file.createNewFile();
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//		Bitmap smaillBit = BitmapUtil.getSmallBitmap(path, 50, 50);
	//		String smallPath = BitmapUtil.saveBitmapFile(smaillBit);
	//		settingFragment.modifyUserIcon(smallPath);
	//	}

	@Override
	public void onNotifyStateChanged() {
		// TODO Auto-generated method stub
		mainRefresh();
	}

	@Override
	public void onLogin(int code, GotyeUser currentLoginUser) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onResume() {
		super.onResume();
		returnNotify = false;
		mainRefresh();
	}

	@Override
	protected void onPause() {
		returnNotify = true;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// 保持好习惯，销毁时请移除监听
		api.removeListener(this);
		// 告诉service已经处于后台运行状态
		// Intent toService=new Intent(this, GotyeService.class);
		// toService.setAction(GotyeService.ACTION_RUN_BACKGROUND);
		// startService(toService);
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
	}
}
