package com.xwq.qingyouapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.bean.UserMetadata;

public class ChatListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<UserMetadata> list;

	@SuppressWarnings("deprecation")
	final int FILL_PARENT = LayoutParams.FILL_PARENT;
	final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;

	public ChatListAdapter(Context contex, ArrayList<UserMetadata> list) {
		this.inflater = LayoutInflater.from(contex);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
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
			itemView = inflater.inflate(R.layout.item_chat_list, null);

			holder.photo = (ImageView) itemView.findViewById(R.id.photo);
			holder.nickname = (TextView) itemView.findViewById(R.id.nickname);
			holder.latestShuoshuo = (TextView) itemView.findViewById(R.id.latest_shuoshuo);
			holder.date = (TextView) itemView.findViewById(R.id.recom_date);

			itemView.setTag(holder);
		} else {
			holder = (ViewHolder) itemView.getTag();
		}

		// holder.photo
		switch (position) {
		case 0:
			holder.photo.setImageResource(R.drawable.girl1);
			holder.latestShuoshuo.setText(R.string.latest_shuoshuo_2);
			break;
		case 1:
			holder.photo.setImageResource(R.drawable.girl2);
			break;
		case 2:
			holder.photo.setImageResource(R.drawable.girl3);
			holder.latestShuoshuo.setText(R.string.latest_shuoshuo_2);
			break;
		case 3:
			holder.photo.setImageResource(R.drawable.girl4);
			break;
		case 4:
			holder.photo.setImageResource(R.drawable.girl5);
			holder.latestShuoshuo.setText(R.string.latest_shuoshuo_2);
			break;
		case 5:
			holder.photo.setImageResource(R.drawable.girl6);
			break;
		case 6:
			holder.photo.setImageResource(R.drawable.girl7);
			holder.latestShuoshuo.setText(R.string.latest_shuoshuo_2);
			break;
		case 7:
			holder.photo.setImageResource(R.drawable.girl1);
			break;
		}
		holder.nickname.setText(list.get(position).getNickname());
//		holder.date.setText(list.get(position).getDate());
		return itemView;
	}

	private class ViewHolder {
		public TextView nickname, date, latestShuoshuo;
		public ImageView photo;
	}

}
