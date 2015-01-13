package com.xwq.qingyouapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xwq.qingyouapp.EditInfoActivity.TAG_TYPE;
import com.xwq.qingyouapp.adapter.TagsGirdAdapter;
import com.xwq.qingyouapp.util.StringHandler;

public class TagsEditActivity extends Activity {

	private String tagString;
	private EditText editText;
	private ImageButton backBtn;
	private Bundle bundle;
	private TextView toolbarTitle;
	private GridView gridView;
	private String totalString;
	private TAG_TYPE tagType;
	private String[] totalArr, selectedArr;
	private int selectedColor = 0;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tags_edit);
		getComponents();
		bundle = getIntent().getExtras();
		tagString = bundle.getString("tag_string");
		editText.setText(tagString);

		int tag_code = bundle.getInt("tag_code");
		switch (tag_code) {
		case EditInfoActivity.TAG_HOBBY:
			toolbarTitle.setText(getResources().getString(R.string.tags_edit_title_1));
			totalString = getResources().getString(R.string.hobby_tag_string);
			tagType = TAG_TYPE.Hobby_tag;
			selectedColor = R.color.hobby_tag_bg;
			break;
		case EditInfoActivity.TAG_PERSONAL:
			toolbarTitle.setText(getResources().getString(R.string.tags_edit_title_2));
			totalString = getResources().getString(R.string.personal_tag_string);
			tagType = TAG_TYPE.Personal_tag;
			selectedColor = R.color.personal_tag_bg;
			break;
		case EditInfoActivity.TAG_TA_PERSONAL:
			toolbarTitle.setText(getResources().getString(R.string.tags_edit_title_3));
			totalString = getResources().getString(R.string.personal_tag_string);
			tagType = TAG_TYPE.Ta_personal_tag;
			selectedColor = R.color.personal_tag_bg;
			break;
		}

		totalArr = StringHandler.formatString(totalString).split("，");
		selectedArr = StringHandler.formatString(tagString).split("，");
		TagsGirdAdapter adapter = new TagsGirdAdapter(this, totalArr, selectedArr, tag_code);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(tagGridLis);
		backBtn.setOnClickListener(backLis);
	}

	public void getComponents() {
		editText = (EditText) this.findViewById(R.id.edit_text);
		backBtn = (ImageButton) this.findViewById(R.id.tags_edit_back);
		toolbarTitle = (TextView) this.findViewById(R.id.toolbar_title);
		// layout = (GridLayout) this.findViewById(R.id.grid_layout);
		gridView = (GridView) this.findViewById(R.id.gridview);
	}

	public void backToEditInfoActivity() {
		Intent intent = new Intent();
		intent.putExtra("tagString", editText.getText().toString());
		setResult(bundle.getInt("tag_code"), intent);
		TagsEditActivity.this.finish();
	}

	OnClickListener backLis = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			backToEditInfoActivity();
		}
	};

	OnItemClickListener tagGridLis = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			TextView textView = (TextView) view.findViewById(R.id.item_text);
			boolean selected = StringHandler.tagInString(tagString, textView.getText().toString());

			if (selected) {
				textView.setBackgroundResource(R.drawable.shape_rectangle_3);
				textView.setTextColor(getResources().getColor(R.color.grey_font_color_1));
				tagString = StringHandler.removeTagFromString(tagString, textView.getText()
						.toString());
			} else {
				textView.setBackgroundResource(selectedColor);
				textView.setTextColor(Color.WHITE);
				if (tagString.length() == 0)
					tagString += textView.getText();
				else
					tagString += "，" + textView.getText();
			}
			editText.setText(tagString);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backToEditInfoActivity();
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tags_edit, menu);
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
