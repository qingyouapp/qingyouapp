package com.xwq.qingyouapp;

import java.util.ArrayList;
import java.util.Date;
import org.json.JSONException;
import com.google.gson.Gson;
import com.xwq.qingyouapp.bean.Discipline;
import com.xwq.qingyouapp.bean.Grade;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.ConnectionDetector;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.DateHandler;
import com.xwq.qingyouapp.util.EditTextListener;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.KeyValue;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.StringHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class BasicInfoActivity extends Activity {

	private EditText nameText, nicknameText;
	private TextView birthdayText;
	private Button nextBtn;

	private Spinner heightSpin, schoolSpin, discipSpin, gradeSpin;
	private RadioGroup radioGroup;
	private RadioButton maleRadio, femaleRadio;
	// private ArrayAdapter heightAda, schoolAda, discipAda, gradeAda;

	private boolean nameOK = false;
	private boolean nicknameOK = false;
	private LocalStorage localStorage;

	// 获取提交数据
	private String nameStr, nicknameStr;
	private short sexSrt, heightSrt, majorSrt, gradeSrt;
	private Date birthDate;
	private int univerInt;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_basic_info);

		getComponents();
		localStorage = new LocalStorage(this);

		// // temp
		// nameText.setText("夏俊");
		// nameOK = true;
		// nicknameText.setText("Kevin");
		// nicknameOK = true;
		// responseToInput();

		heightSpin.setSelection(30, true);
		schoolSpin.setSelection(0, true);
		discipSpin.setSelection(7, true);
		gradeSpin.setSelection(4, true);

		// set listeners
		nameText.addTextChangedListener(nameLis);
		nicknameText.addTextChangedListener(nicknameLis);
		birthdayText.setOnClickListener(birthdayLis);
		nextBtn.setOnClickListener(nextLis);

		pullInLocalStorage();
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
		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		maleRadio = (RadioButton) this.findViewById(R.id.radioMale);
		femaleRadio = (RadioButton) this.findViewById(R.id.radioFemale);
	}

	OnClickListener nextLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			UserMetadata user = getSubmitInfos();

			Gson gons = new Gson();
			System.out.println("Data Submitted:" + gons.toJson(user));
			String url = getResources().getString(R.string.url_base)
					+ getResources().getString(R.string.url_register);

			try {
				if (new ConnectionDetector(BasicInfoActivity.this).isConnectingToInternet()) {
					Processor processor = Processor.instance(user);
					processor.runCommand(url, StringHandler.objectToJsonString(user), callback);
				} else
					Toast.makeText(getApplication(),
							getResources().getString(R.string.network_exception),
							Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public void setLocalStorage() {
		ArrayList<KeyValue> list = new ArrayList<KeyValue>();
		list.add(new KeyValue("my_name", nameStr));
		list.add(new KeyValue("my_nickname", nicknameStr));
		list.add(new KeyValue("my_sex", sexSrt + ""));
		list.add(new KeyValue("my_height", heightSrt + ""));
		list.add(new KeyValue("my_discipline", majorSrt + ""));
		list.add(new KeyValue("my_grade", gradeSrt + ""));
		list.add(new KeyValue("my_birthday", birthdayText.getText().toString()));
		list.add(new KeyValue("my_school", univerInt + ""));

		localStorage.addData(getApplicationContext(), list);
	}

	CommandCallback callback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			if ("valid".equals(jb)) {
				setLocalStorage();
				Toast.makeText(BasicInfoActivity.this, R.string.register_success, 0);
				Intent intent = new Intent(BasicInfoActivity.this, IdentifyActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	OnClickListener birthdayLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Toast.makeText(getApplicationContext(), "birthday", 1000);
			DatePickerDialog datePicker = new DatePickerDialog(BasicInfoActivity.this, dateSetLis,
					1990, 5, 15);
			datePicker.show();
		}
	};

	OnDateSetListener dateSetLis = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			birthdayText.setText(year + "-" + (month + 1) + "-" + day);
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
		if (nameOK && nicknameOK) {
			nextBtn.setBackgroundResource(R.drawable.btn_normal);
			nextBtn.setEnabled(true);
		} else {
			nextBtn.setBackgroundResource(R.drawable.login_btn_disable);
			nextBtn.setEnabled(false);
		}
	}

	public UserMetadata getSubmitInfos() {
		RadioButton box = (RadioButton) BasicInfoActivity.this.findViewById(radioGroup
				.getCheckedRadioButtonId());
		sexSrt = (short) (box.getText().toString().equals("男") ? 0 : 1);
		nameStr = nameText.getText().toString();

		nicknameStr = nicknameText.getText().toString();
		heightSrt = (short) Integer.parseInt(heightSpin.getSelectedItem().toString());
		birthDate = DateHandler.stringToDate(birthdayText.getText().toString());

		School school = null;
		Discipline discipline = null;
		Grade grade = null;
		String schoolName = schoolSpin.getSelectedItem().toString();
		String discplineName = discipSpin.getSelectedItem().toString();
		String gradeName = gradeSpin.getSelectedItem().toString();
		try {
			school = (School) JsonHandler.getBeanByName(BasicInfoActivity.this, schoolName,
					JsonHandler.TYPE_SCHOOL);
			discipline = (Discipline) JsonHandler.getBeanByName(BasicInfoActivity.this,
					discplineName, JsonHandler.TYPE_DISCIPLINE);
			grade = (Grade) JsonHandler.getBeanByName(BasicInfoActivity.this, gradeName,
					JsonHandler.TYPE_GRADE);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		univerInt = school.getId();
		majorSrt = discipline.getId();
		gradeSrt = grade.getId();

		UserMetadata user = new UserMetadata();
		user.setUsername(nameStr);
		user.setNickname(nicknameStr);
		user.setSex(sexSrt);
		user.setHeight(heightSrt);
		user.setBirthday(birthDate);
		user.setuniversity(univerInt);
		user.setMajor(majorSrt);
		user.setGrade(gradeSrt);
		user.setPhonenum(localStorage.getData("my_phone"));
		user.setPassword(localStorage.getData("my_pwd"));

		return user;
	}

	public void pullInLocalStorage() {
		// get data
		if (localStorage.getData("my_name") != null)
			nameText.setText(localStorage.getData("my_name"));

		if (localStorage.getData("my_nickname") != null)
			nicknameText.setText(localStorage.getData("my_nickname"));

		if (localStorage.getData("my_sex") != null) {
			sexSrt = (short) Integer.parseInt(localStorage.getData("my_sex"));
			maleRadio.setChecked(sexSrt == 0);
			femaleRadio.setChecked(sexSrt == 1);
		}

		if (localStorage.getData("my_birthday") != null) {
			birthdayText.setText(localStorage.getData("my_birthday"));
		}

		if (localStorage.getData("my_height") != null) {
			heightSrt = (short) Integer.parseInt(localStorage.getData("my_height"));
			heightSpin.setSelection(heightSrt - 140, true);
		}

		if (localStorage.getData("my_school") != null) {
			univerInt = Integer.parseInt(localStorage.getData("my_school"));
			schoolSpin.setSelection(univerInt, true);
		}

		if (localStorage.getData("my_discipline") != null) {
			int major = Integer.parseInt(localStorage.getData("my_discipline"));
			discipSpin.setSelection(major, true);
		}

		if (localStorage.getData("my_grade") != null) {
			int grade = Integer.parseInt(localStorage.getData("my_grade"));
			gradeSpin.setSelection(grade, true);
		}

	}

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
