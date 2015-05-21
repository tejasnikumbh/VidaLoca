package com.angelhack.vidaloca;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.VideoView;


public class VideoViewActivity extends ActionBarActivity {
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    private Toolbar mToolBar;
    private VideoView mVideoView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        mToolBar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Styling the title
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setTextSize(30);
        mTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pacifico.ttf"));

        mVideoView = (VideoView) findViewById(R.id.video_view);
        String path;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                path = null;
            } else {
                path = extras.getString("mypath");
            }
        } else {
            path = (String) savedInstanceState.getSerializable("mypath");
        }
        Log.i("mypath",path);
        mVideoView.setVideoPath(path);
        mVideoView.start();

    }



}
