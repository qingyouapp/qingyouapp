package com.xwq.qingyouapp.frag;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.TaMainActivity;
import com.xwq.qingyouapp.adapter.RecomListAdapter;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.StringHandler;

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
	private Processor processor;
	private LocalStorage localStorage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_recom_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		processor = Processor.instance(getActivity());
		localStorage = new LocalStorage(getActivity());

		title = (TextView) this.getView().findViewById(R.id.toolbar_title);
		title.setText(getResources().getString(R.string.recommand_history));
		listView = (ListView) this.getView().findViewById(R.id.recom_list);

		// 获取推荐列表
		String url = getActivity().getResources().getString(R.string.url_base)
				+ getActivity().getResources().getString(R.string.url_recommand);
		UserMetadata user = localStorage.getUser();
		try {
			processor.runCommand(url, StringHandler.userToJsonString(user), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	CommandCallback callback = new CommandCallback() {
		@Override
		public void excute(String result) {
			JSONObject jb;
			Gson gson = new Gson();
			ArrayList<UserMetadata> list = new ArrayList<UserMetadata>();
			try {
				//解析返回的UserMetaData集合
				jb = new JSONObject(result);
				JSONArray users = jb.getJSONArray("userMetadata");
				String usersStr = users.toString();
				list = gson.fromJson(usersStr, new TypeToken<ArrayList<UserMetadata>>() {
				}.getType());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//加载至ListView中
			if (list != null && list.size() > 0) {
				adapter = new RecomListAdapter(getActivity(), list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(listViewLis);
			}
		}
	};

	OnItemClickListener listViewLis = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(), TaMainActivity.class);
			startActivity(intent);
		}
	};

}
