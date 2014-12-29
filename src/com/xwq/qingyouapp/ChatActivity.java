package com.xwq.qingyouapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ChatActivity extends Activity {

	private ImageButton backBtn, settingBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);

		getComponents();
		backBtn.setOnClickListener(backLis);
		settingBtn.setOnClickListener(settingLis);
	}

	public void getComponents() {
		backBtn = (ImageButton) findViewById(R.id.ta_main_back);
		settingBtn = (ImageButton) findViewById(R.id.setting);
	}

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			ChatActivity.this.finish();
		}
	};

	OnClickListener settingLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(ChatActivity.this, TaSettingActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
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
