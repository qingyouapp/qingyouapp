package com.xwq.qingyouapp.chat.activity;

import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeNotify;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.chat.adapter.NotifyListAdapter;
import com.xwq.qingyouapp.chat.base.BaseActivity;
import com.xwq.qingyouapp.chat.util.ProgressDialogUtil;
import com.xwq.qingyouapp.chat.util.ToastUtil;
import com.xwq.qingyouapp.chat.view.SwipeMenu;
import com.xwq.qingyouapp.chat.view.SwipeMenuCreator;
import com.xwq.qingyouapp.chat.view.SwipeMenuItem;
import com.xwq.qingyouapp.chat.view.SwipeMenuListView;
import com.xwq.qingyouapp.chat.view.SwipeMenuListView.OnMenuItemClickListener;

public class NotifyListPage extends BaseActivity {
	private SwipeMenuListView listView;
	private NotifyListAdapter adapter;

	private List<GotyeNotify> notifies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_layout_notify_page);
		api.addListener(this);
		listView = (SwipeMenuListView) findViewById(R.id.listview);
		setListview();
		loadData();
	}

	private void setListview() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				createMenu(menu);
			}

			private void createMenu(SwipeMenu menu) {

				SwipeMenuItem item1 = new SwipeMenuItem(getApplicationContext());
				item1.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				
				item1.setWidth(dp2px(90));
//				item1.setIcon(R.drawable.chat_ic_action_about);

				// menu.addMenuItem(item1);

				SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
				item2.setBackground(new ColorDrawable(Color.rgb(0xE5, 0x18,
						0x5E)));
				item2.setWidth(dp2px(90));
//				item2.setIcon(R.drawable.chat_ic_action_discard);

				// menu.addMenuItem(item2);

				int type = menu.getViewType();
				switch (type) {
				case 0:
					item2.setTitle("删除");
					menu.addMenuItem(item2);
					break;
				case 1:
					item1.setTitle("同意");
					menu.addMenuItem(item1);

					item2.setTitle("拒绝");
					menu.addMenuItem(item2);
					break;
				case 2:
					item2.setTitle("删除");
					menu.addMenuItem(item2);
					break;
				case 3:
					
					item1.setTitle("知道了");
					menu.addMenuItem(item1);
					break;
				case 4:
					item2.setTitle("删除");
					menu.addMenuItem(item2);
					break;
				case 5:
					item1.setTitle("同意");
					menu.addMenuItem(item1);

					item2.setTitle("拒绝");
					menu.addMenuItem(item2);
					
					break;
				default:
					break;
				}

			}
		};
		// set creator
		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			public void onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				GotyeNotify notify = adapter.getItem(position);
				switch (notify.getType()) {
				case GroupInvite:
					if (notify.isRead()) {
						api.deleteNotify(notify);
						refresh();

					} else {
						if (index == 0) {
							api.joinGroup(new GotyeGroup(notify.getFrom().getId()));
							notify.setRead(true);
							api.markNotifyIsread(notify);
							ProgressDialogUtil.showProgress(
									NotifyListPage.this, "正在加入..");
							refresh();
							
						} else {
							notify.setRead(true);
							api.markNotifyIsread(notify);
							refresh();
						}
					}
					break;
				case JoinGroupReply:
					if (notify.isRead()) {
						api.deleteNotify(notify);
						refresh();
					} else {
						notify.setRead(true);
						api.markNotifyIsread(notify);
						refresh();
					}
					break;
				case JoinGroupRequest:
					if (notify.isRead()) {
						api.deleteNotify(notify);
						refresh();
					} else {
						if (index == 0) {
							notify.setRead(true);
							api.markNotifyIsread(notify);
							refresh();
							api.replyJoinGroup((GotyeUser) notify.getSender(),
									(GotyeGroup) notify.getFrom(), "欢迎加入", true);
						} else {
							notify.setRead(true);
							api.markNotifyIsread(notify);
							refresh();
							api.replyJoinGroup((GotyeUser) notify.getSender(),
									(GotyeGroup) notify.getFrom(), "不同意", false);
						}
					}
				}
			}
		});
	}

	private void loadData() {
		notifies = api.getNotifyList();
		if (notifies != null) {
			if (adapter == null) {
				adapter = new NotifyListAdapter(this, notifies);
				listView.setAdapter(adapter);
			} else {
				adapter.refreshData(notifies);
			}
		} else if (adapter != null) {
			adapter.clear();
		}

	}

	public void back(View view) {
		finish();
	}

	public void refresh() {
		loadData();
	}

	@Override
	protected void onDestroy() {
		api.removeListener(this);
		super.onDestroy();
	}

	@Override
	public void onReceiveNotify(int code, GotyeNotify notify) {
		loadData();
	}

	@Override
	public void onJoinGroup(int code, GotyeGroup group) {
		ProgressDialogUtil.dismiss();
		if (code == GotyeStatusCode.CODE_OK) {
			if (notifies != null) {
				for (GotyeNotify notify : notifies) {
					if (notify.getFrom().getId() == group.getGroupID()) {
						notify.setRead(true);
						ToastUtil.show(this, "成功加入该群");
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						return;
					}
				}
			}
		} else {
			ToastUtil.show(this, "加群失败");
		}
	}

	@Override
	public void onNotifyStateChanged() {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public void onAddBlocked(int code, GotyeUser user) {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public void onRemoveBlocked(int code, GotyeUser user) {
		// TODO Auto-generated method stub
		loadData();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
