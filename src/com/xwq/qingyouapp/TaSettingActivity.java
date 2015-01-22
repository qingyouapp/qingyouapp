package com.xwq.qingyouapp;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;

import com.gotye.api.GotyeUser;
import com.xwq.qingyouapp.chat.base.BaseActivity;
import com.xwq.qingyouapp.chat.util.ProgressDialogUtil;
import com.xwq.qingyouapp.chat.util.ToastUtil;
import com.xwq.qingyouapp.view.SwitchButton;

public class TaSettingActivity extends BaseActivity{
	private GotyeUser user;
	private ImageButton backBtn;
	private int from;
	SwitchButton blackSwitch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ta_setting);
		user = (GotyeUser) getIntent().getSerializableExtra("user");
		api.addListener(this);
		from = getIntent().getIntExtra("from", -1);
		GotyeUser tempUser=api.requestUserInfo(user.getName(), true);
		if(tempUser!=null){
			user=tempUser; 
		}
		getComponents();
		backBtn.setOnClickListener(backLis);
		blackSwitch = (SwitchButton) findViewById(R.id.blacklist_switchButton);
		List<GotyeUser> blockList =api.getLocalBlockedList();
		if(blockList!=null && blockList.contains(tempUser)){
			blackSwitch.setOn(true);
		}else{
			blackSwitch.setOn(false);
		}
		
		blackSwitch.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(!blackSwitch.isOn()){
					ProgressDialogUtil.showProgress(TaSettingActivity.this, "正在把" + user.getNickname()
							+ "加入到黑名单");
					api.requestAddBlocked(user);
				}else{
					ProgressDialogUtil.showProgress(TaSettingActivity.this, "正在把" + user.getNickname()
							+ "移除黑名单");
					api.removeBolcked(user);
				}
				return false;
			}
		});
	}

	public void getComponents() {
		backBtn = (ImageButton) this.findViewById(R.id.setting_back);
	}

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			TaSettingActivity.this.finish();
		}
	};
	
	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ta_setting, menu);
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
	@Override
	public void onAddBlocked(int code, GotyeUser user) {
		if (code == 0) {
			this.user = user;
			ToastUtil.show(getBaseContext(), "成功把" + user.getName() + "加入黑名单");
			blackSwitch.setOn(true);
			ProgressDialogUtil.dismiss();
		} else {
			ToastUtil.show(getBaseContext(), "抱歉，没能把" + user.getName()
					+ "加入黑名单");
			ProgressDialogUtil.dismiss();
		}
	}
	@Override
	public void onRemoveBlocked(int code, GotyeUser user) {
		// TODO Auto-generated method stub
		ProgressDialogUtil.dismiss();
		if (code == 0) {
			this.user = user;
			ToastUtil.show(getBaseContext(), "成功把" + user.getName() + "移除黑名单");
			blackSwitch.setOn(false);
		} else {
			ToastUtil.show(getBaseContext(), "抱歉，没能把" + user.getName()
					+ "移除黑名单");
		}
	}
	@Override
	public void onGetBlockedList(int code, List<GotyeUser> mList) {
		// TODO Auto-generated method stub
		super.onGetBlockedList(code, mList);
	}

}
