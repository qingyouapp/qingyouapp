package com.xwq.qingyouapp.adapter;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.bean.School;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.util.JsonHandler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class RecomListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<UserMetadata> list;
	private JsonHandler jsonHandler;

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

//		holder.photo.setImageResource(R.drawable.girl1);
		holder.nickname.setText(list.get(position).getNickname());
		holder.latestShuoshuo.setText(list.get(position).getSignature());
		
		//school
		int num = list.get(position).getUniversity();
		School school;
		try {
			school = (School)jsonHandler.getBeanById(num, JsonHandler.TYPE_SCHOOL);
			holder.school.setText(school.getName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		holder.matchNum.setText(list.get(position).getMatchNum());
//		holder.hotNum1.setText(list.get(position).getHotNum1());
//		holder.hotNum2.setText(list.get(position).getHotNum2());
//		holder.date.setText(list.get(position).getDate());

		return itemView;
	}

	private class ViewHolder {
		public TextView nickname, school, matchNum, hotNum1, hotNum2, date, latestShuoshuo;
		public ImageView photo;
	}

}
