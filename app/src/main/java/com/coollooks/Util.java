package com.coollooks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Shrutika on 12/27/2015.
 */
public class Util {
    public static ArrayList<String> tShirtPathList;
    public static ArrayList<String> jeansPathList;
    private static ArrayList<String> bookmarkPathList;
    private static final String tshirtFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CoolLooks/TSHIRTS/" ;
    private static final String jeansFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CoolLooks/JEANS/";
    private static final String bookmarkFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CoolLooks/SCREENCSHOTS/";
    public static HashMap<Integer, String> dislikeMap = new HashMap<Integer, String>();
    public static final HashMap<Integer, String> likeMap = new HashMap<Integer, String>();

    public static String  getTshirtListPath() {
        return tshirtFolderPath;
    }

    public static String  getJeansListPath() {
        return jeansFolderPath;
    }

    public static ArrayList<String> getTshirtList() {
        return tShirtPathList;
    }

    public static void setTshirtList() {
        File[] tShirtsFileList;
        File f = new File(tshirtFolderPath);
        if(!f.exists())
            f.mkdir();
        tShirtPathList = new ArrayList<String>();
        tShirtsFileList = f.listFiles();
        if(tShirtsFileList != null) {
            for (int i = 0; i < tShirtsFileList.length; i++) {
                tShirtPathList.add(tShirtsFileList[i].getPath());
            }
        }
    }

    public static ArrayList<String> getJeansList() {
        return jeansPathList;
    }

    public static void setJeansList() {
        File[] jeansFileList;
        File f = new File(jeansFolderPath);
        if(!f.exists())
            f.mkdir();
        jeansFileList = f.listFiles();
        jeansPathList = new ArrayList<String>();
        if(jeansFileList != null) {
            for (int i = 0; i < jeansFileList.length; i++) {
                jeansPathList.add(jeansFileList[i].getPath());
            }
        }
    }

    public static ArrayList<String> getBookmarkList() {
        if(bookmarkPathList == null)
            setBookmarkList();
        return bookmarkPathList;
    }

    public static void setBookmarkList() {
        File[] bookmarkFileList;
        File f = new File(bookmarkFolderPath);
        bookmarkFileList = f.listFiles();
        bookmarkPathList = new ArrayList<String>();
        if(bookmarkFileList != null) {
            for (int i = 0; i < bookmarkFileList.length; i++) {
                bookmarkPathList.add(bookmarkFileList[i].getPath());
            }
        }
    }

    public static ArrayList<String> getDislikeList() {
        return null;
    }

    public static ArrayList<String> setDislikeList() {
        return null;
    }

    public static String getTodaysTshirt() {
        if(tShirtPathList == null)
            setTshirtList();
        int i = (int) (Math.random()*33 % (tShirtPathList.size()));
        if(tShirtPathList.size() > 0)
            return (tShirtPathList.get(i));
        else
            return null;
    }

    public static String getTodaysJeans() {
        if(jeansPathList == null)
            setJeansList();
        int i = (int) (Math.random()*68 % (jeansPathList.size()));
        if(jeansPathList.size() > 0)
            return (jeansPathList.get(i));
        else
            return null;
    }

}
