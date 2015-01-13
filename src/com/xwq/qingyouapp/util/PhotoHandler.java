package com.xwq.qingyouapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.xwq.qingyouapp.R;

public class PhotoHandler {

	private Context context;
	private String localBase;
	private String lacalAlbum;
	private String localAlbumThubmnail;
	private String localHeadPortrait;
	private String photoBase;

	public static enum ImageType {
		Album, AlbumThumbnail, Headportrait
	}

	public PhotoHandler(Context context) {
		this.context = context;
		localBase = context.getResources().getString(R.string.local_base);
		lacalAlbum = context.getResources().getString(R.string.local_album);
		localAlbumThubmnail = context.getResources().getString(R.string.local_album_thumbnail);
		localHeadPortrait = context.getResources().getString(R.string.local_headportrait);
		photoBase = context.getResources().getString(R.string.photo_base);
	}

	public ArrayList<Bitmap> getLocalBitmap(String url) {
		// 检测目录
		File temp = new File(url);
		if (!temp.exists()) {
			temp.mkdirs();
			return null;
		}

		ArrayList<Bitmap> list = new ArrayList<Bitmap>();
		File album = new File(url);
		File[] files = album.listFiles();

		if (files != null) {
			for (File file : files) {
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				list.add(bitmap);
			}
		}
		return list;
	}

	public boolean isExisted(String url, String photoName) {
		// 检测目录
		File temp = new File(url);
		if (!temp.exists()) {
			temp.mkdirs();
			return false;
		}
		File album = new File(url);
		File[] files = album.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (photoName.equals(file.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public HashSet<String> getLocalBitmapPaths(String url) {

		HashSet<String> list = new HashSet<String>();
		File album = new File(url);
		File[] files = album.listFiles();
		if (files != null) {
			for (File file : files) {
				list.add(file.getAbsolutePath());
			}
		}
		return list;
	}

	public String[] getLocalBitmapPathsArr(String url) {

		String[] list = null;
		File album = new File(url);
		File[] files = album.listFiles();

		if (files != null) {
			list = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				list[i] = files[i].getAbsolutePath();
			}
		}
		return list;
	}

	public String getLocalBitmapNames(String url) {

		String str = "";
		File album = new File(url);
		File[] files = album.listFiles();

		if (files != null) {
			for (File file : files) {
				if (str.equals("")) {
					str = file.getName();
				} else
					str += "," + file.getName();
			}
		}
		return str;
	}

	public void saveBitmapToLocal(Bitmap bitmap, Integer userId, ImageType type) {
		String fileDir = getLocalAbsolutePath(userId, type);
		// 检测目录
		File temp = new File(fileDir);
		if (!temp.exists()) {
			temp.mkdirs();
		}
		// 存储文件
		File file = new File(fileDir + userId + "_" + Calendar.getInstance().getTimeInMillis()
				+ ".png");
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLocalAbsolutePath(Integer userId, ImageType type) {
		String fileName = "";
		switch (type) {
		case Album:
			fileName = lacalAlbum;
			break;
		case AlbumThumbnail:
			fileName = localAlbumThubmnail;
			break;
		case Headportrait:
			fileName = localHeadPortrait;
			break;
		}
		return localBase + userId + "/" + fileName;
	}

	public String getServerPath(Integer userId) {
		return photoBase + (userId / 1000) + "/" + userId + "/";
	}

	public HashSet<String> getNewPhotosPath(HashSet<String> old, HashSet<String> now) {
		HashSet<String> newSet = new HashSet<String>();
		for (String str : now) {
			if (!old.contains(str))
				newSet.add(str);
		}
		return newSet;
	}

	public void deleteLocalBitmap(String url, int position) {
		File album = new File(url);
		File[] files = album.listFiles();
		files[position].delete();
	}

}
