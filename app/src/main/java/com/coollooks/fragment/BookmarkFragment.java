package com.coollooks.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.coollooks.R;
import com.coollooks.Util;
import com.coollooks.adapter.ImageAdapter;


import java.util.ArrayList;

/**
 * Created by Shrutika on 12/26/2015.
 */
public class BookmarkFragment extends Fragment {

    private ImageAdapter imageAdapter;
    private ArrayList<String> bookmarkPathList = new ArrayList<String>();
    GridView grid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_fragment, container, false);
        grid = (GridView)view.findViewById(R.id.grid);
        showBookmarks();
        return view;
    }

    private void showBookmarks() {
        bookmarkPathList = Util.getBookmarkList();
        imageAdapter = new ImageAdapter(getActivity(),bookmarkPathList, "BOOKMARK");
        grid.setAdapter(imageAdapter);
    }

}
