package com.xwq.qingyouapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
	private String localHeadPortrait;
	private String photoBase;
	private ImageLoader imageLoader;

	public static enum ImageType {
		Album, AlbumThumbnail, Headportrait
	}

	public PhotoHandler(Context context) {
		localBase = context.getResources().getString(R.string.local_base);
		lacalAlbum = context.getResources().getString(R.string.local_album);
		localAlbumThubmnail = context.getResources().getString(R.string.local_album_thumbnail);
		localHeadPortrait = context.getResources().getString(R.string.local_headportrait);
		photoBase = context.getResources().getString(R.string.photo_base);
		imageLoader = ThisApp.imageLoader;
	}

	public void checkFolder(String url) {
		File temp = new File(url);
		if (!temp.exists()) {
			temp.mkdirs();
		}
	}

	public ArrayList<Bitmap> getLocalBitmap(String url) {
		checkFolder(url);

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

	public Bitmap getUserHeadPortrait(int userId) {
		Bitmap bitmap = null;
		String url = getLocalAbsolutePath(userId, ImageType.AlbumThumbnail);
		checkFolder(url);

		File album = new File(url);
		File[] files = album.listFiles();

		if (files != null) {
			bitmap = BitmapFactory.decodeFile(files[0].getAbsolutePath());
		}
		return bitmap;
	}

	/**
	 * 下载图片时默认下载到Album和AlbumThumbnail两个文件夹
	 * 
	 * @param url
	 * @param userId
	 */
	public void downloadImageFromServer(final int userId, final String iamgeName,
			final boolean isHeadPic) {
		String url = getServerPath(userId) + iamgeName;
		imageLoader.loadImage(url, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// 保存原图
				saveBitmapToLocal(loadedImage, iamgeName, userId, ImageType.Album);
				// 保存缩略图
				saveBitmapToLocal(ThumbnailUtils.extractThumbnail(loadedImage, 240, 240),
						iamgeName, userId, ImageType.AlbumThumbnail);
				if (isHeadPic) {
					// 保存缩略图为头像
					saveBitmapToLocal(ThumbnailUtils.extractThumbnail(loadedImage, 240, 240),
							iamgeName, userId, ImageType.Headportrait);
				}
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

	public String[] getLocalBitmapPathsArr(String url) {
		checkFolder(url);

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

	public void saveBitmapToLocal(Bitmap bitmap, String imageName, Integer userId, ImageType type) {
		String url = getLocalAbsolutePath(userId, type);
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

	public String getLocalAbsolutePath(Integer userId, ImageType type) {
		String str = "";
		switch (type) {
		case Album:
			str = lacalAlbum;
			break;
		case AlbumThumbnail:
			str = localAlbumThubmnail;
			break;
		case Headportrait:
			str = localHeadPortrait;
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

	public void deleteLocalBitmap(String url, int position) {
		checkFolder(url);

		File album = new File(url);
		File[] files = album.listFiles();
		if (files.length > position)
			files[position].delete();
	}

}
