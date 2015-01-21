package com.xwq.qingyouapp.adapter;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.util.JsonHandler;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.PhotoHandler.ImageType;
import com.xwq.qingyouapp.util.ThisApp;

public class RecomListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<UserMetadata> list;
	private JsonHandler jsonHandler;
	private PhotoHandler photoHandler;
	private ImageLoader imageloader;

	@SuppressWarnings("deprecation")
	final int FILL_PARENT = LayoutParams.FILL_PARENT;
	final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;

	public RecomListAdapter(Context context, ArrayList<UserMetadata> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		try {
			jsonHandler = new JsonHandler(context);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		photoHandler = new PhotoHandler(context);
		imageloader = ThisApp.imageLoader;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public UserMetadata getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View itemView, ViewGroup parent) {
		final ViewHolder holder;
		if (itemView == null) {
			holder = new ViewHolder();
			itemView = inflater.inflate(R.layout.item_recom_list, null);

			holder.photo = (ImageView) itemView.findViewById(R.id.photo);
			holder.nickname = (TextView) itemView.findViewById(R.id.nickname);
			holder.school = (TextView) itemView.findViewById(R.id.school);
			holder.matchNum = (TextView) itemView.findViewById(R.id.match_num);
			holder.hotNum1 = (TextView) itemView.findViewById(R.id.hot_num_1);
			holder.hotNum2 = (TextView) itemView.findViewById(R.id.hot_num_2);
			holder.date = (TextView) itemView.findViewById(R.id.recom_date);
			holder.latestShuoshuo = (TextView) itemView.findViewById(R.id.latest_shuoshuo);

			itemView.setTag(holder);
		} else {
			holder = (ViewHolder) itemView.getTag();
		}

		UserMetadata user = list.get(position);
		// 获取头像
		Bitmap bitmap = photoHandler.getHeadPortrait(user);
		if (bitmap != null) {
			holder.photo.setImageBitmap(bitmap);
		} else {
			// 如果头像不存在，则立即加载
			String url = photoHandler.getServerPath(user.getUserid()) + user.getHeadPortrait();
			imageloader.displayImage(url, holder.photo);
			photoHandler.downloadImageFromServer(user.getUserid(), user.getHeadPortrait());
		}
		holder.nickname.setText(user.getNickname());
		holder.latestShuoshuo.setText(user.getSignature());

		// school
		int num = user.getUniversity();
		School school;
		try {
			school = (School) jsonHandler.getBeanById(num, JsonHandler.TYPE_SCHOOL);
			holder.school.setText(school.getName());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// holder.matchNum.setText(user.getMatchNum());
		// holder.hotNum1.setText(user.getHotNum1());
		// holder.hotNum2.setText(user.getHotNum2());
		// holder.date.setText(user.getDate());

		// 将userid传入view,这样在ListView中可以唯一标识user和行的对应关系
		itemView.setContentDescription(user.getUserid() + "");
		return itemView;
	}

	private class ViewHolder {
		public TextView nickname, school, matchNum, hotNum1, hotNum2, date, latestShuoshuo;
		public ImageView photo;
	}

}
