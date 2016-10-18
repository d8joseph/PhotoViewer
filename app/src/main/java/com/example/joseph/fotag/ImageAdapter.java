package com.example.joseph.fotag;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joseph on 2016-04-02.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private model model;
    private ArrayList<Integer> imList;

    public ImageAdapter(Context c, model m) {
        mContext = c;
        model = m;
        imList = model.getImages();
    }

    public int getCount() {
        Log.d("ImageAdapter","Imlist size = "+imList.size());
        return imList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class Labels {
        TextView textView;
        RatingBar ratingBar;
        ImageView imageView;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        View imagePanel;
        final LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            imagePanel = new View(mContext);
            // LayoutInflater inflater = (LayoutInflater) mContext
            //       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            imagePanel = inflater.inflate(R.layout.image_panel, null);
            //labels = new Labels();
            TextView textView = (TextView) imagePanel.findViewById(R.id.image_name);
            textView.setText(model.getName(imList.get(position)));
            final ImageView imageView = (ImageView) imagePanel.findViewById(R.id.imageView);
           // RatingBar ratingBar = (RatingBar) imagePanel.findViewById(R.id.ratingViewer);
            // imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(800, 600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        model.getPreview(imList.get(position),imageView);
            //ADDD IMAGE SOURCE!
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   View fullScreen = inflater.inflate(R.layout.full_screen,null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext);


                    alertDialogBuilder.setView(fullScreen);


                    ImageView im = (ImageView)fullScreen.findViewById(R.id.imageFS);

                    model.getImage(imList.get(position), im);
                    final AlertDialog alertDialog = alertDialogBuilder.create();


                    alertDialog.show();
                    Log.d("DIALOG", "Height = " + im.getHeight() + " Width = " + im.getWidth());
                    im.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    //AlertDialog alertDialog = alertDialogBuilder.create();


                }
            });
            RatingBar ratingBar = (RatingBar) imagePanel.findViewById(R.id.ratingViewer);
            ratingBar.setRating(model.getRating(imList.get(position)));
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Log.d("ImageAdapter","SetRating for: "+imList.get(position)+ " at position "+position);
                    model.setRating(imList.get(position),ratingBar.getRating());
                }
            });




        return imagePanel;
    }
    public void update(){
        imList.clear();
        imList = null;
        imList = model.getImages();
       /* Log.d("ImageAdapter","ImList Size = "+imList.size());
        Log.d("ImageAdapter","ImList Elements : ");
        for (Integer i:imList
             ) {
            Log.d("ImageAdapter",model.getName(imList.get(i)));

        }*/
        notifyDataSetChanged();
    }

}
