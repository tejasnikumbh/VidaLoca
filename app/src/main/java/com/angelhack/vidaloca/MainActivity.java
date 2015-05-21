package com.angelhack.vidaloca;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import static java.lang.Character.toLowerCase;


class Document
{
    String content;
    int offset;
}

class Entity
{
    public String getOriginal_text() {
        return original_text;
    }

    public void setOriginal_text(String original_text) {
        this.original_text = original_text;
    }

    public String getNormalized_text() {
        return normalized_text;
    }

    public void setNormalized_text(String normalized_text) {
        this.normalized_text = normalized_text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNormalized_length() {
        return normalized_length;
    }

    public void setNormalized_length(int normalized_length) {
        this.normalized_length = normalized_length;
    }

    public int getOriginal_length() {
        return original_length;
    }

    public void setOriginal_length(int original_length) {
        this.original_length = original_length;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAdditional_information() {
        return additional_information;
    }

    public void setAdditional_information(String additional_information) {
        this.additional_information = additional_information;
    }

    String original_text;
    String normalized_text;
    String type;
    int normalized_length;
    int original_length;
    double score;
    String additional_information;
}

public class MainActivity extends ActionBarActivity {

    private Toolbar mToolBar;
    private EditText mSearchBar;
    private List<VidaVideo> mVideos;
    private List<VidaVideo> curVideos;
    private RecyclerView rv;


        //mVideos.add(new VidaVideo("Sample Title","Sample Description","Something",videoThumbs.get(0)));
        // Check them against Database

        // Create list of Untranscribed Videos by checking isTranscribed from DB
        // flag of each video

        // For UnTranscribed Videos List
           // Transcribe the Videos using API

           // Get Keywords and Title from API for all videos and create a list of
           // class transcribed Videos. Each transribed video has -
           // Keywords, Titles, isTranscribed, Thumbnail property
           // Store this in the Database

           // Populate the views as described in Dummy process by fetching from Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Styling the title
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setTextSize(30);
        mTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pacifico.ttf"));
        mVideos = new ArrayList<>();// ArrayList for Actual Video files in recycler view
        curVideos = new ArrayList<>();

        // Getting the recycler view and setting its adapter
        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);




        // Craeting the Adapter from the Model
        RVAdapter adapter = new RVAdapter(mVideos);
        // Setting the Adapter for the recycler view
        rv.setAdapter(adapter);


        // Initialize
        initializeData();

        // Populating the Model
        populateDataView();


        //set adapter for search
        setSearchAdapter();

        // Starting analysis in new task
        AnalyzeFilesTask analysisAsyncTask = new AnalyzeFilesTask();
        analysisAsyncTask.execute();

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String path = curVideos.get(position).getVideoPath();
                        Log.i("PATH", "" + position + " " + path);
                        Intent intent = new Intent(MainActivity.this, VideoViewActivity.class);
                        intent.putExtra("mypath", path);
                        startActivity(intent);
                    }
                })
        );

    }


    private void setSearchAdapter() {
        mSearchBar = (EditText) findViewById(R.id.search_text);
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String theStr = s.toString();
                mVideos.clear();
                Log.i("CV",curVideos.size() + " " + theStr);
                for(int i=0;i<curVideos.size();i++){
                    ArrayList<String> tags = curVideos.get(i).mTags;
                    if(does_contain(tags,theStr)){
                        Log.i("THETAG"," : TRUE");
                        mVideos.add(curVideos.get(i));

                    }
                }
                rv.getAdapter().notifyDataSetChanged();
                Log.i("T","text");

                if(theStr.length() == 0){
                    mVideos.clear();
                    for (int i=0;i<curVideos.size();i++){
                        mVideos.add(curVideos.get(i));
                    }
                    rv.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean does_contain(ArrayList<String> alist, String theStr){
        theStr = theStr.trim();
        for(int i=0;i<alist.size();i++){
            String curStrs = alist.get(i);

            String curStr = curStrs.toLowerCase();
            theStr = theStr.toLowerCase();
            if(curStr.contains(theStr)) return true;
        }
        return false;
    }
    private void analyzeNewVideos() {

        Log.i("GOT"," Speaking from async thread");

        //get untranscribed videos in DB (ID, videoPath)
        ArrayList<VidaVideo> untranscribed_videos = getUntranscribedVideos();

        Log.i("GOT",untranscribed_videos.toString() + "");
        //for each new video
        for(int i=0; i<untranscribed_videos.size();i++)
        {
            //get JSON
            String json_data = "{'entities':[{'original_text':'President Barack','normalized_text':'Barack Obama','type':'people_eng','normalized_length':12,'original_length':12,'score':0.3669,'additional_information':{'person_profession':['author','lawyer','politician','professor'],'person_date_of_birth':'4/8/1961','wikidata_id':76,'wikipedia_eng':'http://en.wikipedia.org/wiki/Barack_Obama','image':'https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg'},'components':[]},{'original_text':'Nelson Mandela','normalized_text':'Nelson Mandela','type':'people_eng','normalized_length':14,'original_length':14,'score':0.2763,'additional_information':{'person_profession':['politician'],'person_date_of_birth':'18/7/1918','person_date_of_death':'05/12/2013','wikidata_id':8023,'wikipedia_eng':'http://en.wikipedia.org/wiki/Nelson_Mandela','image':'https://upload.wikimedia.org/wikipedia/commons/1/14/Nelson_Mandela-2008_(edit).jpg'},'components':[]}]}";
            // = getJSONFromVideo(untranscribed_videos.get(i).getVideoPath());

            //get tags from JSON
            ArrayList<String> tags = getTagsFromJSON(json_data);
            Log.i("GOT", "Final Tags" + tags.toString());

            //insert tags into DB
            insertTitleIntoDB(untranscribed_videos.get(i).getVideoID(), tags.get(0));
            Log.i("INSERTTAG",untranscribed_videos.get(i).getVideoID()+" * " +tags.toString());
            insertTagsIntoDB(untranscribed_videos.get(i).getVideoID(), tags);

        }

        Log.i("GOT","Finished analyzing");

    }

    private void insertTitleIntoDB(int videoID, String title) {
        SQLiteDatabase db= openOrCreateDatabase("vidalocaDB", MODE_PRIVATE, null);

        //set analyzed as true
        db.execSQL("UPDATE vidaloca SET videoTitle='" + title + "' WHERE ID=" + videoID + ";");

    }
    private static ArrayList getEntityObjectFromJSON(JSONObject result) {
        //ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();
        try {
            JSONArray jsonArray = result.getJSONArray("entities");
            Log.i("GOT",jsonArray.toString() + "");

            for (int a = 0; a < jsonArray.length(); a++) {

               /* Entity e = new Entity();

                e.original_text = jsonArray.getJSONObject(a).getString("original_text");
                e.normalized_text = jsonArray.getJSONObject(a).getString("normalized_text");
                e.type = jsonArray.getJSONObject(a).getString("type");
                e.normalized_length = jsonArray.getJSONObject(a).getInt("normalized_length");
                e.original_length = jsonArray.getJSONObject(a).getInt("original_length");
                e.score = jsonArray.getJSONObject(a).getDouble("score");
                */

                // JSON data with in JSONObject "additional_information"
                //e.additional_information = jsonArray.getJSONObject(a).getJSONObject("additional_information").getString("image");
                //e.additional_information = jsonArray.getJSONObject(a).getJSONObject("additional_information").getInt("wikidata_id");

                JSONObject jObj = jsonArray.getJSONObject(a);
                String tag = jObj.getString("original_text");
                tags.add(tag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tags;
    }

    private ArrayList<String> getTagsFromJSON(String json_data) {


        ArrayList<String> tags = new ArrayList();

        //get entities
        try {
            tags = getEntityObjectFromJSON(new JSONObject(json_data));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return tags;
    }

    //ArrayList<String> documents= new ArrayList();
    //documents = getDocumentObjects(data);

    private static ArrayList getDocumentObjects(String data) {

        ArrayList<Document> documents = new ArrayList<>();

        try {
            JSONObject result = new JSONObject(data);

            JSONArray jsonArray = result.getJSONArray("document");

            for(int a=0;a<jsonArray.length();a++)
            {
                Document d = new Document();

                d.offset = jsonArray.getJSONObject(a).getInt("offset");
                d.content = jsonArray.getJSONObject(a).getString("content");

                documents.add(d);

            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return documents;
    }

    private ArrayList<VidaVideo> getUntranscribedVideos() {

        ArrayList<VidaVideo> untranscribed_videos = new ArrayList<>();

        //select from db
        SQLiteDatabase db= openOrCreateDatabase("vidalocaDB", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM vidaloca WHERE isTranscribed=0", null);

        if(c.moveToFirst()) {

            Log.i("GOT","1st step c NOT NULL" + "");

            do {

                //make new temp video, to be added to mVideos
                VidaVideo v= new VidaVideo();

                String currentPath = c.getString(c.getColumnIndex("videoPath"));
                int videoID = c.getInt(c.getColumnIndex("id"));

                v.setVideoPath(currentPath);
                v.setVideoID(videoID);

                untranscribed_videos.add(v);

                Log.i("GOT", "Inside do block");

            }while (c.moveToNext());
        }

        return  untranscribed_videos;
    }

    private void insertTagsIntoDB(int ID, ArrayList<String> tags) {

        SQLiteDatabase db= openOrCreateDatabase("vidalocaDB", MODE_PRIVATE, null);

        //set analyzed as true
        db.execSQL("UPDATE vidaloca SET isTranscribed=1 WHERE ID="+ID+";");


        Log.i("INSERTO",ID + ":*:" + tags.toString());
        //insert tags into DB
        for(int i=0; i<tags.size();i++) {
            String tag = tags.get(i);
            Log.i("PRINT",tag + " ->" + ID);
            db.execSQL("INSERT INTO vidalocaTags (videoID, tag) VALUES( " + ID + ", '" + tag + "' );");
        }
    }

    private void populateDataView() {

        mVideos.clear();
        curVideos.clear();

        //select from db
        SQLiteDatabase db = openOrCreateDatabase("vidalocaDB", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM vidaloca", null);

        ArrayList<VidaVideo> videoList = new ArrayList<>();
        if (c.moveToFirst()) {
            do {

                //make new temp video, to be added to mVideos
                VidaVideo v = new VidaVideo();


                String curT = "Uninitialized Video";
                if (!c.isNull(c.getColumnIndex("videoTitle"))) {
                    curT = c.getString(c.getColumnIndex("videoTitle"));
                }
                int videoID = c.getInt(c.getColumnIndex("id"));
                String currentPath = c.getString(c.getColumnIndex("videoPath"));
                String duration = c.getString(c.getColumnIndex("curDuration"));
                int t = c.getInt(c.getColumnIndex("isTranscribed"));

                Log.i("GOT", t + "");
                v.setmTitle(curT);
                v.setmDuration(duration);
                v.setVideoID(videoID);
                v.setVideoPath(currentPath);


                //get thumbnail
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(currentPath,
                        MediaStore.Images.Thumbnails.MINI_KIND);

                v.setPhotoId(thumb);

                videoList.add(v);//add each video to view

            } while (c.moveToNext());


            Cursor cTags = db.rawQuery("SELECT * FROM vidalocaTags", null);

            if (cTags.moveToFirst()) {

                do {

                    String tag = cTags.getString(cTags.getColumnIndex("tag"));
                    int videoId = cTags.getInt(cTags.getColumnIndex("videoID"));
                    Log.i("Looper",videoId + " ");
                    for (int i = 0; i < videoList.size(); i++) {
                        VidaVideo v = videoList.get(i);

                        Log.i("CURTAG",v.getVideoID()+" & "+videoId+" tag " + tag);

                        if (v.getVideoID() == videoId) {
                            v.mTags.add(tag);
                        }
                    }

                } while (cTags.moveToNext());

            }

            Log.i("MVIDEOS",videoList.toString());
            for(int i=0;i<videoList.size();i++){
                VidaVideo cv = videoList.get(i);
                cv.setmDescription(getDescription(cv.mTags));
                mVideos.add(videoList.get(i));
                curVideos.add(videoList.get(i));
                Log.i("TAGSO", videoList.get(i).mTags.toString());
            }

            rv.getAdapter().notifyDataSetChanged();

        }
    }

    private String getDescription(ArrayList<String> mT){
        String result="";
        for(int i=0;i<mT.size();i++){
            result += mT.get(i) + " ";
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This method creates an ArrayList that has three VidaVideo objects
    // Checkout the project associated with this tutorial on Github if
    // you want to use the same images.
    private void initializeData() {

        // Get Videos from the Gallery
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};

        //open DB connection
        SQLiteDatabase db= openOrCreateDatabase("vidalocaDB", MODE_PRIVATE, null);

        //create table if not exists
        db.execSQL("CREATE TABLE IF NOT EXISTS vidaloca(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "videoTitle varchar(50), videoPath varchar(100), curDuration varchar(100), isTranscribed int(1))");

        //create table if not exists
        db.execSQL("CREATE TABLE IF NOT EXISTS vidalocaTags(id INTEGER PRIMARY KEY AUTOINCREMENT, videoID int(3), tag varchar(100))");


        //get videos stored(do not insert them again)
        Cursor c = db.rawQuery("SELECT videoPath FROM vidaloca", null);

        ArrayList<String> storedVideosPaths = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                storedVideosPaths.add(c.getString(c.getColumnIndex("videoPath")));
            }
            while (c.moveToNext());
        }

        //get videos in gallery
        c = this.getContentResolver().query(uri, projection, null, null, null);

        if(c.moveToFirst()) {
            do {
                String currentPath = c.getString(0);

                // If the current videoPath is already stored, continue. Do not insert into DB again
                if(storedVideosPaths.contains(currentPath)){
                    continue;
                }

                //insert new video:

                // Getting cur duration in format specified
                int millis = MediaPlayer.create(MainActivity.this,
                        Uri.fromFile(new File(currentPath))).getDuration();
                String curDuration = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );

                //get thumbnail
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(currentPath,
                        MediaStore.Images.Thumbnails.MINI_KIND);

                //insert new viddeo into DB
                db.execSQL("INSERT INTO vidaloca (videoPath,curDuration,isTranscribed) VALUES( "
                        + "'" + currentPath + "', '" + curDuration + "' ,0 );");

            }
            while (c.moveToNext());
        }
    }// End of initialize data function

    private class AnalyzeFilesTask extends AsyncTask< Void,Void,Void> implements com.angelhack.vidaloca.AnalyzeFilesTask {

        @Override
        protected void onPostExecute(Void s){
            Log.i("GOT","Inside on post execute");
            populateDataView();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //start analyzing new videos
            analyzeNewVideos();
            Log.i("GOT","Finished Analyzing");
            return null;
        }
    }


}
