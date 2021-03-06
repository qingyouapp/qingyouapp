package com.xwq.qingyouapp.frag;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeUser;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.chat.activity.ChatPage;
import com.xwq.qingyouapp.chat.adapter.MessageListAdapter;
import com.xwq.qingyouapp.chat.base.BaseFragment;
import com.xwq.qingyouapp.chat.view.SwipeMenu;
import com.xwq.qingyouapp.chat.view.SwipeMenuCreator;
import com.xwq.qingyouapp.chat.view.SwipeMenuItem;
import com.xwq.qingyouapp.chat.view.SwipeMenuListView;
import com.xwq.qingyouapp.chat.view.SwipeMenuListView.OnMenuItemClickListener;
@SuppressLint("NewApi")
public class ChatHistoryFrag extends BaseFragment {

	private SwipeMenuListView listView;
	private MessageListAdapter adapter;
	RelativeLayout no_message_view;
//	public static final String fixName = "通知列表";
	private GotyeAPI api = GotyeAPI.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.chat_layout_message_page, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		GotyeAPI.getInstance().addListener(this);
		initView();
	}

	private void initView() {
		TextView toolbar_title = (TextView) getView().findViewById(R.id.toolbar_title);
		toolbar_title.setText("消息");
		no_message_view = (RelativeLayout) getView().findViewById(R.id.no_message_view);
		listView = (SwipeMenuListView) getView().findViewById(R.id.listview);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// Create different menus depending on the view type
//				switch (menu.getViewType()) {
//				case 0:
//					createMenu1(menu);
//					break;
//				case 1:
					createMenu2(menu);
//					break;
//				}
			}
		};
		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public void onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				GotyeChatTarget target = adapter.getItem(position);
				api.deleteSession(target, false);
				updateList();
			}
		});
		int state=api.getOnLineState();
		if(state!=1){
			setErrorTip(0);
		}else{
			setErrorTip(1);
		}
		updateList();
		setListener();
	}
//	private void createMenu1(SwipeMenu menu) {
//		 
//	}

	private void createMenu2(SwipeMenu menu) {
		SwipeMenuItem item2 = new SwipeMenuItem(getActivity());
		item2.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
		item2.setWidth(dp2px(70));
		item2.setIcon(R.drawable.chat_ic_action_discard);
		menu.addMenuItem(item2);
	}

	private void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				GotyeChatTarget target = (GotyeChatTarget) adapter
						.getItem(arg2);
//				if (target.getName().equals(fixName)) {
//					Intent i = new Intent(getActivity(), NotifyListPage.class);
//					startActivity(i);
//				} else {
					GotyeAPI.getInstance().markMeeagesAsread(target);
					if (target.getType() == GotyeChatTargetType.GotyeChatTargetTypeUser) {
						Intent toChat = new Intent(getActivity(),
								ChatPage.class);
						toChat.putExtra("user", (GotyeUser) target);
						startActivity(toChat);
						// updateList();
					} else if (target.getType() == GotyeChatTargetType.GotyeChatTargetTypeRoom) {
						Intent toChat = new Intent(getActivity(),
								ChatPage.class);
						toChat.putExtra("room", (GotyeRoom) target);
						startActivity(toChat);

					} else if (target.getType() == GotyeChatTargetType.GotyeChatTargetTypeGroup) {
						Intent toChat = new Intent(getActivity(),
								ChatPage.class);
						toChat.putExtra("group", (GotyeGroup) target);
						startActivity(toChat);
					}
					refresh();
				}
//			}
		});
	}

	private void updateList() {
		List<GotyeChatTarget> sessions = api.getSessionList();
		Log.d("offLine", "List--sessions" + sessions);

//		GotyeUser target = new GotyeUser();
//		target.setName(fixName);

		if (sessions == null) {
			sessions = new ArrayList<GotyeChatTarget>();
			no_message_view.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
//			sessions.add(target);
		} else if(sessions.size()>0){
			no_message_view.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		} else if(sessions.size()==0){
			no_message_view.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
//		else {
//			sessions.add(0, target);
//		}
		if (adapter == null) {
			adapter = new MessageListAdapter(ChatHistoryFrag.this, sessions);
			listView.setAdapter(adapter);
		} else {
			adapter.setData(sessions);
		}
	}

	public void refresh() {
		updateList();
	}

	@Override
	public void onDestroy() {
		GotyeAPI.getInstance().removeListener(this);
		super.onDestroy();

	}
	@Override
	public void onDownloadMedia(int code, String path, String url) {
		// TODO Auto-generated method stub
		if(getActivity().isTaskRoot()){
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLogout(int code) {
		setErrorTip(0);
	}
	@Override
	public void onLogin(int code, GotyeUser currentLoginUser) {
		setErrorTip(1);
	}
	@Override
	public void onReconnecting(int code, GotyeUser currentLoginUser) {
		setErrorTip(-1);
	}
	
	private void setErrorTip(int code){
//		code=api.getOnLineState();
		if(code==1){
			getView().findViewById(R.id.error_tip).setVisibility(View.GONE);
		}else{
			getView().findViewById(R.id.error_tip).setVisibility(View.VISIBLE);
			if(code==-1){
				getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
				((TextView)getView().findViewById(R.id.showText)).setText("正在连接登陆...");
				getView().findViewById(R.id.error_tip_icon).setVisibility(View.GONE);
			}else{
				getView().findViewById(R.id.loading).setVisibility(View.GONE);
				((TextView)getView().findViewById(R.id.showText)).setText("当前未登陆或网络异常");
				getView().findViewById(R.id.error_tip_icon).setVisibility(View.VISIBLE);
			}
		}
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
