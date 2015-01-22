package com.xwq.qingyouapp;

import org.json.JSONException;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.EditTextListener;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;

public class RegisterActivity extends Activity {

	private boolean inputOK = false;
	private boolean phoneOK = false;
	private boolean pwdsOK = false;

	private EditText phoneText, pwdText, pwd2Text;
	private Button registerBtn;
	private TextView policyView, alertView;
	private CheckBox checkBox;
	private ImageButton backBtn;

	private String phoneStr, pwdStr, pwd2Str;
	private int alertInfoInteger = R.string.alert_info_null;
	private LocalStorage localStorage;
	private UserMetadata user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		localStorage = new LocalStorage(this);
		getComponents();
		phoneText.addTextChangedListener(phoneTextLis);
		pwdText.addTextChangedListener(editTextLis);
		pwd2Text.addTextChangedListener(editTextLis);
		checkBox.setOnCheckedChangeListener(checkBoxLis);

		registerBtn.setOnClickListener(registerLis);
		backBtn.setOnClickListener(backLis);
		
//		//temp
//		phoneText.setText("18511110008");
//		pwdText.setText("qqqq");
//		pwd2Text.setText("qqqq");
	}

	public void getComponents() {
		phoneText = (EditText) this.findViewById(R.id.editText1);
		pwdText = (EditText) this.findViewById(R.id.editText2);
		pwd2Text = (EditText) this.findViewById(R.id.editText3);
		registerBtn = (Button) this.findViewById(R.id.button1);
		policyView = (TextView) this.findViewById(R.id.textView1);
		alertView = (TextView) this.findViewById(R.id.textView2);
		checkBox = (CheckBox) this.findViewById(R.id.checkBox1);
		backBtn = (ImageButton) this.findViewById(R.id.reg_back);
		registerBtn.setEnabled(false);
	}

	TextWatcher phoneTextLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			checkInputAndResponse();
			if (s.length() == 4 && s.charAt(3) != ' ')
				s.insert(3, " ");
			if (s.length() == 9 && s.charAt(8) != ' ')
				s.insert(8, " ");
		}
	};

	TextWatcher editTextLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			checkInputAndResponse();
		}
	};

	OnClickListener registerLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			UserMetadata user = new UserMetadata();
			user.setPhonenum(phoneStr.replace(" ", ""));
			String url = getResources().getString(R.string.url_base)
					+ getResources().getString(R.string.url_check_phone_num);
			Processor processor = Processor.instance(RegisterActivity.this);
			try {
				processor.runCommand(url, StringHandler.userToJsonString(user), callback);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	CommandCallback callback = new CommandCallback() {
		@Override
		public void excute(String jb) {
			System.out.println("post result--" + jb.length() + ":" + jb);
			if ("valid".equals(jb.trim())) {
				setLocalStorage();
				Intent intent = new Intent(RegisterActivity.this, BasicInfoActivity.class);
				startActivity(intent);
			} else if ("invalid".equals(jb.trim())) {
				Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);
				phoneText.startAnimation(shake);
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.phone_repeat_exception),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			RegisterActivity.this.finish();
		}
	};

	OnCheckedChangeListener checkBoxLis = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			checkInputAndResponse();
		}
	};

	public void checkInputAndResponse() {
		inputOK = false;
		phoneOK = false;
		pwdsOK = false;

		phoneStr = phoneText.getText().toString();
		pwdStr = pwdText.getText().toString();
		pwd2Str = pwd2Text.getText().toString();

		phoneOK = StringHandler.isMobile(phoneStr);
		if (pwdStr.equals(pwd2Str) && pwd2Str.length() > 0)
			pwdsOK = true;

		inputOK = phoneOK && pwdsOK;

		if (!phoneOK && !phoneText.isFocused())
			alertInfoInteger = R.string.phone_is_illegal;
		else if (pwd2Text.isFocused() && !pwd2Str.equals(pwdStr))
			alertInfoInteger = R.string.pwd_not_match;
		else
			alertInfoInteger = R.string.alert_info_null;

		alertView.setText(alertInfoInteger);

		if (inputOK && checkBox.isChecked()) {
			registerBtn.setBackgroundResource(R.drawable.login_btn_able);
			registerBtn.setTextColor(Color.rgb(2, 125, 111));
			registerBtn.setEnabled(true);
		} else {
			registerBtn.setBackgroundResource(R.drawable.login_btn_disable);
			registerBtn.setTextColor(Color.rgb(255, 255, 255));
			registerBtn.setEnabled(false);
		}
	}

	// public void pullInLocalStorage() {
	// user = localStorage.getUser();
	// if(user!=null){
	// String phone = user.getPhonenum();
	// StringBuilder sb = new StringBuilder(phone);
	// sb.insert(3, " ");
	// sb.insert(8, " ");
	// phoneText.setText(sb.toString());
	// String pwd= user.getPassword();
	// pwdText.setText(pwd == null ? "" : pwd);
	// pwd2Text.setText(pwd == null ? "" : pwd);
	// }
	// }

	public void setLocalStorage() {
		user = new UserMetadata();
		user.setPhonenum(phoneStr.replace(" ", ""));
		user.setPassword(pwdStr);
		localStorage.setUser(user);

		// ArrayList<KeyValue> list = new ArrayList<KeyValue>();
		// KeyValue loginAccount = new KeyValue("my_loginAccount",
		// phoneStr.replace(" ", ""));
		// KeyValue pwd = new KeyValue("my_pwd", pwdStr);
		// list.add(loginAccount);
		// list.add(pwd);
		// localStorage.addData(list);
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
