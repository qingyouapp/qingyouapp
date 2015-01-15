package com.xwq.qingyouapp;

import org.json.JSONException;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.EditTextListener;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class IdentifyActivity extends Activity {

	public enum IDENTITY_TYPE {
		Email, Photo, Friends
	};

	private RadioButton emailRadio, photoRadio, friendRadio;
	private EditText emailText, friend1Text, friend2Text;
	private TextView photoView, passBtn;
	private Button finishBtn;

	private boolean friend1OK = false;
	private boolean friend2OK = false;

	private IDENTITY_TYPE iden_type;
	private LocalStorage localStorage;
	private UserMetadata user;
	private Processor processor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_identify);
		// ThisApp.clearActivities();
		ThisApp.addActivity(this);

		getComponents();
		localStorage = new LocalStorage(this);
		initComponents(IDENTITY_TYPE.Email);

		emailRadio.setOnClickListener(emailLis);
		photoRadio.setOnClickListener(photoLis);
		friendRadio.setOnClickListener(friendLis);

		emailText.addTextChangedListener(emailTextLis);
		friend1Text.addTextChangedListener(friend1TextLis);
		friend2Text.addTextChangedListener(friend2TextLis);

		finishBtn.setOnClickListener(finishBtnLis);
		photoView.setOnClickListener(uploadPhotoLis);
		passBtn.setOnClickListener(passBtnLis);

		// temp
		emailText.setText("136872734@qq.com");
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
		passBtn = (TextView) this.findViewById(R.id.pass_btn);
	}

	public void initComponents(IDENTITY_TYPE type) {
		switch (type) {
		case Email:
			setEmailStatus(true);
			setPhotoStatus(false);
			setFriendStatus(false);
			setFinishButton(StringHandler.isEmail(emailText.getText().toString()));
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

	OnClickListener passBtnLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(IdentifyActivity.this, SysMainActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener emailLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			iden_type = IDENTITY_TYPE.Email;
			initComponents(iden_type);
		}
	};
	OnClickListener photoLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			iden_type = IDENTITY_TYPE.Photo;
			initComponents(iden_type);
		}
	};
	OnClickListener friendLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			iden_type = IDENTITY_TYPE.Friends;
			initComponents(iden_type);
		}
	};

	OnClickListener uploadPhotoLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(IdentifyActivity.this, ImageUploadActivity.class);
			startActivity(intent);
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
			String updateUrl = getResources().getString(R.string.url_base)
					+ getResources().getString(R.string.url_update);

			user = localStorage.getUser();
			user.setEmail(emailText.getText().toString().trim());
			processor = Processor.instance(IdentifyActivity.this);
			try {
				processor.runCommand(updateUrl, StringHandler.userToJsonString(user),
						updatecallback);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	CommandCallback updatecallback = new CommandCallback() {
		@Override
		public void excute(String jb) {
			System.out.println("post result--" + jb.length() + ":" + jb);
			if ("valid".equals(jb.trim())) {
				String sendMailUrl = getResources().getString(R.string.url_base)
						+ getResources().getString(R.string.url_verify);
				try {
					processor.runCommand(sendMailUrl, StringHandler.userToJsonString(user),
							verifycallback);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
		}
	};

	CommandCallback verifycallback = new CommandCallback() {
		@Override
		public void excute(String jb) {
			System.out.println("post result--" + jb.length() + ":" + jb);
			if ("valid".equals(jb.trim())) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.mail_sent_alert), Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(IdentifyActivity.this, SysMainActivity.class);
				startActivity(intent);
			} else
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
		}
	};

	public void setEmailStatus(boolean status) {

		emailRadio.setChecked(status);
		emailText.setEnabled(status);

		if (status) {
			emailText.setHintTextColor(R.color.hint_color);
			emailText.setBackgroundResource(R.drawable.input_box);
		} else {
			emailText.setHintTextColor(R.color.disabled_color);
			emailText.setBackgroundResource(R.drawable.addphoto_box);
		}
	}

	public void setPhotoStatus(boolean status) {
		photoRadio.setChecked(status);
		photoView.setClickable(status);

		if (status) {
			photoView.setHintTextColor(R.color.hint_color);
			photoView.setBackgroundResource(R.drawable.input_box);
		} else {
			photoView.setHintTextColor(R.color.disabled_color);
			photoView.setBackgroundResource(R.drawable.addphoto_box);
		}
	}

	public void setFriendStatus(boolean status) {
		friendRadio.setChecked(status);
		friend1Text.setEnabled(status);
		friend2Text.setEnabled(status);

		if (status) {
			friend1Text.setHintTextColor(R.color.hint_color);
			friend1Text.setBackgroundResource(R.drawable.input_box);
			friend2Text.setHintTextColor(R.color.hint_color);
			friend2Text.setBackgroundResource(R.drawable.input_box);
		} else {
			friend1Text.setHintTextColor(R.color.disabled_color);
			friend1Text.setBackgroundResource(R.drawable.addphoto_box);
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
