package com.xwq.qingyouapp.chat.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageType;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.gotye.api.PathUtil;
import com.gotye.api.WhineMode;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechRecognizer;
import com.xwq.qingyouapp.R;
import com.xwq.qingyouapp.chat.adapter.ChatMessageAdapter;
import com.xwq.qingyouapp.chat.adapter.EmotiAdapter;
import com.xwq.qingyouapp.chat.base.BaseActivity;
import com.xwq.qingyouapp.chat.main.ChatMainActivity;
import com.xwq.qingyouapp.chat.util.AnimUtil;
import com.xwq.qingyouapp.chat.util.CommonUtils;
import com.xwq.qingyouapp.chat.util.GotyeVoicePlayClickListener;
import com.xwq.qingyouapp.chat.util.GotyeVoicePlayClickPlayListener;
import com.xwq.qingyouapp.chat.util.GraphicsUtil;
import com.xwq.qingyouapp.chat.util.ProgressDialogUtil;
import com.xwq.qingyouapp.chat.util.SendImageMessageTask;
import com.xwq.qingyouapp.chat.util.SmileyUtil;
import com.xwq.qingyouapp.chat.util.ToastUtil;
import com.xwq.qingyouapp.chat.util.URIUtil;
import com.xwq.qingyouapp.chat.util.ViewHelper;
import com.xwq.qingyouapp.chat.util.VoiceToTextUtil;
import com.xwq.qingyouapp.chat.view.RTPullListView;
import com.xwq.qingyouapp.chat.view.RTPullListView.OnRefreshListener;

public class ChatPage extends BaseActivity implements OnClickListener {
	public static final int REALTIMEFROM_OTHER = 2;
	public static final int REALTIMEFROM_SELF = 1;
	public static final int REALTIMEFROM_NO = 0;
	private static final int REQUEST_PIC = 1;
	private static final int REQUEST_CAMERA = 2;
	private static final int TEXT_MAX_LEN = 150;
	public static final int VOICE_MAX_TIME = 60 * 1000;
	private RTPullListView pullListView;
	public ChatMessageAdapter chatMessageAdapter;
	private GotyeUser o_user,user;
	private GotyeRoom o_room,room;
	private GotyeGroup o_group,group;
	private GotyeUser currentLoginUser;
	private ImageButton voice_text_chage;
	private TextView pressToVoice;
	private EditText textMessage;
	private ImageButton showMoreType;
	private TextView sendMessage;
	private Handler handler = new Handler();
	private GridView emoti_list;
	private View textModelArea;
	private PopupWindow pannelWindow;
	private PopupWindow menuWindow;
	private AnimationDrawable anim;
	public int chatType = 0;
	public static final int TEXT = 0;
	public static final int VOICE = 1;

	private int inputMode = TEXT;
	private View realTalkView;
	private TextView realTalkName, stopRealTalk;
	private AnimationDrawable realTimeAnim;
	//	private boolean moreTypeForSend = true;

	public int onRealTimeTalkFrom = -1; // -1默认状态 ,0表示我在说话,1表示别人在实时语音

	private File cameraFile;
	public static final int IMAGE_MAX_SIZE_LIMIT = 1024 * 1024;
	public static final int Voice_MAX_TIME_LIMIT = 60 * 1000;
	private long playingId;
	private TextView title;
	private SpeechRecognizer mSpeech;
	public InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(ChatPage.this, "讯飞初始化失败，状态码：" + code,
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_gotye_activity_chat);
		mSpeech = SpeechRecognizer.createRecognizer(this, mInitListener);
		currentLoginUser = api.getCurrentLoginUser();
		api.addListener(this);
		o_user=user = (GotyeUser) getIntent().getSerializableExtra("user");
		o_room=room = (GotyeRoom) getIntent().getSerializableExtra("room");
		o_group=group = (GotyeGroup) getIntent().getSerializableExtra("group");
		initView();
		if (chatType == 0) {
			api.activeSession(user);
			loadData();
		} else if (chatType == 1) {
			int code = api.enterRoom(room);
			if (code == GotyeStatusCode.CODE_OK) {
				api.activeSession(room);
				loadData();
				api.getLocalMessages(room, true);
				GotyeRoom temp = api.requestRoomInfo(room.getId(), true);
				if (temp != null && !TextUtils.isEmpty(temp.getRoomName())) {
					title.setText("聊天室：" + temp.getRoomName());
				}
			} else {
				ProgressDialogUtil.showProgress(this, "正在进入房间...");
			}
		} else if (chatType == 2) {
			api.activeSession(group);
			loadData();
		}
		SharedPreferences spf=getSharedPreferences("fifter_cfg", Context.MODE_PRIVATE);
		boolean fifter=spf.getBoolean("fifter", false);
		api.enableTextFilter(GotyeChatTargetType.values()[chatType], fifter);
		int state=api.getOnLineState();
		if(state!=1){
			setErrorTip(0);
		}else{
			setErrorTip(1);
		}
		emoti_list = (GridView) findViewById(R.id.emoti_list);
		emoti_list.setAdapter(new EmotiAdapter(this, getResources()));
		hideEmotiView();
		emoti_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				int index = textMessage.getSelectionStart();
				Editable edit = textMessage.getEditableText();
				String face="[s"+(i+1)+"]";

				if(edit.length() + face.length() > TEXT_MAX_LEN){
					return;
				}

				edit.insert(index,face);//光标所在位置插入文字

				textMessage.setText(SmileyUtil.replace(ChatPage.this, getResources(), edit));
				textMessage.setSelection(index+face.length());
			}
		});
	}
	private void hideEmotiView(){
		emoti_list.setVisibility(View.GONE);
	}

	private void showEmotiView(){
		emoti_list.setVisibility(View.VISIBLE);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				scrollToBottom();
			}
		}, 400);
		textMessage.requestFocus();
	}
	private boolean isEmotiShow(){
		return  emoti_list.getVisibility() == View.VISIBLE;
	}
	private void initView() {
		pullListView = (RTPullListView) findViewById(R.id.gotye_msg_listview);
		findViewById(R.id.back).setOnClickListener(this);
		title = ((TextView) findViewById(R.id.title));
		realTalkView = findViewById(R.id.real_time_talk_layout);
		realTalkName = (TextView) realTalkView
				.findViewById(R.id.real_talk_name);
		Drawable[] anim = realTalkName.getCompoundDrawables();
		realTimeAnim = (AnimationDrawable) anim[2];
		stopRealTalk = (TextView) realTalkView
				.findViewById(R.id.stop_real_talk);

		textModelArea = findViewById(R.id.gotye_text_area);
		stopRealTalk.setOnClickListener(this);

		if (user != null) {
			chatType = 0;
			title.setText("和 " + user.getName() + " 聊天");
		} else if (room != null) {
			chatType = 1;
			title.setText("聊天室：" + room.getRoomID());
		} else if (group != null) {
			chatType = 2;
			String titleText = null;
			if (!TextUtils.isEmpty(group.getGroupName())) {
				titleText = group.getGroupName();
			} else {
				GotyeGroup temp = api
						.requestGroupInfo(group.getGroupID(), true);
				if (temp != null && !TextUtils.isEmpty(temp.getGroupName())) {
					titleText = temp.getGroupName();
				} else {
					titleText = String.valueOf(group.getGroupID());
				}
			}
			title.setText("群：" + titleText);
		}

		voice_text_chage = (ImageButton) findViewById(R.id.send_voice);
		pressToVoice = (TextView) findViewById(R.id.press_to_voice_chat);
		pressToVoice.setText(R.string.gotye_record_press);
		pressToVoice.setTextColor(getResources().getColor(R.color.gotye_voice_send_pressed));
		textMessage = (EditText) findViewById(R.id.text_msg_input);
		showMoreType = (ImageButton) findViewById(R.id.more_type);
		sendMessage = (TextView) findViewById(R.id.send_message);

		voice_text_chage.setOnClickListener(this);
		showMoreType.setOnClickListener(this);
		sendMessage.setOnClickListener(this);
		textMessage.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				String text = arg0.getText().toString();
				// GotyeMessage message =new GotyeMessage();
				// GotyeChatManager.getInstance().sendMessage(message);
				sendTextMessage(text);
				textMessage.setText("");
				return true;
			}
		});

		textMessage.addTextChangedListener(new TextWatcher() {   

			int line;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(line != textMessage.getLineCount()){
					scrollToBottom();
				}
				line = textMessage.getLineCount();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		textMessage.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(150)});  


		textMessage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				hideEmotiView();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						scrollToBottom();
					}
				}, 400);

				return false;
			}
		});
		pressToVoice.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (GotyeVoicePlayClickListener.isPlaying) {
						GotyeVoicePlayClickListener.currentPlayListener
						.stopPlayVoice();
					}
					pressVoice();
					break;
				case MotionEvent.ACTION_UP:
					if (onRealTimeTalkFrom == 0) {
						return false;
					}
					upVoice();
					break;
				case MotionEvent.ACTION_CANCEL:
					if (onRealTimeTalkFrom == 0) {
						return false;
					}
					Log.d("chat_page",
							"onTouch action=ACTION_CANCEL" + event.getAction());
					upVoice();
					break;
				default:
					break;
				}

				return true;
				//				switch (event.getAction()) {
				//				case MotionEvent.ACTION_DOWN:
				//					if (onRealTimeTalkFrom == 0) {
				//						ToastUtil.show(ChatPage.this, "正在实时通话中...");
				//						return false;
				//					}
				//
				//					if (GotyeVoicePlayClickListener.isPlaying) {
				//						GotyeVoicePlayClickListener.currentPlayListener
				//						.stopPlayVoice();
				//					}
				//					int code = 0;
				//					if (chatType == 0) {
				//						pressVoice();
				//					} else if (chatType == 1) {
				//						code=api.startTalk(room, WhineMode.DEFAULT, false, 60 * 1000);
				//					} else if (chatType == 2) {
				//						code=api.startTalk(group, WhineMode.DEFAULT, 60 * 1000);
				//					}
				//					break;
				//				case MotionEvent.ACTION_UP:
				//					if (onRealTimeTalkFrom == 0) {
				//						return false;
				//					}
				//					Log.d("chat_page",
				//							"onTouch action=ACTION_UP" + event.getAction());
				//					// if (onRealTimeTalkFrom > 0) {
				//					// return false;
				//					// }
				//					upVoice();
				//					Log.d("chat_page",
				//							"after stopTalk action=" + event.getAction());
				//					break;
				//				case MotionEvent.ACTION_CANCEL:
				//					if (onRealTimeTalkFrom == 0) {
				//						return false;
				//					}
				//					Log.d("chat_page",
				//							"onTouch action=ACTION_CANCEL" + event.getAction());
				//					// if (onRealTimeTalkFrom > 0) {
				//					// return false;
				//					// }
				//					upVoice();
				//					break;
				//				default:
				//					Log.d("chat_page",
				//							"onTouch action=default" + event.getAction());
				//					break;
				//				}
				//				return false;





			}
		});
		chatMessageAdapter = new ChatMessageAdapter(this, new ArrayList<GotyeMessage>());
		pullListView.setAdapter(chatMessageAdapter);
		scrollToBottom();
		setListViewInfo();
	}
	private void pressVoice(){
		pressToVoice.setBackgroundResource(R.drawable.gotye_btn_send_voice_pressed);
		pressToVoice.setText(R.string.gotye_record_up);
		pressToVoice.setTextColor(getResources().getColor(R.color.gotye_text_white));


		showRecordingView();
		int code = 0;
		code = api.startTalk(user, WhineMode.DEFAULT, 60 * 1000);
		if(code==0){
			handler.post(new Runnable() {

				@Override
				public void run() {
					upVoice();
				}
			});
		}
	}

	private void upVoice(){
		hidesRecordingView();
		pressToVoice.setBackgroundResource(R.drawable.gotye_btn_send_voice_normal);
		pressToVoice.setText(R.string.gotye_record_press);
		pressToVoice.setTextColor(getResources().getColor(R.color.gotye_voice_send_pressed));
		api.stopTalk();
	}

	private void showRecordingView() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				View view = LayoutInflater.from(ChatPage.this).inflate(
						R.layout.gotye_audio_recorder_ring, null);

				anim = initRecordingView(view);

				menuWindow = new PopupWindow(ChatPage.this);
				menuWindow.setContentView(view);

				menuWindow.setAnimationStyle(android.R.style.Animation_Dialog);
				// int width = (int) (view.getMeasuredWidth() * 3 * 1.0 / 2);
				Drawable dd = getResources().getDrawable(R.drawable.gotye_pls_talk);
				menuWindow.setWidth(dd.getIntrinsicWidth());

				menuWindow.setHeight(dd.getIntrinsicHeight());
				menuWindow.setBackgroundDrawable(null);
				menuWindow.showAtLocation(findViewById(R.id.gotye_chat_content), Gravity.CENTER, 0, 0);

				handler.post(new Runnable() {

					@Override
					public void run() {
						if (anim != null) {
							anim.start();
						}
					}
				});
			}
		});
	}

	private void hidesRecordingView() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				if (menuWindow != null) {
					menuWindow.dismiss();
				}				
			}
		});

	}
	private AnimationDrawable initRecordingView(View layout) {

		ImageView speakingBg = (ImageView) layout
				.findViewById(R.id.background_image);
		speakingBg.setImageDrawable(getResources().getDrawable(R.drawable.gotye_pop_voice));
		layout.setBackgroundResource(R.drawable.gotye_pls_talk);


		AnimationDrawable anim = AnimUtil.getSpeakBgAnim(getResources());
		anim.selectDrawable(0);

		ImageView dot = (ImageView) layout.findViewById(R.id.speak_tip);
		dot.setBackgroundDrawable(anim);

		return anim;
	}
	private void sendTextMessage(String text) {
		if (!TextUtils.isEmpty(text)) {
			GotyeMessage toSend;
			if (chatType == 0) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser, o_user,
						text);
			} else if (chatType == 1) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser, o_room,
						text);
			} else {
				toSend = GotyeMessage.createTextMessage(currentLoginUser,
						o_group, text);
			}
			String extraStr = null;
			if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}

			} else if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}
			}
			if (extraStr != null) {
				toSend.putExtraData(extraStr.getBytes());
			}

			// int code =
			int code = api.sendMessage(toSend);
			Log.d("", String.valueOf(code));
			chatMessageAdapter.addMsgToBottom(toSend);
			scrollToBottom();
			//sendUserDataMessage("userdata message".getBytes(), "text#text");
		}
	}

	public void sendUserDataMessage(byte[] userData, String text) {
		if (userData != null) {
			GotyeMessage toSend;
			if (chatType == 0) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						user, userData, userData.length);
			} else if (chatType == 1) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						room, userData, userData.length);
			} else {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						group, userData, userData.length);
			}
			String extraStr = null;
			if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}

			} else if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}
			}
			if (extraStr != null) {
				toSend.putExtraData(extraStr.getBytes());
			}

			// int code =
			api.sendMessage(toSend);
			chatMessageAdapter.addMsgToBottom(toSend);
			scrollToBottom();
		}
	}

	public void callBackSendImageMessage(GotyeMessage msg) {
		chatMessageAdapter.addMsgToBottom(msg);
		scrollToBottom();
	}

	public void info(View v) {
		if (chatType == 0) {
			Intent intent = getIntent();
			intent.setClass(getApplication(), UserInfoPage.class);
			intent.putExtra("user", user);
			startActivity(intent);
		} else if (chatType == 1) {
			Intent info = new Intent(getApplication(), RoomInfoPage.class);
			info.putExtra("room", room);
			startActivity(info);
		} else {
			Intent info = new Intent(getApplication(), GroupInfoPage.class);
			info.putExtra("group", group);
			startActivity(info);
		}
	}

	private void loadData() {
		List<GotyeMessage> messages = null;
		if (user != null) {
			messages = api.getLocalMessages(user, true);
		} else if (room != null) {
			messages = api.getLocalMessages(room, true);
		} else if (group != null) {
			messages = api.getLocalMessages(group, true);
		}
		if (messages == null) {
			messages = new ArrayList<GotyeMessage>();
		}
		for (GotyeMessage msg : messages) {
			api.downloadMessage(msg);
		}
		chatMessageAdapter.refreshData(messages);
		scrollToBottom();
	}

	private void setListViewInfo() {
		// 下拉刷新监听器
		pullListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (chatType == 1) {
					api.getLocalMessages(room, true);
				} else {
					List<GotyeMessage> list = null;

					if (chatType == 0) {
						list = api.getLocalMessages(user, true);
					} else if (chatType == 2) {
						list = api.getLocalMessages(group, true);
					}
					if (list != null) {
						for (GotyeMessage msg : list) {
							api.downloadMessage(msg);
						}
						chatMessageAdapter.refreshData(list);
					} else {
						ToastUtil.show(ChatPage.this, "没有更多历史消息");
					}
				}
				chatMessageAdapter.notifyDataSetChanged();
				pullListView.onRefreshComplete();
			}
		});
		pullListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				final GotyeMessage message = chatMessageAdapter.getItem(position - 1);
				pullListView.setTag(message);
				pullListView.showContextMenu();
				return true;
			}
		});

		pullListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				hideEmotiView();
				//				if(isShowTopPannel()){
				//					hideTopPannel();
				//					return true;
				//				}

				return false;
			}
		});

		pullListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu conMenu,
					View arg1, ContextMenuInfo arg2) {
				final GotyeMessage message = (GotyeMessage) pullListView
						.getTag();
				if (message == null) {
					return;
				}
				MenuItem m = null;
				if (chatType == 1
						&& !message.getSender().getName()
						.equals(currentLoginUser.getName())) {
					m = conMenu.add(0, 0, 0, "举报");
				}
				// if(message.getType()==GotyeMessageType.GotyeMessageTypeAudio){
				// m= conMenu.add(0, 1, 0, "转为文字(仅限普通话)");
				// }
				if (m == null) {
					return;
				}
				m.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
						case 0:
							api.report(0, "举报的说明", message);
							break;

						case 1:
							// api.decodeMessage(message);
							break;
						}
						return true;
					}
				});
			}
		});

	}

	public void scrollToBottom() {
		pullListView.setSelection(chatMessageAdapter.getCount() - 1);
	}

	// private void showTalkView() {
	// dismissTalkView();
	// View view = LayoutInflater.from(this).inflate(
	// R.layout.chat_gotye_audio_recorder_ring, null);
	//
	// anim = initRecordingView(view);
	// anim.start();
	// menuWindow = new PopupWindow(this);
	// menuWindow.setContentView(view);
	// menuWindow.setAnimationStyle(android.R.style.Animation_Dialog);
	// // int width = (int) (view.getMeasuredWidth() * 3 * 1.0 / 2);
	// Drawable dd = getResources().getDrawable(R.drawable.chat_gotye_pls_talk);
	// menuWindow.setWidth(dd.getIntrinsicWidth());
	//
	// menuWindow.setHeight(dd.getIntrinsicHeight());
	// menuWindow.setBackgroundDrawable(null);
	// menuWindow.showAtLocation(findViewById(R.id.gotye_chat_content),
	// Gravity.CENTER, 0, 0);
	// }
	//
	// private void dismissTalkView() {
	// if (menuWindow != null && menuWindow.isShowing()) {
	// menuWindow.dismiss();
	// }
	// if (anim != null && anim.isRunning()) {
	// anim.stop();
	// }
	// }
	//
	// private AnimationDrawable initRecordingView(View layout) {
	// ImageView speakingBg = (ImageView) layout
	// .findViewById(R.id.background_image);
	// speakingBg.setImageDrawable(getResources().getDrawable(
	// R.drawable.chat_gotye_pop_voice));
	// layout.setBackgroundResource(R.drawable.chat_gotye_pls_talk);
	//
	// AnimationDrawable anim = AnimUtil.getSpeakBgAnim(getResources());
	// anim.selectDrawable(0);
	//
	// ImageView dot = (ImageView) layout.findViewById(R.id.speak_tip);
	// dot.setBackgroundDrawable(anim);
	// return anim;
	// }

	@Override
	protected void onDestroy() {
		api.removeListener(this);
		if (chatType == 0) {
			api.deactiveSession(o_user);
		} else if (chatType == 1) {
			api.deactiveSession(o_room);
			api.leaveRoom(o_room);
		} else {
			api.deactiveSession(o_group);
		}
		if(pannelWindow != null){
			pannelWindow.dismiss();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(pannelWindow != null){
			pannelWindow.update();
		}
	}

	@Override
	protected void onPause() {
		if (GotyeVoicePlayClickListener.isPlaying
				&& GotyeVoicePlayClickListener.currentPlayListener != null) {
			// 停止语音播放
			GotyeVoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// if (onRealTimeTalkFrom == REALTIMEFROM_SELF) {
		api.stopTalk();
		// return;
		// } else if (onRealTimeTalkFrom == REALTIMEFROM_OTHER) {
		api.stopPlay();
		// }
		if(isEmotiShow()){
			hideEmotiView();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.back:
			if(onRealTimeTalkFrom>=0){
				return;
			}
			onBackPressed();
			break;
		case R.id.send_voice:
			if(inputMode == TEXT){
				toVoice(true);
			}else {
				toText(true);
			}

			break;
		case R.id.more_type:
			showPannel(arg0);
			break;
		case R.id.send_message:
			String str = textMessage.getText().toString();
			sendTextMessage(str);
			textMessage.setText("");
			break;
		case R.id.stop_real_talk:
			// int i =
			api.stopTalk();
			break;
		default:
			break;
		}
	}

	public void showImagePrev(GotyeMessage message) {
		hideKeyboard();
	}

	public void realTimeTalk() {
		if (onRealTimeTalkFrom > 0) {
			Toast.makeText(this, "请稍后...", Toast.LENGTH_SHORT).show();
			return;
		}
		api.startTalk(room, WhineMode.DEFAULT, true, Voice_MAX_TIME_LIMIT);
	}

	public void hideKeyboard() {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 显示或者隐藏输入法
		imm.hideSoftInputFromWindow(textMessage.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void takePic() {
		Intent intent;
		intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/jpeg");
		startActivityForResult(intent, REQUEST_PIC);

		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		// intent.setType("image/*");
		// startActivityForResult(intent, REQUEST_PIC);
	}

	private void takePhoto() {
		selectPicFromCamera();
	}

	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照",
					Toast.LENGTH_SHORT).show();
			return;
		}

		cameraFile = new File(PathUtil.getAppFIlePath()
				+ +System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
						REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 选取图片的返回值
		if (requestCode == REQUEST_PIC) {
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					String path = URIUtil.uriToPath(this, selectedImage);
					sendPicture(path);
				}
			}

		} else if (requestCode == REQUEST_CAMERA) {
			if (resultCode == RESULT_OK) {

				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			}
		}
		// TODO 获取图片失败
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void sendPicture(String path) {
		SendImageMessageTask task;
		if (chatType == 0) {
			task = new SendImageMessageTask(this, user);
		} else if (chatType == 1) {
			task = new SendImageMessageTask(this, room);
		} else {
			task = new SendImageMessageTask(this, group);
		}
		task.execute(path);
	}

	public void setPlayingId(long playingId) {
		this.playingId = playingId;
		chatMessageAdapter.notifyDataSetChanged();
	}

	public long getPlayingId() {
		return playingId;
	}


	private void showPannel(final View v){
		hideKeyboard();
		hideEmotiView();
		if(pannelWindow != null){
			pannelWindow.dismiss();
		}
		View view = LayoutInflater.from(v.getContext()).inflate(R.layout.gotye_pop_chat_pannel, null);
		final PopupWindow window = new PopupWindow(view, ViewHelper.getWidth(view), ViewHelper.getHeight(view));
		window.setOutsideTouchable(true);
		window.setFocusable(true);
		//让pop可以点击外面消失掉  
		window.setBackgroundDrawable(new ColorDrawable(0));  
		window.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
			}
		});
		int lineCount = textMessage.getLineCount();
		if(lineCount == 0 || lineCount == 1){
			window.showAsDropDown(v, 0, GraphicsUtil.dipToPixel(30));
		}else if(lineCount == 2){
			window.showAsDropDown(v, 0, GraphicsUtil.dipToPixel(35));
		}else if(lineCount == 3){
			window.showAsDropDown(v, 0, GraphicsUtil.dipToPixel(40));
		}else if(lineCount == 4){
			window.showAsDropDown(v, 0, GraphicsUtil.dipToPixel(50));
		}

		View faceSelectBtn = view.findViewById(R.id.gotye_pannel_face);
		faceSelectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				if(inputMode != TEXT){
					toText(true);
				}
				showEmotiView();
				hideKeyboard();
			}
		});

		View picSelectBtn = view.findViewById(R.id.gotye_pannel_pic);
		picSelectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				hideKeyboard();
				takePic();
			}
		});

		View cameraSelectBtn = view.findViewById(R.id.gotye_pannel_camera);
		cameraSelectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
				hideKeyboard();
				takePhoto();
			}
		});
		pannelWindow = window;
	}
	private void toText(boolean hasAnim){
		//      hideEmotiView();
		if(textMessage.getLineCount() > 1){
			hasAnim = false;
		}
		if(hasAnim){
			pressToVoice.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gotye_translate_anim_down));
		}
		pressToVoice.setVisibility(View.GONE);

		textModelArea.setVisibility(View.VISIBLE);
		if(hasAnim){
			textModelArea.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gotye_translate_anim_down2));
		}
		if(inputMode == TEXT){
			inputMode = VOICE;
			voice_text_chage.setImageResource(R.drawable.gotye_btn_to_text_selector);
		}else {
			inputMode = TEXT;
			voice_text_chage.setImageResource(R.drawable.gotye_btn_to_voice_selector);
		}
		handler.post(new Runnable() {

			@Override
			public void run() {
				textMessage.requestFocus();		
			}
		});
	}

	private void toVoice(boolean hasAnim){
		hideEmotiView();
		hideKeyboard();

		textMessage.clearFocus();

		if(hasAnim){
			textModelArea.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gotye_translate_anim_up2));
		}
		textModelArea.setVisibility(View.GONE);

		pressToVoice.setVisibility(View.VISIBLE);
		if(hasAnim){
			pressToVoice.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gotye_translate_anim_up));
		}

		if(inputMode == TEXT){
			inputMode = VOICE;
			voice_text_chage.setImageResource(R.drawable.gotye_btn_to_text_selector);
		}else {
			inputMode = TEXT;
			voice_text_chage.setImageResource(R.drawable.gotye_btn_to_voice_selector);
		}
	}

	@Override
	public void onSendMessage(int code, GotyeMessage message) {
		Log.d("OnSend", "code= " + code + "message = " + message);
		// GotyeChatManager.getInstance().insertChatMessage(message);
		chatMessageAdapter.updateMessage(message);
		if (message.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
			// api.decodeMessage(message);
		}
		// message.senderUser =
		// DBManager.getInstance().getUser(currentLoginName);
		if(pannelWindow != null){
			pannelWindow.update();
		}
		scrollToBottom();
	}

	@Override
	public void onReceiveMessage(int code, GotyeMessage message) {
		// GotyeChatManager.getInstance().insertChatMessage(message);
		if (chatType == 0) {
			if (isMyMessage(message)) {
				chatMessageAdapter.addMsgToBottom(message);
				scrollToBottom();
				api.downloadMessage(message);
			}
		} else if (chatType == 1) {
			if (message.getReceiver().getId() == room.getRoomID()) {
				chatMessageAdapter.addMsgToBottom(message);
				scrollToBottom();
				api.downloadMessage(message);

			}
		} else if (chatType == 2) {
			if (message.getReceiver().getId() == group.getGroupID()) {
				chatMessageAdapter.addMsgToBottom(message);
				scrollToBottom();
				api.downloadMessage(message);
			}
		}
		if(pannelWindow != null){
			pannelWindow.update();
		}
		scrollToBottom();
	}

	private boolean isMyMessage(GotyeMessage message) {
		if (message.getSender() != null
				&& user.getName().equals(message.getSender().getName())
				&& currentLoginUser.getName().equals(message.getReceiver().getName())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDownloadMessage(int code, GotyeMessage message) {
		// adapter.downloadDone(message);
		if (message.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
			if (message.getExtraData() == null) {
				api.decodeMessage(message);
			}
		}
	}

	@Override
	public void onEnterRoom(int code, long lastMsgID, GotyeRoom room) {
		ProgressDialogUtil.dismiss();
		if (code == 0) {
			api.activeSession(room);
			loadData();
			GotyeRoom temp = api.requestRoomInfo(room.getId(), true);
			if (temp != null && !TextUtils.isEmpty(temp.getRoomName())) {
				title.setText("聊天室：" + temp.getRoomName());
			}
		} else {
			ToastUtil.show(this, "房间不存在...");
			finish();
		}
	}

	@Override
	public void onGetMessageList(int code, List<GotyeMessage> list) {
		if (chatType == 0) {
			List<GotyeMessage> listmessages = api.getLocalMessages(o_user, false);
			if (listmessages != null) {
				for (GotyeMessage temp : listmessages) {
					api.downloadMessage(temp);
				}
				chatMessageAdapter.refreshData(listmessages);
			} else {
				ToastUtil.show(this, "没有历史记录");
			}
		}else if (chatType == 1){
			List<GotyeMessage> listmessages = api.getLocalMessages(o_room, false);
			if (listmessages != null) {
				for (GotyeMessage temp : listmessages) {
					api.downloadMessage(temp);
				}
				chatMessageAdapter.refreshData(listmessages);
			} else {
				ToastUtil.show(this, "没有历史记录");
			}
		}else if (chatType == 2){
			List<GotyeMessage> listmessages = api.getLocalMessages(o_group, false);
			if (listmessages != null) {
				for (GotyeMessage temp : listmessages) {
					api.downloadMessage(temp);
				}
				chatMessageAdapter.refreshData(listmessages);
			} else {
				ToastUtil.show(this, "没有历史记录");
			}
		}
		chatMessageAdapter.notifyDataSetInvalidated();
		pullListView.onRefreshComplete();
	}
	boolean realTalk,realPlay;
	@Override
	public void onStartTalk(int code, boolean isRealTime, int targetType,
			GotyeChatTarget target) {

		Log.e("player", "onStartTalk:" + isRealTime);
		if (isRealTime) {
			this.realTalk=true;
			if (code != 0) {
				ToastUtil.show(this, "抢麦失败，先听听别人说什么。");
				return;
			}
			if (GotyeVoicePlayClickPlayListener.isPlaying) {
				GotyeVoicePlayClickPlayListener.currentPlayListener.stopPlayVoice(true);
			}
			onRealTimeTalkFrom = 0;
			realTimeAnim.start();
			realTalkView.setVisibility(View.VISIBLE);
			realTalkName.setText("您正在说话..");
			stopRealTalk.setVisibility(View.VISIBLE);
		}
	}

	public List<GotyeMessage> toSend;

	@Override
	public void onStopTalk(int code, GotyeMessage message, boolean isVoiceReal) {
		Log.e("player", "onStopTalk");

		if (isVoiceReal) {
			onRealTimeTalkFrom = -1;
			realTimeAnim.stop();
			realTalkView.setVisibility(View.GONE);
		} else {
			if (code != 0) {
				ToastUtil.show(this, "时间太短...");
				return;
			} else if (message == null) {
				ToastUtil.show(this, "时间太短...");
				return;
			}
			api.decodeMessage(message);
			if (toSend == null) {
				toSend = new ArrayList<GotyeMessage>();
			}
			toSend.add(message);
			// api.sendMessage(message);
			// message.setStatus(GotyeMessage.STATUS_SENDING);
			// adapter.addMsgToBottom(message);
			scrollToBottom();
		}

	}

	@Override
	public void onPlayStart(int code, GotyeMessage message) {
		// TODO Auto-generated method stub
		Log.e("player", "onPlayStart");
	}

	@Override
	public void onPlaying(int code, int position) {
		// TODO Auto-generated method stub
		Log.e("player", "onPlaying");
	}

	@Override
	public void onPlayStop(int code) {
		Log.e("player", "onPlayStop");
		setPlayingId(-1);
		if(this.realPlay){
			onRealTimeTalkFrom = -1;
			realTimeAnim.stop();
			realTalkView.setVisibility(View.GONE);	
		}
		chatMessageAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPlayStartReal(int code, long roomId, String who) {
		if (code == 0 && roomId == this.room.getRoomID()) {
			this.realPlay=true;
			onRealTimeTalkFrom = 1;
			realTalkView.setVisibility(View.VISIBLE);
			realTalkName.setText(who + "正在说话..");
			realTimeAnim.start();
			stopRealTalk.setVisibility(View.GONE);
			if (GotyeVoicePlayClickListener.isPlaying) {
				GotyeVoicePlayClickListener.currentPlayListener.stopPlayVoice();
			}
		}
	}

	@Override
	public void onRequestUserInfo(int code, GotyeUser user) {
		if(chatType==0){
			if(user.getName().equals(this.user.getName())){
				this.user = user;
			}
		}
	}

	@Override
	public void onDownloadMedia(int code, String path, String url) {
		// TODO Auto-generated method stub
		chatMessageAdapter.notifyDataSetChanged();
	}

	@Override
	public void onUserDismissGroup(GotyeGroup group, GotyeUser user) {
		// TODO Auto-generated method stub
		if (this.group != null && group.getGroupID() == this.group.getGroupID()) {
			Intent i = new Intent(this, ChatMainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Toast.makeText(getBaseContext(), "群主解散了该群,会话结束", Toast.LENGTH_SHORT)
			.show();
			finish();
			startActivity(i);
		}
	}

	@Override
	public void onUserKickdFromGroup(GotyeGroup group, GotyeUser kicked,
			GotyeUser actor) {
		// TODO Auto-generated method stub
		if (this.group != null && group.getGroupID() == this.group.getGroupID()) {
			if (kicked.getName().equals(currentLoginUser.getName())) {
				Intent i = new Intent(this, ChatMainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Toast.makeText(getBaseContext(), "您被踢出了群,会话结束",
						Toast.LENGTH_SHORT).show();
				finish();
				startActivity(i);
			}

		}
	}

	@Override
	public void onReport(int code, GotyeMessage message) {
		// TODO Auto-generated method stub
		if (code == GotyeStatusCode.CODE_OK) {
			ToastUtil.show(this, "举报成功");
		} else {
			ToastUtil.show(this, "举报失败");
		}
		super.onReport(code, message);
	}

	@Override
	public void onRequestRoomInfo(int code, GotyeRoom room) {
		// TODO Auto-generated method stub
		if (this.room != null && this.room.getRoomID() == room.getRoomID()) {
			title.setText("聊天室：" + room.getRoomName());
		}
		super.onRequestRoomInfo(code, room);
	}

	@Override
	public void onRequestGroupInfo(int code, GotyeGroup group) {
		if (this.group != null && this.group.getGroupID() == group.getGroupID()) {
			title.setText("聊天室：" + group.getGroupName());
		}
	}

	@Override
	public void onDecodeMessage(int code, GotyeMessage message) {
		// Intent intent = new Intent(ChatPage.this, TextPageVoice.class);
		// intent.putExtra("text_voice", message.getMedia().getPath_ex());
		// startActivity(intent);
		if (code == GotyeStatusCode.CODE_OK) {
			VoiceToTextUtil util = new VoiceToTextUtil(this);
			util.toPress(mSpeech, message);

		} else {
			if (toSend != null) {
				int p = toSend.indexOf(message);
				if (p >= 0 && p < toSend.size()) {
					api.sendMessage(message);
					toSend.remove(p);
				}
			}
		}
		super.onDecodeMessage(code, message);
	}
	@Override
	public void onLogin(int code, GotyeUser currentLoginUser) {
		// TODO Auto-generated method stub
		setErrorTip(1);
	}

	@Override
	public void onLogout(int code) {
		// TODO Auto-generated method stub
		setErrorTip(0);
	}

	@Override
	public void onReconnecting(int code, GotyeUser currentLoginUser) {
		// TODO Auto-generated method stub
		setErrorTip(-1);
	}
	private void setErrorTip(int code) {
		if (code == 1) {
			findViewById(R.id.error_tip).setVisibility(View.GONE);
		} else {
			findViewById(R.id.error_tip).setVisibility(View.VISIBLE);
			if (code == -1) {
				findViewById(R.id.loading)
				.setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.showText))
				.setText("正在连接登陆...");
				findViewById(R.id.error_tip_icon).setVisibility(
						View.GONE);
			} else {
				findViewById(R.id.loading).setVisibility(View.GONE);
				((TextView)  findViewById(R.id.showText))
				.setText("当前未登陆或网络异常");
				findViewById(R.id.error_tip_icon).setVisibility(
						View.VISIBLE);
			}
		}
	}
}
