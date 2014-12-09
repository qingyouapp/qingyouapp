package com.xwq.qingyouapp;

import com.xwq.qingyouapp.util.EditTextListener;
import com.xwq.qingyouapp.util.StringHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		getComponents();
		phoneText.addTextChangedListener(phoneTextLis);
		pwdText.addTextChangedListener(editTextLis);
		pwd2Text.addTextChangedListener(editTextLis);
		checkBox.setOnCheckedChangeListener(checkBoxLis);

		registerBtn.setOnClickListener(registerLis);
		backBtn.setOnClickListener(backLis);
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
			Intent intent = new Intent(RegisterActivity.this,
					BasicInfoActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
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
