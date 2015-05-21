package com.angelhack.vidaloca;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Tejas on 5/14/2015.
 */
class VidaVideo {
    String mTitle;
    String mDescription;
    String mDuration;
    public ArrayList<String> mTags;


    String videoPath;
    Bitmap photoId;
    int videoID;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getVideoID() {
        return videoID;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }



    public Bitmap getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Bitmap photoId) {
        this.photoId = photoId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }



    VidaVideo(){
        mTags = new ArrayList<>();
    }

    VidaVideo(String title, String desc,String duration, Bitmap photoId) {
        mTags = new ArrayList<>();
        this.mTitle = title;
        this.mDescription = desc;
        this.mDuration = duration;
        this.photoId = photoId;
    }

}