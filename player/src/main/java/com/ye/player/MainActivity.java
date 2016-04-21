package com.ye.player;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

public class MainActivity extends Activity {
	// public static List<VideoInfo> sysVideoList = null;// 视频信息集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// sysVideoList = new ArrayList<VideoInfo>();
		setVideoList();
	}

	private void setVideoList() {
		// MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
		String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID };

		// MediaStore.Video.Media.DATA：视频文件路径；
		// MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
		// MediaStore.Video.Media.TITLE: 视频标题 : testVideo
		String[] mediaColumns = { MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
				MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE, MediaStore.Video.Media.DISPLAY_NAME };

		Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

		if (cursor == null) {
			Toast.makeText(MainActivity.this, "没有找到可播放视频文件", Toast.LENGTH_LONG).show();
			return;
		}
		if (cursor.moveToFirst()) {
			do {
				// VideoInfo info = new VideoInfo();
				int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
				Cursor thumbCursor = managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns,
						MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null);
				if (thumbCursor.moveToFirst()) {
					/*
					 * info.setThumbPath(thumbCursor.getString(thumbCursor
					 * .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
					 */
					String path = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
				}
				/*
				 * info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(
				 * MediaStore.Video.Media.DATA)));
				 * info.setTitle(cursor.getString
				 * (cursor.getColumnIndexOrThrow(MediaStore
				 * .Video.Media.TITLE)));
				 * 
				 * info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow
				 * (MediaStore.Video.Media.DISPLAY_NAME))); LogUtil.log(TAG,
				 * "DisplayName:" + info.getDisplayName());
				 * info.setMimeType(cursor
				 * .getString(cursor.getColumnIndexOrThrow
				 * (MediaStore.Video.Media.MIME_TYPE)));
				 * 
				 * sysVideoList.add(info);
				 */
			} while (cursor.moveToNext());
		}
	}
}
