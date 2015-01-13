package com.xwq.qingyouapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import com.xwq.qingyouapp.frag.ImagePagerFragment;

public class PhotoShowActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_show);

		Fragment fr;
		String tag = ImagePagerFragment.class.getSimpleName();
		fr = getSupportFragmentManager().findFragmentByTag(tag);
		if (fr == null) {
			fr = new ImagePagerFragment();
		}

		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag)
				.commit();

	}

}
