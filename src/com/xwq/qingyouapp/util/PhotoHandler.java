package com.xwq.qingyouapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.View;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xwq.qingyouapp.R;

public class PhotoHandler {

	private String localBase;
	private String lacalAlbum;
	private String localAlbumThubmnail;
	private String photoBase;
	private ImageLoader imageLoader;

	public static enum ImageType {
		Album, AlbumThumbnail
	}

	public PhotoHandler(Context context) {
		localBase = context.getResources().getString(R.string.local_base);
		lacalAlbum = context.getResources().getString(R.string.local_album);
		localAlbumThubmnail = context.getResources().getString(R.string.local_album_thumbnail);
		photoBase = context.getResources().getString(R.string.photo_base);
		imageLoader = ThisApp.imageLoader;
	}

	public void checkFolder(String url) {
		File temp = new File(url);
		if (!temp.exists()) {
			temp.mkdirs();
		}
	}

	public Bitmap getPhoto(int userId, String photoName, ImageType type) {
		Bitmap bitmap = null;
		String url = getLocalAbsolutePath(userId, type);
		checkFolder(url);

		File album = new File(url);
		File[] files = album.listFiles();

		if (files != null) {
			bitmap = BitmapFactory.decodeFile(url + photoName);
		}
		return bitmap;
	}

	/**
	 * 下载图片时默认下载到Album和AlbumThumbnail两个文件夹
	 * 
	 * @param url
	 * @param userId
	 */
	public void downloadImageFromServer(final int userId, final String imageName) {
		String url = getServerPath(userId) + imageName;
		imageLoader.loadImage(url, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				saveBitmaps(userId, imageName, loadedImage);
				super.onLoadingComplete(imageUri, view, loadedImage);
			}
		});
	}

	public boolean isExisted(String url, String photoName) {
		checkFolder(url);

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
		checkFolder(url);

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

	// 返回有序的图片集合，头像在第一张的位置
	public ArrayList<Bitmap> getLocalBitmaps(Integer userId, String headPicName, ImageType type) {
		String url = getLocalAbsolutePath(userId, type);
		checkFolder(url);

		ArrayList<Bitmap> list = new ArrayList<Bitmap>();
		File album = new File(url);
		String[] names = album.list(null);

		if (names != null) {
			// 初次排序
			Arrays.sort(names);
			// 再次排序
			for (int i = 0; i < names.length; i++) {
				if (names[i].equals(headPicName)) {
					String s = names[i];
					names[i] = names[0];
					names[0] = s;
				}
			}
			// 按顺序取图片
			for (String name : names) {
				Bitmap bitmap = BitmapFactory.decodeFile(url + name);
				list.add(bitmap);
			}
		}
		return list;
	}

	// 返回有序的路径集合，头像在第一张的位置
	public String[] getLocalBitmapPathsArr(Integer userId, String headPicName, ImageType type) {
		String url = getLocalAbsolutePath(userId, type);
		checkFolder(url);

		String[] list = null;
		File album = new File(url);
		String[] names = album.list(null);

		if (names != null) {
			// 初次排序
			Arrays.sort(names);
			// 再次排序
			for (int i = 0; i < names.length; i++) {
				if (names[i].equals(headPicName)) {
					String s = names[i];
					names[i] = names[0];
					names[0] = s;
				}
			}
			// 按顺序取图片
			list = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				list[i] = url + names[i];
			}
		}
		return list;
	}

	public String getLocalBitmapNames(String url) {
		checkFolder(url);

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

	public void saveBitmap(String url, String imageName, Bitmap bitmap) {
		checkFolder(url);
		// 存储文件
		File file = new File(url + imageName);
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

	public void saveBitmaps(Integer userId, String imageName, Bitmap bitmap) {
		String albumUrl = localBase + userId + "/" + lacalAlbum;
		String albumThumbnailUrl = localBase + userId + "/" + localAlbumThubmnail;
		
		saveBitmap(albumUrl, imageName, bitmap);
		saveBitmap(albumThumbnailUrl, imageName, ThumbnailUtils.extractThumbnail(bitmap, 240, 240));
	}

	public String getLocalAbsolutePath(Integer userId, ImageType type) {
		String str = "";
		switch (type) {
		case Album:
			str = lacalAlbum;
			break;
		case AlbumThumbnail:
			str = localAlbumThubmnail;
			break;
		}
		return localBase + userId + "/" + str;
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

	public void clearFolder(String url) {
		File temp = new File(url);
		if (temp.exists()) {
			temp.delete();
		}
	}

}
