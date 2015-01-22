package com.xwq.qingyouapp;

import com.xwq.qingyouapp.util.EditTextListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class IdentifyCodeActivity extends Activity {

	private TextView phoneview, codeview;
	private Button button;
	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_identify_code);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String phone = bundle.getString("photoString");
		String code = bundle.getString("codeString");

		phoneview = (TextView) this.findViewById(R.id.phone_number);
		codeview = (TextView) this.findViewById(R.id.identify_code);
		button = (Button) this.findViewById(R.id.next_button);
		back = (ImageButton) this.findViewById(R.id.identify_code_back);

		phoneview.setText(phone);
		codeview.setText(code);

		codeview.addTextChangedListener(codeLis);
		button.setOnClickListener(buttonLis);
		back.setOnClickListener(backLis);
	}

	OnClickListener buttonLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(IdentifyCodeActivity.this, BasicInfoActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			IdentifyCodeActivity.this.finish();
		}
	};

	TextWatcher codeLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			if (s.length() == 6) {
				button.setEnabled(true);
				button.setBackgroundResource(R.drawable.btn_normal);
			} else {
				button.setEnabled(false);
				button.setBackgroundResource(R.drawable.login_btn_disable);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.identify_code, menu);
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
