package com.ye.player.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.ye.player.KikkApplication;
import com.ye.player.common.utils.FileUtils;
import com.ye.player.common.utils.StringUtil;
import com.ye.player.common.utils.Utils;

import java.io.File;

public class PicassoImageLoader {

    private static Picasso picassoInstance;

    private static Object localObject = new OkHttpClient();

    public static Picasso getInstance() {
        if (picassoInstance == null) {
            synchronized (PicassoImageLoader.class) {
                if (picassoInstance == null) {
                    init(KikkApplication.app);
                }
            }
        }
        return picassoInstance;
    }

    public static void loadImage(String url, ImageView imageView) {
        if (!StringUtil.isEmpty(url)) {
            loadImage(url, imageView, null);
        }
    }

    public static void loadImage(int resourceId, ImageView imageView) {
        getInstance().load(resourceId).into(imageView);
    }

    public static void loadImage(String url, ImageView imageView, int errorResourceId) {
        if (!StringUtil.isEmpty(url)) {
            getInstance().load(url).error(errorResourceId).into(imageView);
        }
    }

    public static void loadImage(File file, ImageView imageView, int errorResourceId) {
            getInstance().load(file).error(errorResourceId).into(imageView);
    }

    public static void loadImage(File file, final ImageView imageView, final String path) {
        getInstance().load(file).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    public static void loadImage(String url, ImageView imageView, int errorResourceId, int defaultId) {
        if (!StringUtil.isEmpty(url)) {
            getInstance().load(url).placeholder(defaultId).error(errorResourceId).into(imageView);
        }
    }

    public static void loadImage(ImageView imageView, String url) {
        if (!StringUtil.isEmpty(url)) {
            loadImage(url, imageView, null);
        }
    }

    public static void loadImage(String url, ImageView imageView, com.squareup.picasso.Callback callback) {
        if (!StringUtil.isEmpty(url)) {
            if (callback != null) {
                getInstance().load(url).into(imageView, callback);
            } else {
                getInstance().load(url).into(imageView);
            }
        }
    }

    public static void init(Context paramContext) {
        File localFile = FileUtils.createDefaultCacheDir(paramContext);
        ((OkHttpClient) localObject).setCache(new Cache(localFile, Utils.calculateDiskCacheSize(localFile)));
        localObject = new OkHttpDownloader((OkHttpClient) localObject);
        picassoInstance = new Picasso.Builder(paramContext).downloader((Downloader) localObject).loggingEnabled(false)
                .build();
    }
}
