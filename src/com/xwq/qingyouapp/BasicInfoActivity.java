package com.xwq.qingyouapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.json.JSONException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
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
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;
import com.xwq.qingyouapp.util.PhotoHandler.ImageType;

@SuppressLint("ShowToast")
public class BasicInfoActivity extends Activity {

	private EditText nameText, nicknameText;
	private TextView birthdayText;
	private Button registerBtn;
	private ImageView photoView;

	private Spinner heightSpin, schoolSpin, discipSpin, gradeSpin;
	private RadioGroup radioGroup;

	private boolean nameOK = false;
	private boolean nicknameOK = false;
	private boolean photoOK = false;
	private Bitmap photoBitmap;
	private String photoName;
	private LocalStorage localStorage;
	private JsonHandler jsonHandler;
	private PhotoHandler photoHandler;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_basic_info);
		ThisApp.addActivity(this);

		getComponents();
		localStorage = new LocalStorage(this);
		photoHandler = new PhotoHandler(this);
		try {
			jsonHandler = new JsonHandler(this);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		heightSpin.setSelection(30, true);
		schoolSpin.setSelection(0, true);
		discipSpin.setSelection(7, true);
		gradeSpin.setSelection(4, true);

		// 圆角头像
		Drawable drawable = getResources().getDrawable(R.drawable.head_default);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		photoView.setImageBitmap(toRoundCorner(bitmap));

		// set listeners
		nameText.addTextChangedListener(nameLis);
		nicknameText.addTextChangedListener(nicknameLis);
		birthdayText.setOnClickListener(birthdayLis);
		registerBtn.setOnClickListener(registerLis);
		photoView.setOnClickListener(photoLis);

		// temp
//		nameText.setText("夏俊");
//		nameOK = true;
//		nicknameText.setText("Kevin");
//		nicknameOK = true;
//		responseToInput();
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
		photoView = (ImageView) this.findViewById(R.id.photo_upload);
	}

	OnClickListener photoLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			showPhotoSelectDialog();
		}
	};

	private void showPhotoSelectDialog() {
		new AlertDialog.Builder(this)
				.setTitle(null)
				.setItems(ImageUploadActivity.items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									ImageUploadActivity.IMAGE_REQUEST_CODE);
							break;
						case 1:
							Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (ImageUploadActivity.hasSdcard()) {
								intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												ImageUploadActivity.IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture,
									ImageUploadActivity.CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();

	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView() {
		photoBitmap = decodeUriAsBitmap(EditInfoActivity.IMAGE_URL);
		photoView.setImageBitmap(toRoundCorner(photoBitmap));
		photoOK = true;
		responseToInput();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED)
			switch (requestCode) {
			// 处理上传图片的回传结果
			case ImageUploadActivity.IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case ImageUploadActivity.CAMERA_REQUEST_CODE:
				if (ImageUploadActivity.hasSdcard()) {
					File tempFile = new File(Environment.getExternalStorageDirectory() + "/"
							+ ImageUploadActivity.IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(BasicInfoActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}
				break;
			case ImageUploadActivity.RESULT_REQUEST_CODE:
				getImageToView();
				break;
			}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 控制宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 720);
		intent.putExtra("outputY", 720);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, EditInfoActivity.IMAGE_URL);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, ImageUploadActivity.RESULT_REQUEST_CODE);
	}

	public Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	OnClickListener registerLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {

			user = getSubmitInfos();
			// 提前传入头像名称，命名为.0_开头，而不是用userid
			photoName = ".0_" + Calendar.getInstance().getTimeInMillis() + ".png";
			user.setHeadPortrait(photoName);
			user.setPhotoAlbum(photoName);

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
			//注册成功，返回user对象
			if (jb.indexOf("username") >= 0) {
				user = new Gson().fromJson(jb, UserMetadata.class);
				photoHandler.saveBitmaps(user.getUserid(), photoName, photoBitmap);
				// 上传图片至服务器
				String path = photoHandler.getLocalAbsolutePath(user.getUserid(), ImageType.Album) + photoName;
				HashSet<String> paths = new HashSet<String>();
				paths.add(path);
				processor.refreshPhoto(paths, "", user, photoCallback);
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	CommandCallback photoCallback = new CommandCallback() {
		@SuppressLint("ShowToast")
		@Override
		public void excute(String jb) {
			// 注册成功之后，执行登录操作，更新User数据（带有id）
			if ("200".equals(jb.trim())) {
				setLocalStorage();
				Intent intent = new Intent(BasicInfoActivity.this, IdentifyActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.server_exception), Toast.LENGTH_SHORT)
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
		if (nameOK && nicknameOK && photoOK) {
			registerBtn.setBackgroundResource(R.drawable.btn_normal);
			registerBtn.setEnabled(true);
		} else {
			registerBtn.setBackgroundResource(R.drawable.login_btn_disable);
			registerBtn.setEnabled(false);
		}
	}

	@SuppressWarnings("deprecation")
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
		user.setVersion(0);

		return user;
	}

	// 获取圆角图片
	public Bitmap toRoundCorner(Bitmap bitmap) {
		Bitmap output = Bitmap
				.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		// 参数0.1定义了圆角占图片的比例
		float roundPx = (float) (bitmap.getWidth() * 0.1);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
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
