package com.xwq.qingyouapp;

import java.util.ArrayList;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.ConnectionDetector;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.EditTextListener;
import com.xwq.qingyouapp.util.KeyValue;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.PhotoHandler.ImageType;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;

public class LoginActivity extends Activity {

	private boolean adNull = true;
	private boolean paNull = true;

	private EditText accountText, pwdText;
	private Button loginBtn;
	private TextView registerText;

	private String accountStr;
	private Processor processor;
	private Animation shake;
	private LocalStorage localStorage;
	private UserMetadata user;
	private boolean isOldUser = false;
	private PhotoHandler photoHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ThisApp.addActivity(this);

		localStorage = new LocalStorage(this);
		photoHandler = new PhotoHandler(this);

		getComponents();

		accountText.addTextChangedListener(accountTextLis);
		pwdText.addTextChangedListener(pwdTextLis);
		loginBtn.setOnClickListener(loginLis);
		registerText.setOnClickListener(registerLis);

		pullInLocalStorage();
	}

	public void pullInLocalStorage() {
		String loginAccount = localStorage.getData("my_loginAccount");
		String pwd = localStorage.getData("my_pwd");

		if (!StringHandler.isNull(loginAccount)) {
			StringBuilder sb = new StringBuilder(loginAccount);
			accountText.setText(sb.toString());
		}
		if (!StringHandler.isNull(loginAccount) && !StringHandler.isNull(pwd)) {
			pwdText.setText(pwd == null ? "" : pwd);
			isOldUser = true;
			login();
		}
	}

	private void getComponents() {
		accountText = (EditText) this.findViewById(R.id.editText1);
		pwdText = (EditText) this.findViewById(R.id.editText2);
		loginBtn = (Button) this.findViewById(R.id.button1);
		registerText = (TextView) this.findViewById(R.id.textView1);
		// loginQuesText = (TextView) this.findViewById(R.id.textView2);
		loginBtn.setClickable(false);

		shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
	}

	OnClickListener loginLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			login();
		}
	};

	public void login() {
		accountStr = accountText.getText().toString();
		UserMetadata user;

		if (!(StringHandler.isEmail(accountStr)) && !(StringHandler.isMobile(accountStr))) {
			accountText.startAnimation(shake);
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.account_illegal), Toast.LENGTH_SHORT).show();
		} else {
			String url = getResources().getString(R.string.url_base)
					+ getResources().getString(R.string.url_login);
			user = new UserMetadata();
			user.setPhonenum(accountText.getText().toString());
			user.setPassword(pwdText.getText().toString());
			try {
				// 检测网络正常，则请求数据
				if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
					processor = Processor.instance(LoginActivity.this);
					processor.runCommand(url, StringHandler.userToJsonString(user), loginCallback);
				} else if (isOldUser) {
					// 无网络，但是登录过
					Toast.makeText(
							LoginActivity.this,
							LoginActivity.this.getResources().getString(R.string.network_exception),
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this, IdentifyActivity.class);
					startActivity(intent);
				} else {
					// 无网络，且没登录过
					Toast.makeText(
							LoginActivity.this,
							LoginActivity.this.getResources().getString(R.string.network_exception),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	CommandCallback loginCallback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			// 如果没有此人，则返回 {"userid":"-1"}
			// 如果有此人，则返回
			// {"age":"1","edueducation":"教育","email":"..........."...."username":""...}
			System.out.println("jb:" + jb);
			if (jb.indexOf("username") == -1) {
				accountText.startAnimation(shake);
				Toast.makeText(getApplication(),
						getResources().getString(R.string.account_pwd_exception),
						Toast.LENGTH_SHORT).show();
			} else if (jb.indexOf("username") >= 0) {
				Gson gson = new Gson();
				user = gson.fromJson(jb, UserMetadata.class);
				setLocalStorage();
				Intent intent = null;
				if (user.getVarificationPass())
					intent = new Intent(LoginActivity.this, SysMainActivity.class);
				else
					intent = new Intent(LoginActivity.this, IdentifyActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplication(),
						getResources().getString(R.string.network_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}

	};

	public void setLocalStorage() {
		localStorage.setUser(user);
		ArrayList<KeyValue> list = new ArrayList<KeyValue>();
		list.add(new KeyValue("my_loginAccount", accountText.getText().toString()));
		list.add(new KeyValue("my_pwd", pwdText.getText().toString()));
		localStorage.addData(list);
		// 下载图片
		if (!StringHandler.isNull(user.getPhotoAlbum())) {
			String[] photoNames = user.getPhotoAlbum().trim().split(",");
			// 检测解析后的名字不为空
			if (photoNames != null && photoNames.length > 0) {
				String localUrl = photoHandler.getLocalAbsolutePath(user.getUserid(),
						ImageType.Album);
				for (String photoName : photoNames) {
					// 如果图片不存在则开始下载
					if (!photoHandler.isExisted(localUrl, photoName)) {
						photoHandler.downloadImageFromServer(user.getUserid(), photoName);
					}
				}
			}
		}
	}

	OnClickListener registerLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	};

	TextWatcher accountTextLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			adNull = s.length() > 0 ? false : true;
			responseToInput(s);
		}
	};

	TextWatcher pwdTextLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			paNull = s.length() > 0 ? false : true;
			responseToInput(s);
		}
	};

	public boolean responseToInput(Editable s) {
		if (s.length() > 0) {
			if (textsInputed()) {
				loginBtn.setBackgroundResource(R.drawable.login_btn_able);
				loginBtn.setTextColor(Color.rgb(2, 125, 111));
				loginBtn.setEnabled(true);
			}
			return false;
		} else {
			if (textsAreNull()) {
				loginBtn.setBackgroundResource(R.drawable.login_btn_disable);
				loginBtn.setTextColor(Color.rgb(255, 255, 255));
				loginBtn.setEnabled(false);
			}
			return true;
		}
	}

	public void lockInput(boolean b) {
		// accountText.setEnabled(!b);
		// pwdText.setEnabled(!b);
		// loginBtn.setClickable(!b);
	}

	public boolean textsInputed() {
		return !(adNull || paNull);
	}

	public boolean textsAreNull() {
		return adNull || paNull;
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// 11.17
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
