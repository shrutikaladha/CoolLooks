package com.coollooks.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.coollooks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Shrutika on 12/31/2015.
 */

public class ImageAdapter extends BaseAdapter {

    private static final String wardrobe = "WARDROBE";
    private static final String bookmark = "BOOKMARK";
    ArrayList<String> mList;
    LayoutInflater mInflater;
    Context mContext;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private String viewType;

    public ImageAdapter(Context context, ArrayList<String> imageList, String viewType) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = new ArrayList<String>();
        this.mList = imageList;
        this.viewType = viewType;
        options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null && viewType.equalsIgnoreCase(wardrobe)) {
            convertView = mInflater.inflate(R.layout.grid_item_default, null);
        } else if (convertView == null && viewType.equalsIgnoreCase(bookmark)) {
            convertView = mInflater.inflate(R.layout.grid_item_big, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageLoader.displayImage("file://" + mList.get(position), imageView, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }

        });

        return convertView;
    }

}
