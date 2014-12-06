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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private boolean adNull = true;
	private boolean paNull = true;

	private EditText accountText, pwdText;
	private Button loginBtn;
	private TextView registerText, loginQuesText;

	private String accountStr, pwdStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		getComponents();

		accountText.addTextChangedListener(accountTextLis);
		pwdText.addTextChangedListener(pwdTextLis);
		loginBtn.setOnClickListener(loginLis);
		registerText.setOnClickListener(registerLis);
	}

	private void getComponents() {
		accountText = (EditText) this.findViewById(R.id.editText1);
		pwdText = (EditText) this.findViewById(R.id.editText2);
		loginBtn = (Button) this.findViewById(R.id.button1);
		registerText = (TextView) this.findViewById(R.id.textView1);
		loginQuesText = (TextView) this.findViewById(R.id.textView2);
		loginBtn.setClickable(false);
	}

	OnClickListener loginLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			accountStr = accountText.getText().toString();
			pwdStr = pwdText.getText().toString();

			if (!(StringHandler.isEmail(accountStr))
					&& !(StringHandler.isMobile(accountStr))) {
				Animation shake = AnimationUtils.loadAnimation(
						LoginActivity.this, R.anim.shake);
				accountText.startAnimation(shake);
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.account_illegal),
						Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			}

		}
	};

	OnClickListener registerLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
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
			// TODO Auto-generated method stub
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
