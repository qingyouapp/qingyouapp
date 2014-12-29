package com.xwq.qingyouapp.frag;

import java.util.ArrayList;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.TaMainActivity;
import com.xwq.qingyouapp.adapter.RecomListAdapter;
import com.xwq.qingyouapp.bean.UserMetadata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RecomHistoryFrag extends Fragment {

	private TextView title;
	private ListView listView;
	private RecomListAdapter adapter;
	private Activity thisActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_recom_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		thisActivity = this.getActivity();
		title = (TextView) this.getView().findViewById(R.id.toolbar_title);
		listView = (ListView) this.getView().findViewById(R.id.recom_list);
		ArrayList<UserMetadata> list = new ArrayList<UserMetadata>();
//		UserMetadata user = new UserMetadata("企鹅", "中国科学技术大学", "91", "23", "31", "今天中午");
//		UserMetadata user1 = new UserMetadata("慢N拍", "复旦大学", "100", "65", "23", "今天中午");
//		UserMetadata user2 = new UserMetadata("Lucy", "上海交通大学", "89", "43", "65", "昨天晚上");
//		UserMetadata user3 = new UserMetadata("茉莉花", "合肥工业大学", "78", "34", "54", "昨天晚上");
//		UserMetadata user4 = new UserMetadata("宝宝", "中国科学技术大学", "98", "26", "84", "昨天中午");
//		UserMetadata user5 = new UserMetadata("Kevin", "安徽大学", "96", "34", "31", "昨天中午");
//		UserMetadata user6 = new UserMetadata("风雪月", "清华大学", "92", "63", "71", "12月13日晚上");
//		UserMetadata user7 = new UserMetadata("小乌龟", "华南理工大学", "31", "64", "36", "12月13日晚上");
//		UserMetadata user8 = new UserMetadata("北极熊", "中国科学技术大学", "91", "23", "31", "12月13日中午");
//		list.add(user);
//		list.add(user1);
//		list.add(user2);
//		list.add(user3);
//		list.add(user4);
//		list.add(user5);
//		list.add(user6);
//		list.add(user7);
//		list.add(user8);
		adapter = new RecomListAdapter(this.getActivity(), list);

		title.setText(getResources().getString(R.string.recommand_history));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listViewLis);

	}

	OnItemClickListener listViewLis = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(thisActivity, TaMainActivity.class);
			startActivity(intent);
		}
	};

}
