package com.xwq.qingyouapp.frag;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.TaMainActivity;
import com.xwq.qingyouapp.adapter.RecomListAdapter;
import com.xwq.qingyouapp.bean.UserMetadata;
import com.xwq.qingyouapp.command.CommandCallback;
import com.xwq.qingyouapp.command.Processor;
import com.xwq.qingyouapp.util.LocalStorage;
import com.xwq.qingyouapp.util.PhotoHandler;
import com.xwq.qingyouapp.util.StringHandler;
import com.xwq.qingyouapp.util.ThisApp;

public class RecomHistoryFrag extends Fragment {

	private TextView title;
	private ListView listView;
	private RelativeLayout noRecomView;
	private RecomListAdapter adapter;
	private Processor processor;
	private LocalStorage localStorage;
	private ArrayList<UserMetadata> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_recom_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		processor = Processor.instance(getActivity());
		localStorage = new LocalStorage(getActivity());
		list = new ArrayList<UserMetadata>();

		title = (TextView) this.getView().findViewById(R.id.toolbar_title);
		title.setText(getResources().getString(R.string.recommand_history));
		listView = (ListView) this.getView().findViewById(R.id.recom_list);
		noRecomView = (RelativeLayout) this.getView().findViewById(R.id.no_recom_view);

		list = localStorage.getRecommendList();
		if (list != null && list.size() > 0) {
			noRecomView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			adapter = new RecomListAdapter(getActivity(), list);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(listViewLis);
		}
		getLatestRecommend();
	}

	// 获取最新推荐列表
	public void getLatestRecommend() {
		AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>() {
			@Override
			protected String doInBackground(Object... arg0) {
				String url = getActivity().getResources().getString(R.string.url_base)
						+ getActivity().getResources().getString(R.string.url_recommand);
				// 传参数时使用简化版的user对象
				UserMetadata user = localStorage.getUser();
				UserMetadata newUser = new UserMetadata(user.getUserid(), user.getPassword());
				try {
					processor.runCommand(url, StringHandler.userToJsonString(newUser), callback);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		task.execute();
	}

	CommandCallback callback = new CommandCallback() {
		@Override
		public void excute(String result) {
			JSONObject jb;
			Gson gson = new Gson();
			try {
				// 解析返回的UserMetaData集合
				jb = new JSONObject(result);
				JSONArray users = jb.getJSONArray("userMetadata");
				String usersStr = users.toString();
				list = gson.fromJson(usersStr, new TypeToken<ArrayList<UserMetadata>>() {
				}.getType());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// 加载至ListView中
			if (list != null && list.size() > 0) {
				noRecomView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				localStorage.setRecommendList(list);
				adapter = new RecomListAdapter(getActivity(), list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(listViewLis);
			}
		}
	};

	OnItemClickListener listViewLis = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 根据view获取之前存入的userid，并存入全局变量
			int userid = Integer.parseInt(view.getContentDescription().toString());
			ThisApp.SHOW_USER_ID = userid;
			
			Intent intent = new Intent(getActivity(), TaMainActivity.class);
			startActivity(intent);
		}
	};

}
