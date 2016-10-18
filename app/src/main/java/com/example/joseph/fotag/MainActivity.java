package com.example.joseph.fotag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends AppCompatActivity implements Observer{
    static model model;
    GridView grid;
    final Context mContext = this;
    public static String PACKAGE_NAME;
    ImageAdapter img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView reset = (TextView)findViewById(R.id.textView);
        if(model==null) {
            model = new model(mContext);

        }
        model.addObserver(this);

        final RatingBar filter = (RatingBar)findViewById(R.id.ratingBar);
        assert reset != null;
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setFilter(0f);
                filter.setRating(0f);
            }
        });

        assert filter != null;
        filter.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                model.setFilter(filter.getRating());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.google.com/search?tbm=isch&q=hello+world");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Log.d("MVC","Observer added?");
        grid = (GridView)findViewById(R.id.gridView);
        img = new ImageAdapter(this,model);
        grid.setAdapter(img);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //do nothing for now
            }
        });

        PACKAGE_NAME = getApplicationContext().getPackageName();

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
        switch(id){
            case R.id.action_clear:
                model.clear();
                break;
            case R.id.action_load_default:
                model.addDefaults();
                Snackbar.make(getWindow().getDecorView(), "Default Images Loaded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               // model.setDef(Boolean.TRUE);
                //grid.setAdapter(img);
                //defImageLoad();
                break;
            case R.id.action_add:
                addUri();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    public void addUri() {
        LayoutInflater li = LayoutInflater.from(mContext);
        View uriLoader = li.inflate(R.layout.uri_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);


        alertDialogBuilder.setView(uriLoader);

        final EditText userInput = (EditText) uriLoader
                .findViewById(R.id.uri_text);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                model.addImageFromUri(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public class gisearch extends AsyncTask<Void, Void,Void>{

        ArrayList<String> imgs = new ArrayList<>();

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Log.d("MVC","update called! woot!");
        img.update();
    }
}
