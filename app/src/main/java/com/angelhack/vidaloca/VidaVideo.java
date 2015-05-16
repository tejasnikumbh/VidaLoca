package com.angelhack.vidaloca;

/**
 * Created by Tejas on 5/14/2015.
 */
class VidaVideo {
    String mTitle;
    String mDescription;
    String mDuration;
    int photoId;

    VidaVideo(String title, String desc,String duration, int photoId) {
        this.mTitle = title;
        this.mDescription = desc;
        this.mDuration = duration;
        this.photoId = photoId;
    }

}