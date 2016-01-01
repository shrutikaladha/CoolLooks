package com.coollooks.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.coollooks.R;
import com.coollooks.Util;
import com.coollooks.activity.SelectPhotosActivity;
import com.coollooks.adapter.ImageAdapter;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Shrutika on 12/26/2015.
 */
public class WardrobeFragment extends Fragment {

    private final String DRESS_TYPE_TSHIRTS = "TSHIRTS";
    private final String DRESS_TYPE_JEANS = "JEANS";
    private final int UPLOAD = 1;

    GridView gridTshirt, gridJeans;
    private ArrayList<String> tShirtPathList = new ArrayList<String>();
    private ArrayList<String> jeansPathList = new ArrayList<String>();
    private ImageAdapter tShirtAdapter, jeansAdapter;
    private int TSHIRT = 1;
    private int JEANS = 2;
    private int uploadType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wardrobe_fragment, container, false);
        gridTshirt = (GridView) view.findViewById(R.id.grid1);
        gridJeans = (GridView) view.findViewById(R.id.grid2);
        setTshirtsList();
        setJeansList();
        Button addTshirtsBtn = (Button) view.findViewById(R.id.addTshirtsBtn);
        Button addJeansBtn = (Button) view.findViewById(R.id.addJeansBtn);
        addTshirtsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectPhotosActivity.class);
                i.putExtra("DRESS_TYPE", DRESS_TYPE_TSHIRTS);
                uploadType = 1;
                startActivityForResult(i, UPLOAD);
            }
        });

        addJeansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectPhotosActivity.class);
                i.putExtra("DRESS_TYPE", DRESS_TYPE_JEANS);
                uploadType = 2;
                startActivityForResult(i, UPLOAD);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPLOAD) {
            if (resultCode == Activity.RESULT_OK) {
                switch (uploadType) {
                    case 1:
                        scanFile(Util.getTshirtListPath());
                        break;
                    case 2:
                        scanFile(Util.getJeansListPath());
                        break;
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Problem uploading images.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setJeansList() {
        jeansPathList = Util.getJeansList();
        jeansAdapter = new ImageAdapter(getActivity(), jeansPathList, "WARDROBE");
        gridJeans.setAdapter(jeansAdapter);
    }

    private void setTshirtsList() {
        tShirtPathList = Util.getTshirtList();
        tShirtAdapter = new ImageAdapter(getActivity(), tShirtPathList, "WARDROBE");
        gridTshirt.setAdapter(tShirtAdapter);
    }


    private void scanFile(String path) {
        File file = new File(path);
        Uri imageUri = Uri.fromFile(file);
        MediaScannerConnection.scanFile(getActivity(), new String[]{imageUri.getPath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                switch (uploadType) {
                                    case 1:
                                        Util.setTshirtList();
                                        setTshirtsList();
                                        break;
                                    case 2:
                                        Util.setJeansList();
                                        setJeansList();
                                        break;
                                }
                            }
                        });
                    }
                });
    }

}
