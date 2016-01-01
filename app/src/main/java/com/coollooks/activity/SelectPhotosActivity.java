package com.coollooks.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.coollooks.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Shrutika on 12/26/2015.
 */
public class SelectPhotosActivity extends BaseActivity {

    private final String DIRECTORY = "CoolLooks";
    private final String IMAGE_PATH = "path";
    private final String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private final int CAPTURE_IMAGE = 1;

    private ArrayList<String> deviceUrlList = new ArrayList<String>();
    private ArrayList<String> selectedUrlList = new ArrayList<String>();
    private ArrayList<Integer> checkImage = new ArrayList<Integer>();
    private SparseBooleanArray mSparseBooleanArray = new SparseBooleanArray();

    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;

    Uri imageUri;
    String currentPhotoPath = "";
    String dressType;
    GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_photos_layout);
        Button selectBtn = (Button) findViewById(R.id.selectBtn);
        Button captureBtn = (Button) findViewById(R.id.captureBtn);
        gridView = (GridView) findViewById(R.id.gridView);

        if (getIntent() != null)
            dressType = getIntent().getStringExtra("DRESS_TYPE");

        selectBtn.setOnClickListener(selectOnClickListener);
        captureBtn.setOnClickListener(captureOnClickListener);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        loadImagesFromDevice();
    }

    private void loadImagesFromDevice() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        @SuppressWarnings("deprecation")
        Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            deviceUrlList.add(imagecursor.getString(dataColumnIndex));
        }
        options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();

        imageAdapter = new ImageAdapter(this, deviceUrlList);
        gridView.setAdapter(imageAdapter);
    }


    public void btnChoosePhotosClick(View v) {
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
        Toast.makeText(SelectPhotosActivity.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
    }

    public class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mList = new ArrayList<String>();
            this.mList = imageList;
        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }
            return mTempArry;
        }

        @Override
        public int getCount() {
            return deviceUrlList.size();
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

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.grid_item, null);
            }

            final CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageLoader.displayImage("file://" + deviceUrlList.get(position), imageView, options, new ImageLoadingListener() {

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

            mCheckBox.setTag(position);
            if (selectedUrlList.contains(deviceUrlList.get(position))) {
                mCheckBox.setChecked(true);
                checkImage.add(position);
            }
            if (!checkImage.contains(position)) {
                mCheckBox.setChecked(false);
            }
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = deviceUrlList.get(position);
                    if (mCheckBox.isChecked()) {
                        selectedUrlList.add(url);
                    } else {
                        selectedUrlList.remove(url);
                    }
                }
            });
            return convertView;
        }

    }

    View.OnClickListener selectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (selectedUrlList.size() == 0) {
                Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Added " + selectedUrlList.size() + " photos", Toast.LENGTH_LONG).show();
                try {
                    addImagesToFolder();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        }
    };

    private void addImagesToFolder() throws IOException {
        ArrayList<String> originalPathList =  new ArrayList<String>();
        ArrayList<String> targetPathList =  new ArrayList<String>();
        String filename;
        String targetPath = ExternalStorageDirectoryPath + "/" + DIRECTORY + "/" + dressType + "/";
        File f = new File(targetPath);
        if(!f.exists())
            f.mkdirs();
        for (int i = 0; i < selectedUrlList.size(); i++) {
            originalPathList.add(selectedUrlList.get(i));
            filename = originalPathList.get(i).substring(originalPathList.get(i).lastIndexOf("/") + 1);
            targetPathList.add(targetPath + filename + "/");

        }
        copyFile(originalPathList,targetPathList);
    }

    public void copyFile(ArrayList<String> originalPathList, ArrayList<String> targetPathList) throws IOException {
        String selectedImagePath, string;
        for(int i=0;i<originalPathList.size();i++) {
            selectedImagePath = originalPathList.get(i);
            string = targetPathList.get(i);

            InputStream in = new FileInputStream(selectedImagePath);
            OutputStream out = new FileOutputStream(string);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }


    View.OnClickListener captureOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
                File myDirectory = new File(Environment.getExternalStorageDirectory() + "/" + DIRECTORY + "/" + dressType + "/");
                if (!myDirectory.exists())
                    myDirectory.mkdirs();
                File file = new File(myDirectory, fileName);
                imageUri = Uri.fromFile(file);
                currentPhotoPath = file.getAbsolutePath();
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Scanned = ", "Scanned " + path);
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        loadImagesFromDevice();
                                    }
                                });
                            }
                        });
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Picture could not be taken.", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(IMAGE_PATH, currentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPhotoPath = savedInstanceState.getString(IMAGE_PATH);
    }

    @Override
    protected void onStop() {
        imageLoader.stop();
        super.onStop();
    }

}