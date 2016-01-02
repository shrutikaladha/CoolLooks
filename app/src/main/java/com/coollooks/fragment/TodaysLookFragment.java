package com.coollooks.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.coollooks.R;
import com.coollooks.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Shrutika on 12/26/2015.
 */
public class TodaysLookFragment extends Fragment {

    ImageView ivTshirt, ivJeans, ivLike, ivDislike, ivShare;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    String tShirtPath, jeansPath;
    String mPath;
    private final String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/CoolLooks/SCREENCSHOTS/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.todays_look_fragment, container, false);
        ivTshirt = (ImageView) view.findViewById(R.id.ivTshirt);
        ivJeans = (ImageView) view.findViewById(R.id.ivJeans);
        ivLike = (ImageView) view.findViewById(R.id.ivLike);
        //ivLike = (ImageView) view.findViewById(R.id.ivLike);
        ivDislike = (ImageView) view.findViewById(R.id.ivDislike);
        ivShare = (ImageView) view.findViewById(R.id.ivShare);
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

        requestNewImage();
        
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Util.likeMap.containsKey((tShirtPath + jeansPath).hashCode())) {
                    Util.likeMap.put((tShirtPath + jeansPath).hashCode(), tShirtPath + jeansPath);
                    takeScreenshot();
                    Util.setBookmarkList();
                } else {
                    Toast.makeText(getActivity(),"Already in favorites",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.dislikeMap.put((10 * Util.tShirtPathList.indexOf(tShirtPath) + Util.jeansPathList.indexOf(jeansPath)), tShirtPath + jeansPath);
                requestNewImage();
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });
        return view;
    }

    private void requestNewImage() {
        tShirtPath = Util.getTodaysTshirt();
        jeansPath = Util.getTodaysJeans();
        boolean set = true;
        if (Util.dislikeMap.containsKey(10 * Util.tShirtPathList.indexOf(tShirtPath) + Util.jeansPathList.indexOf(jeansPath))){
            if(Util.tShirtPathList.size() * Util.jeansPathList.size() == Util.dislikeMap.size()) {
                Toast.makeText(getActivity(), "Time for shopping", Toast.LENGTH_SHORT).show();
                set = false;
            } else
                requestNewImage();
        }
        if(set == true) {
            setImage();
        }
    }

    private void setImage() {
        imageLoader.displayImage("file://" + tShirtPath, ivTshirt, options, null);
        imageLoader.displayImage("file://" + jeansPath, ivJeans, options, null);
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            mPath = folderPath + "/" + now + ".jpg";
            File f = new File(folderPath);
            if (!f.exists())
                f.mkdir();
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void shareIt() {
        takeScreenshot();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse("file://"+mPath);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

}

