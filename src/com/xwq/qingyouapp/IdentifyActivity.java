package com.xwq.qingyouapp;

import com.xwq.qingyouapp.util.EditTextListener;
import com.xwq.qingyouapp.util.StringHandler;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class IdentifyActivity extends Activity {

	public enum IDENTITY_TYPE {
		Email, Photo, Friends
	};

	private RadioButton emailRadio, photoRadio, friendRadio;
	private EditText emailText, friend1Text, friend2Text;
	private TextView photoView;
	private Button finishBtn;
	private ImageButton back;

	private boolean friend1OK = false;
	private boolean friend2OK = false;

	private IDENTITY_TYPE iden_type = IDENTITY_TYPE.Email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_identify);

		getComponents();
		initComponents(iden_type);

		emailRadio.setOnClickListener(emailLis);
		photoRadio.setOnClickListener(photoLis);
		friendRadio.setOnClickListener(friendLis);

		emailText.addTextChangedListener(emailTextLis);
		friend1Text.addTextChangedListener(friend1TextLis);
		friend2Text.addTextChangedListener(friend2TextLis);

		finishBtn.setOnClickListener(finishBtnLis);
		back.setOnClickListener(backLis);
	}

	public void getComponents() {
		emailRadio = (RadioButton) this.findViewById(R.id.email_iden);
		photoRadio = (RadioButton) this.findViewById(R.id.photo_iden);
		friendRadio = (RadioButton) this.findViewById(R.id.friend_iden);
		emailText = (EditText) this.findViewById(R.id.input_email);
		friend1Text = (EditText) this.findViewById(R.id.friend_1);
		friend2Text = (EditText) this.findViewById(R.id.friend_2);
		photoView = (TextView) this.findViewById(R.id.upload_photo);
		finishBtn = (Button) this.findViewById(R.id.finish_button);
		back = (ImageButton) this.findViewById(R.id.iden_back);
	}

	public void initComponents(IDENTITY_TYPE type) {
		switch (type) {
		case Email:
			setEmailStatus(true);
			setPhotoStatus(false);
			setFriendStatus(false);
			setFinishButton(StringHandler.isEmail(emailText.getText()
					.toString()));
			break;
		case Photo:
			setEmailStatus(false);
			setPhotoStatus(true);
			setFriendStatus(false);
			setFinishButton(false);
			break;
		case Friends:
			setEmailStatus(false);
			setPhotoStatus(false);
			setFriendStatus(true);
			setFinishButton(friend1OK && friend2OK);
			break;
		}
	}

	OnClickListener emailLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Toast.makeText(getApplicationContext(), "birthday", 1000);
			iden_type = IDENTITY_TYPE.Email;
			initComponents(iden_type);
		}
	};
	OnClickListener photoLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Toast.makeText(getApplicationContext(), "birthday", 1000);
			iden_type = IDENTITY_TYPE.Photo;
			initComponents(iden_type);
		}
	};
	OnClickListener friendLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// Toast.makeText(getApplicationContext(), "birthday", 1000);
			iden_type = IDENTITY_TYPE.Friends;
			initComponents(iden_type);
		}
	};

	TextWatcher emailTextLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			if (StringHandler.isEmail(s.toString()))
				setFinishButton(true);
			else
				setFinishButton(false);

		}
	};

	TextWatcher friend1TextLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			if (StringHandler.isMobile(s.toString()))
				friend1OK = true;
			else
				friend1OK = false;
			if (friend1OK && friend2OK)
				setFinishButton(true);
			else
				setFinishButton(false);
		}
	};

	TextWatcher friend2TextLis = new EditTextListener() {
		@Override
		public void afterTextChanged(Editable s) {
			super.afterTextChanged(s);
			if (StringHandler.isMobile(s.toString()))
				friend2OK = true;
			else
				friend2OK = false;
			if (friend1OK && friend2OK)
				setFinishButton(true);
			else
				setFinishButton(false);
		}
	};

	OnClickListener finishBtnLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(IdentifyActivity.this,
					MainActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
		}
	};
	
	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			IdentifyActivity.this.finish();
		}
	};

	public void setEmailStatus(boolean status) {
		if (status) {
			emailRadio.setChecked(true);
			emailText.setEnabled(true);
			emailText.setHintTextColor(R.color.hint_color);
			emailText.setBackgroundResource(R.drawable.input_box);
		} else {
			emailRadio.setChecked(false);
			emailText.setEnabled(false);
			emailText.setHintTextColor(R.color.disabled_color);
			emailText.setBackgroundResource(R.drawable.addphoto_box);
		}
	}

	public void setPhotoStatus(boolean status) {
		if (status) {
			photoRadio.setChecked(true);
			photoView.setClickable(true);
		} else {
			photoRadio.setChecked(false);
			photoView.setClickable(false);
		}
	}

	public void setFriendStatus(boolean status) {
		if (status) {
			friendRadio.setChecked(true);
			friend1Text.setEnabled(true);
			friend1Text.setHintTextColor(R.color.hint_color);
			friend1Text.setBackgroundResource(R.drawable.input_box);
			friend2Text.setEnabled(true);
			friend2Text.setHintTextColor(R.color.hint_color);
			friend2Text.setBackgroundResource(R.drawable.input_box);
		} else {
			friendRadio.setChecked(false);
			friend1Text.setEnabled(false);
			friend1Text.setHintTextColor(R.color.disabled_color);
			friend1Text.setBackgroundResource(R.drawable.addphoto_box);
			friend2Text.setEnabled(false);
			friend2Text.setHintTextColor(R.color.disabled_color);
			friend2Text.setBackgroundResource(R.drawable.addphoto_box);
		}
	}

	public void setFinishButton(boolean sta) {
		if (sta) {
			finishBtn.setBackgroundResource(R.drawable.btn_normal);
			finishBtn.setEnabled(true);
		} else {
			finishBtn.setBackgroundResource(R.drawable.login_btn_disable);
			finishBtn.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.identify, menu);
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
