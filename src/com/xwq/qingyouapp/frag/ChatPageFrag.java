package com.xwq.qingyouapp.frag;

import java.util.ArrayList;

import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.adapter.ChatListAdapter;
import com.xwq.qingyouapp.bean.UserMetadata;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ChatPageFrag extends Fragment {

	private TextView title;
	private ListView listView;
	private ChatListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_chat_page, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

}
