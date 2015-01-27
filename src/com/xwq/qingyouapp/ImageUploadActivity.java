package com.xwq.qingyouapp;

import java.io.File;

import com.xwq.qingyouapp.chat.util.URIUtil;
import com.xwq.qingyouapp.util.ThisApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageUploadActivity extends Activity {

	private ImageView uploadImage;
	private ImageButton back;
	private Button finishBtn;
	private boolean finishBtnStatus = false;

	public static String[] items = new String[] { "从相册选择", "拍照" };
	// 名称加点，目录下不可见
	public static final String IMAGE_FILE_NAME = ".qingyoutemp.png";
	/* 请求码 */
	public static final int IMAGE_REQUEST_CODE = 1;
	public static final int CAMERA_REQUEST_CODE = 2;
	public static final int RESULT_REQUEST_CODE = 3;
	public static final int IMAGE_REQUEST_CODE_Kitkat = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title setting
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_upload);
		
		uploadImage = (ImageView) this.findViewById(R.id.upload_image);
		back = (ImageButton) this.findViewById(R.id.iden_back);
		finishBtn = (Button) this.findViewById(R.id.finish_button);

		uploadImage.setOnClickListener(uploadImageLis);
		back.setOnClickListener(backLis);
		finishBtn.setOnClickListener(finishBtnLis);
		showDialog();
	}

	OnClickListener uploadImageLis = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showDialog();
		}

	};

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			ImageUploadActivity.this.finish();
		}
	};

	OnClickListener finishBtnLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(ImageUploadActivity.this, SysMainActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	};

	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle(null)
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
							if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){                  
								startActivityForResult(intentFromGallery, ImageUploadActivity.IMAGE_REQUEST_CODE_Kitkat);    
							}else{                
								startActivityForResult(intentFromGallery, ImageUploadActivity.IMAGE_REQUEST_CODE);   
							} 
							break;
						case 1:
							Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (hasSdcard()) {
								intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(Environment
												.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (finishBtnStatus)
									dialog.dismiss();
								else
									ImageUploadActivity.this.finish();
							}
						}).show();
		;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (hasSdcard()) {
					File tempFile = new File(Environment.getExternalStorageDirectory() + "/"
							+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(ImageUploadActivity.this, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			case IMAGE_REQUEST_CODE_Kitkat:
				String path = URIUtil.getPath(this, data.getData());
				File file = new File(path);
				startPhotoZoom(Uri.fromFile(file));
				break;
			}
		}

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
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true);
		intent.putExtra("circleCrop", new String(""));
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(photo);
			uploadImage.setImageDrawable(drawable);
			setFinishButton(true);
		}
	}

	public void setFinishButton(boolean sta) {
		if (sta) {
			finishBtn.setBackgroundResource(R.drawable.btn_normal);
			finishBtn.setEnabled(true);
			finishBtnStatus = true;
		} else {
			finishBtn.setBackgroundResource(R.drawable.login_btn_disable);
			finishBtn.setEnabled(false);
			finishBtnStatus = false;
		}
	}

	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_upload, menu);
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
