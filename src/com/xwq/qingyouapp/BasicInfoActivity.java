package com.xwq.qingyouapp;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xwq.qingyouapp.bean.Discipline;
import com.xwq.qingyouapp.bean.Grade;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.DateHandler;
import com.xwq.qingyouapp.util.EditTextListener;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.KeyValue;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;

@SuppressLint("ShowToast")
public class BasicInfoActivity extends Activity {

	private EditText nameText, nicknameText;
	private TextView birthdayText;
	private Button registerBtn;

	private Spinner heightSpin, schoolSpin, discipSpin, gradeSpin;
	private RadioGroup radioGroup;
	private RadioButton maleRadio, femaleRadio;
	// private ArrayAdapter heightAda, schoolAda, discipAda, gradeAda;

	private boolean nameOK = false;
	private boolean nicknameOK = false;
	private LocalStorage localStorage;
	private JsonHandler jsonHandler;

	// 获取提交数据
	private String nameStr, nicknameStr;
	private short sexSrt, heightSrt, majorSrt, gradeSrt;
	private Date birthDate;
	private int univerInt, age, constallation;
	private int yearInt = 1990;
	private int monthInt = 5;
	private int dayInt = 15;
	private Processor processor;
	private UserMetadata user;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_basic_info);
		ThisApp.addActivity(this);

		getComponents();
		localStorage = new LocalStorage(this);
		try {
			jsonHandler = new JsonHandler(this);
		} catch (JSONException e) {
			e.printStackTrace();
		}

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
		registerBtn.setOnClickListener(registerLis);

	}

	private void getComponents() {
		nameText = (EditText) this.findViewById(R.id.name);
		nicknameText = (EditText) this.findViewById(R.id.nickname);
		heightSpin = (Spinner) this.findViewById(R.id.height);
		birthdayText = (TextView) this.findViewById(R.id.birthday);
		schoolSpin = (Spinner) this.findViewById(R.id.school);
		discipSpin = (Spinner) this.findViewById(R.id.discipline);
		gradeSpin = (Spinner) this.findViewById(R.id.grade);
		registerBtn = (Button) this.findViewById(R.id.register_button);
		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		maleRadio = (RadioButton) this.findViewById(R.id.radioMale);
		femaleRadio = (RadioButton) this.findViewById(R.id.radioFemale);
	}

	OnClickListener registerLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			user = getSubmitInfos();

			String url = getResources().getString(R.string.url_base)
					+ getResources().getString(R.string.url_register);

			try {
				processor = Processor.instance(BasicInfoActivity.this);
				processor.runCommand(url, StringHandler.userToJsonString(user), registerCallback);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	CommandCallback registerCallback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			// 注册成功之后，执行登录操作，更新User数据（带有id）
			if ("valid".equals(jb)) {
				try {
					String url = getResources().getString(R.string.url_base)
							+ getResources().getString(R.string.url_login);
					processor.runCommand(url, StringHandler.userToJsonString(user), loginCallback);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	CommandCallback loginCallback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			if (jb.indexOf("username") >= 0) {
				Gson gson = new Gson();
				user = gson.fromJson(jb, UserMetadata.class);
				setLocalStorage();
				Intent intent = new Intent(BasicInfoActivity.this, IdentifyActivity.class);
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
		list.add(new KeyValue("my_loginAccount", user.getPhonenum()));
		list.add(new KeyValue("my_pwd", user.getPassword()));
		localStorage.addData(list);
	}

	OnClickListener birthdayLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Toast.makeText(getApplicationContext(), "birthday", 1000);
			DatePickerDialog datePicker = new DatePickerDialog(BasicInfoActivity.this, dateSetLis,
					yearInt, monthInt, dayInt);
			datePicker.show();
		}
	};

	OnDateSetListener dateSetLis = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			birthdayText.setText(year + "-" + (month + 1) + "-" + day);
			responseToInput();
			yearInt = year;
			monthInt = month;
			dayInt = day;
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
			registerBtn.setBackgroundResource(R.drawable.btn_normal);
			registerBtn.setEnabled(true);
		} else {
			registerBtn.setBackgroundResource(R.drawable.login_btn_disable);
			registerBtn.setEnabled(false);
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
		age = new Date().getYear() + 1900 - yearInt;
		constallation = DateHandler.getConstellationInt(monthInt + 1, dayInt);

		School school = null;
		Discipline discipline = null;
		Grade grade = null;
		String schoolName = schoolSpin.getSelectedItem().toString();
		String discplineName = discipSpin.getSelectedItem().toString();
		String gradeName = gradeSpin.getSelectedItem().toString();
		try {
			school = (School) jsonHandler.getBeanByName(schoolName, JsonHandler.TYPE_SCHOOL);
			discipline = (Discipline) jsonHandler.getBeanByName(discplineName,
					JsonHandler.TYPE_DISCIPLINE);
			grade = (Grade) jsonHandler.getBeanByName(gradeName, JsonHandler.TYPE_GRADE);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		univerInt = school.getId();
		majorSrt = discipline.getId();
		gradeSrt = grade.getId();

		UserMetadata user = localStorage.getUser();
		user.setUsername(nameStr);
		user.setNickname(nicknameStr);
		user.setSex(sexSrt);
		user.setHeight(heightSrt);
		user.setBirthday(birthDate.getTime());
		user.setAge(age);
		user.setConstellation((short) constallation);
		user.setUniversity(univerInt);
		user.setMajor(majorSrt);
		user.setGrade(gradeSrt);

		return user;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.basic_info, menu);
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
