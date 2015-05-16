package com.angelhack.vidaloca;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private Toolbar mToolBar;
    private EditText mSearchBar;

    private List<VidaVideo> mVideos;

    // This method creates an ArrayList that has three VidaVideo objects
    // Checkout the project associated with this tutorial on Github if
    // you want to use the same images.
    private void initializeData() {
        mVideos = new ArrayList<>();
        mVideos.add(new VidaVideo("Running", "Time : 23 Minutes 35 Seconds Calories Burnt : 200","16:47",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("Walking", "Time : 30 Minutes 15 Seconds Calories Burnt : 486","23:51",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("ic_launcher Intake", "ic_launcher Description : 2 Samosas Calories Intake : 332","23:51",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("ic_launcher Intake", "ic_launcher Description : 2 Samosas Calories Intake : 332","23:51",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("Walking", "Time : 30 Minutes 15 Seconds Calories Burnt : 486","23:51",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("Running", "Time : 15 Miuntes 12 Seconds Calories Burnt : 389","23:51",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("ic_launcher Intake", "ic_launcher Description : Some 234 Samosas Calories Intake : 332","23:51",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("ic_launcher Intake", "ic_launcher Description : 2 Samosas Calories Intake : 332","23:51",R.drawable.ic_launcher));
        mVideos.add(new VidaVideo("Running", "Time : 15 Miuntes 12 Seconds Calories Burnt : 389","23:51",R.drawable.ic_launcher));

    }

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


        // Getting the recycler view and setting its adapter
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        // Populating the Model
        initializeData();

        // Craeting the Adapter from the Model
        RVAdapter adapter = new RVAdapter(mVideos);
        // Setting the Adapter for the recycler view
        rv.setAdapter(adapter);
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


}
