package com.xwq.qingyouapp;

import com.xwq.qingyouapp.util.EditTextListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class BasicInfoActivity extends Activity {

	private EditText nameText, nicknameText;
	private TextView birthdayText;
	private Button nextBtn;

	private Spinner heightSpin, schoolSpin, discipSpin, gradeSpin;
	private ArrayAdapter heightAda, schoolAda, discipAda, gradeAda;

	private boolean nameOK = false;
	private boolean nicknameOK = false;
	private boolean birthdayOK = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_basic_info);

		getComponents();

		// height
		heightAda = ArrayAdapter.createFromResource(this, R.array.height,
				android.R.layout.simple_spinner_item);
		heightAda
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		heightSpin.setAdapter(heightAda);

		// school
		// schoolAda = ArrayAdapter.createFromResource(this, R.array.school,
		// android.R.layout.simple_spinner_item);
		// schoolAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// schoolSpin.setAdapter(schoolAda);

		// discipline
		// discipAda = ArrayAdapter.createFromResource(this, R.array.discipline,
		// android.R.layout.simple_spinner_item);
		// discipAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// discipSpin.setAdapter(discipAda);

		// grade
		// gradeAda = ArrayAdapter.createFromResource(this, R.array.grade,
		// android.R.layout.simple_spinner_item);
		// gradeAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// gradeSpin.setAdapter(gradeAda);

		// set listeners
		nameText.addTextChangedListener(nameLis);
		nicknameText.addTextChangedListener(nicknameLis);
		birthdayText.setOnClickListener(birthdayLis);
		nextBtn.setOnClickListener(nextLis);

	}

	private void getComponents() {
		nameText = (EditText) this.findViewById(R.id.name);
		nicknameText = (EditText) this.findViewById(R.id.nickname);
		heightSpin = (Spinner) this.findViewById(R.id.height);
		birthdayText = (TextView) this.findViewById(R.id.birthday);
		schoolSpin = (Spinner) this.findViewById(R.id.school);
		discipSpin = (Spinner) this.findViewById(R.id.discipline);
		gradeSpin = (Spinner) this.findViewById(R.id.grade);
		nextBtn = (Button) this.findViewById(R.id.necxt_button);
	}

	OnClickListener birthdayLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Toast.makeText(getApplicationContext(), "birthday", 1000);
			DatePickerDialog datePicker = new DatePickerDialog(
					BasicInfoActivity.this, dateSetLis, 1990, 5, 15);
			datePicker.show();
		}
	};

	OnDateSetListener dateSetLis = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			birthdayText.setText(year + "-" + (month + 1) + "-" + day);
			birthdayOK = true;
			responseToInput();
		}
	};

	TextWatcher nameLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			if (s.length() > 0)
				nameOK = true;
			else
				nameOK = false;
			responseToInput();
		}
	};

	TextWatcher nicknameLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			if (s.length() > 0)
				nicknameOK = true;
			else
				nicknameOK = false;
			responseToInput();
		}
	};

	public void responseToInput() {
		if (nameOK && nicknameOK && birthdayOK) {
			nextBtn.setBackgroundResource(R.drawable.btn_normal);
			nextBtn.setEnabled(true);
		} else {
			nextBtn.setBackgroundResource(R.drawable.login_btn_disable);
			nextBtn.setEnabled(false);
		}
	}
	
	OnClickListener nextLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(BasicInfoActivity.this,
					IdentifyActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basic_info, menu);
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
